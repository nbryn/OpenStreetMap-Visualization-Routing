package bfst20.logic.drawables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bfst20.logic.entities.Node;
import bfst20.logic.entities.Relation;
import bfst20.logic.entities.Way;
import bfst20.logic.interfaces.Drawable;
import bfst20.presentation.Parser;
import javafx.scene.paint.Color;

public class DrawableFactory {
    public static Map<Type, List<Drawable>> createDrawables() {
        Map<Type, List<Drawable>> drawables = new HashMap<>();

        Parser parser = Parser.getInstance();
        Map<Long, Way> OSMWays = parser.getOSMWays();
        Map<Long, Node> OSMNodes = parser.getOSMNodes();
        List<Relation> OSMRelations = parser.getOSMRelations();
        List<Drawable> islands = new ArrayList<>();

        Map<Node, Way> nodeToCoastline = new HashMap<>();

        for (Map.Entry<Long, Way> entry : OSMWays.entrySet()) {

            Way way = entry.getValue();

            Type type = Type.UNKNOWN;

            try {
                type = Type.valueOf(way.getFirstTag()[0].toUpperCase());
            } catch (Exception err) {
            }

            Color color = Type.getColor(type);
            Boolean fill = Type.getFill(type);

            if (drawables.get(type) == null) {
                drawables.put(type, new ArrayList<>());
            }

            drawables.get(type).add(new LinePath(entry.getValue(), type, OSMNodes, color, fill));

        }

        System.out.println("RUN");

        System.out.println(OSMRelations.size());

        for (Relation relation : OSMRelations) {
                for (Map.Entry<Long, String> entry : relation.getMembers().entrySet()) {

                    if (entry.getValue().equals("way")) {
                        // 593573608
                        if (entry.getKey() == 593573608) {
                            // System.out.println("Now");
                        }

                        Way way = OSMWays.get(entry.getKey());

                        if (way != null && way.getTagValue("natural") != null
                                && way.getTagValue("natural").equals("coastline")) {

                            Way before = nodeToCoastline.remove(OSMNodes.get(way.getFirstNodeId()));
                            if (before != null) {
                                nodeToCoastline.remove(OSMNodes.get(before.getFirstNodeId()));
                                nodeToCoastline.remove(OSMNodes.get(before.getLastNodeId()));
                            }
                            Way after = nodeToCoastline.remove(OSMNodes.get(way.getLastNodeId()));
                            if (after != null) {
                                nodeToCoastline.remove(OSMNodes.get(after.getFirstNodeId()));
                                nodeToCoastline.remove(OSMNodes.get(after.getLastNodeId()));
                            }
                            way = Way.merge(Way.merge(before, way, OSMNodes), after, OSMNodes);
                            nodeToCoastline.put(OSMNodes.get(way.getFirstNodeId()), way);
                            nodeToCoastline.put(OSMNodes.get(way.getLastNodeId()), way);

                        }
                    }
            }
        }

        for (var entry : nodeToCoastline.entrySet()) {
            if (entry.getKey() == OSMNodes.get(entry.getValue().getLastNodeId())) {
                if (drawables.get(Type.COASTLINE) == null) {
                    drawables.put(Type.COASTLINE, new ArrayList<>());
                }

                drawables.get(Type.COASTLINE).add(
                        new LinePath(entry.getValue(), Type.COASTLINE, OSMNodes, Type.getColor(Type.COASTLINE), true));
            }
        }

        return drawables;
    }

}