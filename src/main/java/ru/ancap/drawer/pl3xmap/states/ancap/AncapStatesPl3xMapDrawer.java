package ru.ancap.drawer.pl3xmap.states.ancap;

import lombok.SneakyThrows;
import net.pl3x.map.core.Pl3xMap;
import net.pl3x.map.core.image.IconImage;
import net.pl3x.map.core.registry.IconRegistry;
import ru.ancap.commons.resource.ResourceSource;
import ru.ancap.framework.plugin.api.AncapPlugin;

import java.awt.image.BufferedImage;
import java.util.Objects;

public final class AncapStatesPl3xMapDrawer extends AncapPlugin {
    
    public static AncapStatesPl3xMapDrawer INSTANCE;
    
    @Override
    public void onEnable() {
        super.onEnable();
        INSTANCE = this;
        this.newResourceSource(new ImageSource());
        addIcons(this.newResourceSource(new ImageSource()));
        Registrator.init();
    }
    
    @SneakyThrows
    public static void addIcons(ResourceSource<BufferedImage> resourceSource) {
        var registry = Pl3xMap.api().getIconRegistry();
        registerIconFromDir(registry, resourceSource, "blue_flag");
        registerIconFromDir(registry, resourceSource, "green_flag");
        registerIconFromDir(registry, resourceSource, "king");
        registerIconFromDir(registry, resourceSource, "tower");
        registerIconFromDir(registry, resourceSource, "shield");
        registerIconFromDir(registry, resourceSource, "fire");
        registerIconFromDir(registry, resourceSource, "warning");
    }
    
    @SneakyThrows
    private static void registerIconFromDir(IconRegistry registry, ResourceSource<BufferedImage> resourceSource, String key) {
        registry.register(key, new IconImage(key, Objects.requireNonNull(resourceSource.getResource(key + ".png")), "png"));
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
}