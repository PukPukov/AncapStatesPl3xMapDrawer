package ru.ancap.drawer.pl3xmap.states.ancap;

import net.pl3x.map.core.markers.layer.WorldLayer;
import net.pl3x.map.core.markers.marker.Icon;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.markers.option.Options;
import net.pl3x.map.core.markers.option.Popup;
import net.pl3x.map.core.world.World;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import ru.ancap.states.AncapStates;
import ru.ancap.states.states.city.City;
import ru.ancap.states.wars.AncapWars;
import ru.ancap.states.wars.api.castle.Castle;
import ru.ancap.states.wars.api.field.FieldConflict;
import ru.ancap.states.wars.api.state.WarState;
import ru.ancap.states.wars.plugin.listener.AssaultRuntime;

import java.util.Collection;
import java.util.HashSet;

public class IconsLayer extends WorldLayer {
    
    public static final String KEY = "ancap_city_icons";
    
    public IconsLayer(World world) {
        super(KEY, world, () -> "Значки государств");
        super.setPriority(51);
    }
    
    @Override
    public @NotNull Collection<Marker<?>> getMarkers() {
        Collection<Marker<?>> markers = new HashSet<>();
        org.bukkit.World bukkitWorld = Bukkit.getWorld(super.getWorld().getName());
        if (bukkitWorld == null) return markers;
        for (City city : AncapStates.cityMap().cities()) {
            ASCustomIcon icon = ASCustomIcon.BLUE_FLAG;
            if (city.isFree()) icon = ASCustomIcon.GREEN_FLAG;
            else if (city.mayor().isLeader()) icon = ASCustomIcon.KING;
            markers.add(new Icon(DrawUtil.nextId(), DrawUtil.locationToPl3xPoint(city.getHome()), icon.key()).setOptions(Options.builder()
                .popup(new Popup(String.join("<br>", city.getDescription().description())))
            ));
            for (FieldConflict conflict : AncapWars.fieldConflicts().attacksTo(WarState.of(city.id()).warActor())) {
                markers.add(new Icon(DrawUtil.nextId(), DrawUtil.hexagonPointToPl3x(AncapStates.grid.hexagon(conflict.hexagon().code())), ASCustomIcon.FIRE.key()).setOptions(Options.builder()
                    .popup(new Popup(
                        "Гексагон под атакой!<br>" +
                            "Атакующий: " + conflict.attacker().getName() + "<br>" +
                            "Процент захвата: " + ((1 - ((double) conflict.health() / (double) conflict.maxHealth())) * 100)
                    ))
                ));
            }
        }
        for (Castle castle : AncapWars.warMap().getCastles()) {
            if (castle.protectLevel() <= 1) continue;
            if (AncapWars.assaults().assault(castle.hexagon().code()) instanceof AssaultRuntime.Realized realized) {
                markers.add(new Icon(DrawUtil.nextId(), DrawUtil.locationToPl3xPoint(castle.getLocation()), ASCustomIcon.SHIELD.key()).setOptions(Options.builder()
                    .popup(new Popup(
                        castle.name() + "<br>" +
                            "Замок под атакой!<br>" +
                            "Атакующий: " + realized.attacker().getName()
                    ))
                ));
            } else {
                markers.add(new Icon(DrawUtil.nextId(), DrawUtil.locationToPl3xPoint(castle.getLocation()), ASCustomIcon.TOWER.key()).setOptions(Options.builder()
                    .popup(new Popup(castle.name()))
                ));
            }
            
        }
        return markers;
    }
    
}