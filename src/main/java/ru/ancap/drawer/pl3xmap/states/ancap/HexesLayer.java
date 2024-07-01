package ru.ancap.drawer.pl3xmap.states.ancap;

import net.pl3x.map.core.markers.layer.WorldLayer;
import net.pl3x.map.core.markers.marker.Icon;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.markers.marker.Polygon;
import net.pl3x.map.core.markers.marker.Polyline;
import net.pl3x.map.core.markers.option.Fill;
import net.pl3x.map.core.markers.option.Options;
import net.pl3x.map.core.markers.option.Popup;
import net.pl3x.map.core.world.World;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import ru.ancap.hexagon.Hexagon;
import ru.ancap.states.AncapStates;
import ru.ancap.states.states.city.City;
import ru.ancap.states.wars.api.hexagon.WarHexagon;

import java.util.*;
import java.util.List;

public class HexesLayer extends WorldLayer {

    public static final String KEY = "ancap_hexagons";

    public HexesLayer(World world) {
        super(KEY, world, () -> "Территории");
        super.setPriority(50);
    }

    @Override
    public @NotNull Collection<Marker<?>> getMarkers() {
        Collection<Marker<?>> markers = new HashSet<>();
        org.bukkit.World bukkitWorld = Bukkit.getWorld(super.getWorld().getName());
        if (bukkitWorld == null) {
            return markers;
        }
        List<Marker<?>> list = new ArrayList<>();
        for (City city : AncapStates.getCityMap().getCities()) {
            for (Hexagon hex : city.getTerritories()) {
                Polygon polygon = new Polygon(
                    "hex_" + hex.code(),
                    Polyline.of(
                        "line_" + hex.code(),
                        hex.vertexes().stream().map(DrawUtil::vertexPointToPl3x).toList()
                    )
                ).setOptions(Options.builder()
                    .fillColor(DrawUtil.color(city.getColor()))
                    .fillType(Fill.Type.NONZERO)
                    .popup(new Popup(String.join("<br>", city.getDescription().description())))
                    .stroke(false)
                );
                list.add(polygon);

                if (new WarHexagon(hex.code()).devastation() instanceof WarHexagon.DevastationStatus.Devastated devastated) {
                    list.add(new Icon(DrawUtil.nextId(), devastated.brokenCastleLocation().getBlockX(), devastated.brokenCastleLocation().getBlockZ(), "warning"));
                }
            }
        }
        markers.addAll(list);
        return markers;
    }

}