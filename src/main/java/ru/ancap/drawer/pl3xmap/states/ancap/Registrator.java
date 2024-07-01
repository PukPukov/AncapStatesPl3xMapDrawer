package ru.ancap.drawer.pl3xmap.states.ancap;

import lombok.SneakyThrows;
import net.pl3x.map.core.Pl3xMap;
import net.pl3x.map.core.event.EventHandler;
import net.pl3x.map.core.event.EventListener;
import net.pl3x.map.core.event.server.Pl3xMapEnabledEvent;
import net.pl3x.map.core.event.server.ServerLoadedEvent;
import net.pl3x.map.core.event.world.WorldLoadedEvent;
import net.pl3x.map.core.event.world.WorldUnloadedEvent;
import net.pl3x.map.core.image.IconImage;
import net.pl3x.map.core.world.World;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import ru.ancap.commons.Pair;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Registrator implements EventListener, Listener {
    
    private Registrator() { }
    
    public static Registrator init() {
        var registrator = new Registrator();
        registrator.register();
        return registrator;
    }
    
    public void register() {
        Pl3xMap.api().getEventRegistry().register(this);
    }

    @EventHandler
    public void onPl3xMapEnabled(@NotNull Pl3xMapEnabledEvent event) {
    }

    @EventHandler
    public void onServerLoaded(@NotNull ServerLoadedEvent event) {
        Pl3xMap.api().getWorldRegistry().forEach(this::registerWorld);
    }

    @EventHandler
    public void onWorldLoaded(@NotNull WorldLoadedEvent event) {
        registerWorld(event.getWorld());
    }

    @EventHandler
    public void onWorldUnloaded(@NotNull WorldUnloadedEvent event) {
        try {
            event.getWorld().getLayerRegistry().unregister(HexesLayer.KEY);
        } catch (Throwable ignore) {
        }
    }

    @SneakyThrows
    private void registerWorld(@NotNull World world) {
        File folder = new File(AncapStatesPl3xMapDrawer.INSTANCE.getDataFolder(), "icons");
        folder.mkdirs();

        List<Pair<String, BufferedImage>> images;
        try (var paths = Files.list(Paths.get(folder.getAbsolutePath()))) { images = paths
            .filter(Files::isRegularFile)
            .map(file -> {
                try { return new Pair<>(FilenameUtils.getBaseName(file.getFileName().toString()), ImageIO.read(file.toFile())); } 
                catch (IOException e) { throw new RuntimeException(e); }
            })
            .toList();
        }
        
        images.forEach(image -> {
            try {
                Pl3xMap.api().getIconRegistry().register(image.getKey(), new IconImage(image.getKey(), image.getValue(), "png"));
            } catch (Exception exception) {
                System.err.println(exception.getClass().getSimpleName());
            }
        });
        
        world.getLayerRegistry().register(new HexesLayer(world));
        world.getLayerRegistry().register(new BordersLayer(world));
        world.getLayerRegistry().register(new IconsLayer(world));
    }
    
}