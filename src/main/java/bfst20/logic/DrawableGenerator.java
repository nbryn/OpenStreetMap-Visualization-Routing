package bfst20.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bfst20.logic.AppController;
import bfst20.logic.entities.Node;
import bfst20.logic.entities.Relation;
import bfst20.logic.entities.Way;
import bfst20.logic.Type;
import bfst20.logic.interfaces.Drawable;
import bfst20.presentation.LinePath;
import bfst20.presentation.Parser;
import javafx.scene.paint.Color;

public class DrawableGenerator {

    Map<Type, List<Drawable>> drawables = new HashMap<>();
    Map<Node, Way> nodeToCoastline = new HashMap<>();
    List<Way> OSMWays;
    Map<Long, Node> OSMNodes;
    List<Relation> OSMRelations;
    List<Drawable> islands = new ArrayList<>();
    static boolean loaded = false;
    static DrawableGenerator drawableGenerator;
    AppController appController;

    private DrawableGenerator() {
        appController = new AppController();
        OSMWays = appController.getOSMWaysFromModel();
        OSMNodes = appController.getOSMNodesFromModel();
        OSMRelations = appController.getOSMRelationsFromModel();
    }

    public static DrawableGenerator getInstance() {
        if (loaded == false) {
            drawableGenerator = new DrawableGenerator();
        }

        return drawableGenerator;
    }

    public Map<Type, List<Drawable>> createDrawables() {

        createWays();

        createRelations();

        addCoastlines();

        return drawables;
    }

    private void createWays() {
        for (Way way : OSMWays) {
            if (way.getTagValue("natural") != null && way.getTagValue("natural").equals("coastline")) continue;

            LinePath linePath = createLinePath(way);

            if (linePath.getType() == Type.LANDUSE) {

                try {
                    for (Type t : Type.values()) {

                        // TODO: Add all landuse values to Types
                        if (way.getTagValue(linePath.getType().toString().toLowerCase()) != null) {
                            if (way.getTagValue(linePath.getType().toString().toLowerCase()).equals(t.toString().toLowerCase())) {


                                linePath.setType(way.getTagValue(linePath.getType().toString().toLowerCase()));
                                Color color = Type.getColor(linePath.getType());
                                Boolean fill = Type.getFill(linePath.getType());

                                linePath.setColor(color);
                                linePath.setFill(fill);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            Type type = linePath.getType();
            if (drawables.get(type) == null) {

                drawables.put(type, new ArrayList<>());
            }

            if (type != Type.PLACE) {
                drawables.get(type).add(linePath);
            }
        }
    }

    private void createRelations() {
        for (Relation relation : OSMRelations) {
            if (relation.getTag("name").contains("Region")) {
                Collections.sort(relation.getMembers());
                for (long entry : relation.getMembers()) {
                    Way way = (binarySearch(OSMWays, entry));

                    if (way != null && way.getTagValue("natural") != null
                            && way.getTagValue("natural").equals("coastline")) {

                        Way before = removeWayBefore(way);
                        Way after = removeWayAfter(way);

                        way = merge(merge(before, way), after);
                        nodeToCoastline.put(OSMNodes.get(way.getFirstNodeId()), way);
                        nodeToCoastline.put(OSMNodes.get(way.getLastNodeId()), way);
                    }
                }
            }
        }
    }

    private void addCoastlines() {
        if (!drawables.containsKey(Type.COASTLINE)) {
            drawables.put(Type.COASTLINE, new ArrayList<>());
        }


        for (Map.Entry<Node, Way> entry : nodeToCoastline.entrySet()) {
            if (entry.getKey() == OSMNodes.get(entry.getValue().getLastNodeId())) {


                drawables.get(Type.COASTLINE).add(
                        new LinePath(entry.getValue(), Type.COASTLINE, OSMNodes, Type.getColor(Type.COASTLINE), true));
            }
        }
    }

    private Way removeWayAfter(Way way) {
        Way after = nodeToCoastline.remove(OSMNodes.get(way.getLastNodeId()));
        if (after != null) {
            nodeToCoastline.remove(OSMNodes.get(after.getFirstNodeId()));
            nodeToCoastline.remove(OSMNodes.get(after.getLastNodeId()));
        }
        return after;
    }

    private Way removeWayBefore(Way way) {
        Way before = nodeToCoastline.remove(OSMNodes.get(way.getFirstNodeId()));
        if (before != null) {
            nodeToCoastline.remove(OSMNodes.get(before.getFirstNodeId()));
            nodeToCoastline.remove(OSMNodes.get(before.getLastNodeId()));
        }
        return before;
    }

    private LinePath createLinePath(Way way) {
        Type type = Type.UNKNOWN;


        try {
            type = Type.valueOf(way.getFirstTag()[0].toUpperCase());
        } catch (Exception err) {
        }

        Color color = Type.getColor(type);
        Boolean fill = Type.getFill(type);

        return new LinePath(way, type, OSMNodes, color, fill);
    }


    public Way merge(Way before, Way after) {
        if (before == null) return after;
        if (after == null) return before;

        Way way = new Way();
        // Why do we need this? Seems to do the same without it
        /*if (before.getFirstNodeId() == after.getFirstNodeId()) {
            way.addAllNodeIds(before);
            
            Collections.reverse(way.getNodeIds());
            way.getNodeIds().remove(way.getNodeIds().size() - 1);
            way.addAllNodeIds(after);
        } else*/
        if (before.getFirstNodeId() == after.getLastNodeId()) {

            addWayToMerge(way, after, before);

        } else if (before.getLastNodeId() == after.getFirstNodeId()) {

            addWayToMerge(way, before, after);
        }

        // Why do we need this? Seems to do the same without it
        /* else if (before.getLastNodeId() == after.getLastNodeId()) {
            Way tmp = new Way(after);
            
            Collections.reverse(tmp.getNodeIds());
            way.addAllNodeIds(before);
            way.getNodeIds().remove(way.getNodeIds().size() - 1);
            way.addAllNodeIds(tmp);
        }*/
        else {
            throw new IllegalArgumentException("Cannot merge unconnected OSMWays");
        }

        return way;
    }

    //Order of before and after depends on the context
    private void addWayToMerge(Way way, Way before, Way after) {
        way.addAllNodeIds(before);
        way.getNodeIds().remove(way.getNodeIds().size() - 1);
        way.addAllNodeIds(after);
    }


    private Way binarySearch(List<Way> list, long id) {
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