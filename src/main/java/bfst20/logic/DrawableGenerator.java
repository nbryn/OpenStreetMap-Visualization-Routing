package bfst20.logic;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import bfst20.logic.entities.Node;
import bfst20.logic.entities.Relation;
import bfst20.logic.entities.Way;
import bfst20.presentation.LinePath;
import javafx.scene.paint.Color;

public class DrawableGenerator {

    private List<Way> OSMWays;
    private Map<Long, Node> OSMNodes;
    private List<Relation> OSMRelations;
    private static boolean loaded = false;
    private static DrawableGenerator drawableGenerator;
    private AppController appController;

    private DrawableGenerator() {
        appController = new AppController();
        OSMWays = appController.getOSMWaysFromModel();
        OSMNodes = appController.getOSMNodesFromModel();
        OSMRelations = appController.getOSMRelationsFromModel();
        appController.clearModelData();
    }

    public void clearData() {
        OSMNodes = null;
        OSMWays = null;
        OSMRelations = null;
    }

    public static DrawableGenerator getInstance() {
        if (!loaded) {
            drawableGenerator = new DrawableGenerator();
        }

        return drawableGenerator;
    }

    public void createDrawables() {
        createWays();

        createRelations();

    }

    private void createWays() {
        for (Way way : OSMWays) {
            if (way.getType() == Type.COASTLINE || way.getType() == null) continue;

            LinePath linePath = createLinePath(way);

            Type type = linePath.getType();

            if (appController.getDrawablesFromModel().get(type) == null) {
                appController.addTypeListToModel(type);
            }

            if (type != Type.PLACE) {
                appController.addLinePathToModel(type, linePath);
            }
        }
    }

    private void createRelations() {
        for (Relation relation : OSMRelations) {
            if (relation.getType() == Type.FOREST) {

                connectWays(relation, Type.FOREST);

            } else if (relation.getType() == Type.FARMLAND) {

                connectWays(relation, Type.FARMLAND);

            } else if (relation.getName() != null && relation.getName().contains("Region")) {

                if (!appController.getDrawablesFromModel().containsKey(Type.COASTLINE)) {
                    appController.addTypeListToModel(Type.COASTLINE);
                }

                connectWays(relation, Type.COASTLINE);
            }

            addRelation(Type.FOREST, appController.getNodeTo(Type.FOREST));
            addRelation(Type.FARMLAND, appController.getNodeTo(Type.FARMLAND));
            addRelation(Type.COASTLINE, appController.getNodeTo(Type.COASTLINE));

        }
    }

    private void addRelation(Type type, Map<Node, Way> nodeTo) {
        for (Map.Entry<Node, Way> entry : nodeTo.entrySet()) {
            if (entry.getKey() == OSMNodes.get(entry.getValue().getLastNodeId())) {

                LinePath linePath = new LinePath(entry.getValue(), type, OSMNodes, Type.getColor(type), true);
                appController.addLinePathToModel(type, linePath);

            }
        }
    }

    private void connectWays(Relation relation, Type type) {
        Collections.sort(relation.getMembers());
        for (long entry : relation.getMembers()) {
            Way way = (binarySearch(OSMWays, entry));

            if (way == null) continue;

            Way before = removeWayBefore(way, type);
            Way after = removeWayAfter(way, type);

            way = merge(merge(before, way), after);

            appController.addToMapInModel(type, OSMNodes.get(way.getFirstNodeId()), way);
            appController.addToMapInModel(type, OSMNodes.get(way.getLastNodeId()), way);
        }
    }


    private Way removeWayAfter(Way way, Type type) {
        Node node = OSMNodes.get(way.getLastNodeId());
        Way after = appController.removeWayFromNodeTo(type, node);
        if (after != null) {
            Node firstNode = OSMNodes.get(after.getFirstNodeId());
            Node lastNode = OSMNodes.get(after.getLastNodeId());
            appController.removeWayFromNodeTo(type, firstNode);
            appController.removeWayFromNodeTo(type, lastNode);

        }
        return after;
    }

    private Way removeWayBefore(Way way, Type type) {
        Node node = OSMNodes.get(way.getFirstNodeId());
        Way before = appController.removeWayFromNodeTo(type, node);
        if (before != null) {
            Node firstNode = OSMNodes.get(before.getFirstNodeId());
            Node lastNode = OSMNodes.get(before.getLastNodeId());
            appController.removeWayFromNodeTo(type, firstNode);
            appController.removeWayFromNodeTo(type, lastNode);
        }
        return before;
    }


    private LinePath createLinePath(Way way) {
        Type type = Type.UNKNOWN;

        try {
            type = way.getType();
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
        Color color = Type.getColor(type);
        Boolean fill = Type.getFill(type);

        return new LinePath(way, type, OSMNodes, color, fill);
    }


    private Way merge(Way before, Way after) {
        if (before == null) return after;
        if (after == null) return before;

        Way way = new Way();
        // Why do we need this? Seems to do the same without it
        if (before.getFirstNodeId() == after.getFirstNodeId()) {
            way.addAllNodeIds(before);

            Collections.reverse(way.getNodeIds());
            way.getNodeIds().remove(way.getNodeIds().size() - 1);
            way.addAllNodeIds(after);

        } else if (before.getFirstNodeId() == after.getLastNodeId()) {

            addWayToMerge(way, after, before);

        } else if (before.getLastNodeId() == after.getFirstNodeId()) {

            addWayToMerge(way, before, after);
        }

        // Why do we need this? Seems to do the same without it
        else if (before.getLastNodeId() == after.getLastNodeId()) {
            Way tmp = new Way(after);

            Collections.reverse(tmp.getNodeIds());
            way.addAllNodeIds(before);
            way.getNodeIds().remove(way.getNodeIds().size() - 1);
            way.addAllNodeIds(tmp);
        } else {
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