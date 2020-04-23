package bfst20.logic;

import java.util.*;

import bfst20.logic.entities.Address;
import bfst20.logic.entities.Node;
import bfst20.logic.entities.Relation;
import bfst20.logic.entities.Way;
import bfst20.logic.entities.LinePath;
import bfst20.logic.misc.OSMType;


public class LinePathGenerator {

    private List<Way> OSMWays;
    private Map<Long, Node> OSMNodes;
    private Map<Long, Address> addresses;
    private List<Relation> OSMRelations;
    private static boolean loaded = false;
    private static LinePathGenerator linePathGenerator;
    private AppController appController;

    private LinePathGenerator() {
        appController = new AppController();
        OSMWays = appController.getWaysFromModel();
        OSMNodes = appController.getNodesFromModel();
        OSMRelations = appController.getRelationsFromModel();
        addresses = appController.getAddressesFromModel();
        appController.clearOSMData();
    }

    public void clearData() {
        OSMNodes = new HashMap<>();
        OSMWays = new ArrayList<>();
        OSMRelations = new ArrayList<>();

        System.gc();
    }

    public static LinePathGenerator getInstance() {
        if (!loaded) {
            linePathGenerator = new LinePathGenerator();
        }

        return linePathGenerator;
    }

    public void createLinePaths() {
        createWays();

        createRelations();

    }

    private void createWays() {
        for (Way way : OSMWays) {
            if (way.getOSMType() == OSMType.COASTLINE || way.getOSMType() == null) continue;

            LinePath linePath = createLinePath(way);
            OSMType OSMType = linePath.getOSMType();

            if (!appController.getLinePathsFromModel().containsKey(OSMType)) {
                appController.addToModel(OSMType);
            }

            if (OSMType != OSMType.PLACE) {
                appController.addToModel(OSMType, linePath);
            }
        }
    }

    private void createRelations() {
        for (Relation relation : OSMRelations) {
            if (relation.getOSMType() == OSMType.FOREST) {

                connectWays(relation, OSMType.FOREST);

            } else if (relation.getOSMType() == OSMType.FARMLAND) {

                connectWays(relation, OSMType.FARMLAND);

            } else if (relation.getName() != null && relation.getName().startsWith("Region ")) {

                if (!appController.getLinePathsFromModel().containsKey(OSMType.COASTLINE)) {
                    appController.addToModel(OSMType.COASTLINE);
                }

                connectWays(relation, OSMType.COASTLINE);
            }


        }
        addRelation(OSMType.FOREST, appController.getNodeTo(OSMType.FOREST));
        addRelation(OSMType.FARMLAND, appController.getNodeTo(OSMType.FARMLAND));
        addRelation(OSMType.COASTLINE, appController.getNodeTo(OSMType.COASTLINE));
    }

    private void addRelation(OSMType OSMType, Map<Node, Way> nodeTo) {
        for (Map.Entry<Node, Way> entry : nodeTo.entrySet()) {
            if (entry.getKey() == OSMNodes.get(entry.getValue().getLastNodeId())) {

                appController.addToModel(OSMType, new LinePath(entry.getValue(), OSMType, OSMNodes, addresses, true));
            }
        }
    }

    private void connectWays(Relation relation, OSMType OSMType) {
        Collections.sort(relation.getMembers());
        for (long entry : relation.getMembers()) {
            Way way = (binarySearch(OSMWays, entry));

            if (way == null) continue;

            Way before = removeWayBefore(way, OSMType);
            Way after = removeWayAfter(way, OSMType);

            way = merge(merge(before, way), after);

            appController.addToModel(OSMType, OSMNodes.get(way.getFirstNodeId()), way);
            appController.addToModel(OSMType, OSMNodes.get(way.getLastNodeId()), way);
        }
    }


    private Way removeWayAfter(Way way, OSMType OSMType) {
        Node node = OSMNodes.get(way.getLastNodeId());
        Way after = appController.removeWayFromNodeTo(OSMType, node);
        if (after != null) {
            Node firstNode = OSMNodes.get(after.getFirstNodeId());
            Node lastNode = OSMNodes.get(after.getLastNodeId());
            appController.removeWayFromNodeTo(OSMType, firstNode);
            appController.removeWayFromNodeTo(OSMType, lastNode);

        }
        return after;
    }

    private Way removeWayBefore(Way way, OSMType OSMType) {
        Node node = OSMNodes.get(way.getFirstNodeId());
        Way before = appController.removeWayFromNodeTo(OSMType, node);
        if (before != null) {
            Node firstNode = OSMNodes.get(before.getFirstNodeId());
            Node lastNode = OSMNodes.get(before.getLastNodeId());
            appController.removeWayFromNodeTo(OSMType, firstNode);
            appController.removeWayFromNodeTo(OSMType, lastNode);
        }
        return before;
    }


    private LinePath createLinePath(Way way) {
        OSMType type = OSMType.UNKNOWN;

        try {
            type = way.getOSMType();
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
        Boolean fill = OSMType.getFill(type);

        // TODO: Does every LinePath need all nodes?
        return new LinePath(way, type, OSMNodes, addresses, fill);
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