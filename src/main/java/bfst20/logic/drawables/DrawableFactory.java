package bfst20.logic.drawables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        List<Way> OSMWays = parser.getOSMWays();
        Map<Long, Node> OSMNodes = parser.getOSMNodes();
        List<Relation> OSMRelations = parser.getOSMRelations();
        List<Drawable> islands = new ArrayList<>();

        Map<Node, Way> nodeToCoastline = new HashMap<>();

        for (Way way : OSMWays) {

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

            drawables.get(type).add(new LinePath(way, type, OSMNodes, color, fill));

        }

        for (Relation relation : OSMRelations) {
            Collections.sort(relation.getMembers());
            for (long entry : relation.getMembers()) {
                Way way = (binarySearch(OSMWays, entry));

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

        for (var entry : nodeToCoastline.entrySet()) {
            if (entry.getKey() == OSMNodes.get(entry.getValue().getLastNodeId())) {
                if (drawables.get(Type.COASTLINE) == null) {
                    drawables.put(Type.COASTLINE, new ArrayList<>());
                }

                System.out.println(entry.getValue().getNodeIds().size());

                drawables.get(Type.COASTLINE).add(
                        new LinePath(entry.getValue(), Type.COASTLINE, OSMNodes, Type.getColor(Type.COASTLINE), true));
            }
        }

        return drawables;
    }

    public static Way binarySearch(List<Way> list, long id) {
        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            Way midElement = list.get(mid);
            long midId = midElement.getId();

            if (midId < id) {
                low = mid + 1;
            } else if (midId > id) {
                high = mid - 1;
            } else {
                return midElement;
            }
        }

        return null;
    }

}