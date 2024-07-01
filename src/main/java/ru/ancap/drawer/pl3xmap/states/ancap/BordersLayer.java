package ru.ancap.drawer.pl3xmap.states.ancap;

import net.pl3x.map.core.markers.layer.WorldLayer;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.markers.marker.Polyline;
import net.pl3x.map.core.markers.option.Options;
import net.pl3x.map.core.markers.option.Popup;
import net.pl3x.map.core.world.World;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import ru.ancap.states.AncapStates;

import java.awt.*;
import java.util.Collection;
import java.util.HashSet;

public class BordersLayer extends WorldLayer {
    
    public static final String KEY = "ancap_borders";
    
    public BordersLayer(World world) {
        super(KEY, world, () -> "Границы");
        super.setPriority(51);
    }
    
    @Override
    public @NotNull Collection<Marker<?>> getMarkers() {
        Collection<Marker<?>> markers = new HashSet<>();
        org.bukkit.World bukkitWorld = Bukkit.getWorld(super.getWorld().getName());
        if (bukkitWorld == null) {
            return markers;
        }
        markers.addAll(AncapStates.cityMap().cities().stream()
            .flatMap(city -> AncapStates.grid.region(new HashSet<>(city.getTerritories())).bounds().stream()
                .map(side -> {
                    var start = side.start();
                    var end = side.end();
                    return new Polyline(DrawUtil.nextId(), DrawUtil.vertexPointToPl3x(start), DrawUtil.vertexPointToPl3x(end))
                        .setOptions(Options.builder()
                            .strokeColor(new Color(0, 0, 0, 255).getRGB())
                            .strokeWeight(6)
                            .popup(new Popup(String.join("<br>", city.getDescription().description())))
                            .build());
                })
            ).toList()
        );
        return markers;
    }
    
}