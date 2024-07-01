package ru.ancap.drawer.pl3xmap.states.ancap;

import net.pl3x.map.core.markers.Point;
import org.bukkit.Location;
import ru.ancap.commons.exception.Try;
import ru.ancap.hexagon.Hexagon;
import ru.ancap.hexagon.HexagonVertex;

import java.awt.*;

public class DrawUtil {

    private static long id = 0;

    public static synchronized String nextId() {
        long nextId = id;
        id++;
        return nextId+"";
    }

    public static Point hexagonPointToPl3x(Hexagon hexagon) {
        ru.ancap.hexagon.common.Point point = hexagon.center();
        return new Point((int) point.x(), (int) point.y());
    }

    public static Point vertexPointToPl3x(HexagonVertex vertex) {
        ru.ancap.hexagon.common.Point point = vertex.position();
        return new Point((int) Math.round(point.x()), (int) (Math.round(point.y())));
    }

    public static Integer color(String colorString) {
        if (colorString.startsWith("#")) colorString = colorString.substring(1);
        int parsed; try                                     { parsed = Integer.parseInt(colorString, 16); } 
                    catch (NumberFormatException exception) { parsed = 0;                                 }
        Color base = new Color(parsed);
        Color transparent = new Color(base.getRed(), base.getGreen(), base.getBlue(), 192);
        return transparent.getRGB();
    }

    public static Point locationToPl3xPoint(Location location) {
        return new Point(location.getBlockX(), location.getBlockZ());
    }
    
}
