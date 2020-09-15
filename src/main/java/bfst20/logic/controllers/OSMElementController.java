package bfst20.logic.controllers;

import bfst20.data.OSMElementData;
import bfst20.logic.controllers.interfaces.OSMElementAPI;
import bfst20.logic.entities.Bounds;
import bfst20.logic.entities.Node;
import bfst20.logic.entities.Relation;
import bfst20.logic.entities.Way;

import java.util.List;
import java.util.Map;

public class OSMElementController implements OSMElementAPI {
    private OSMElementData osmElementData;

    public OSMElementController(OSMElementData osmElementData) {
        this.osmElementData = osmElementData;
    }

    @Override
    public Bounds fetchBoundsData() {
        return osmElementData.getBounds();
    }

    @Override
    public void saveRelationData(Relation relation) {
        osmElementData.saveRelation(relation);
    }

    @Override
    public List<Relation> fetchAllRelations() {
        return osmElementData.getRelations();
    }

    @Override
    public void saveBoundsData(Bounds bounds) {
        osmElementData.saveBounds(bounds);
    }

    @Override
    public void saveNodeData(long id, Node node) {
        osmElementData.addToNodeMap(id, node);
    }

    @Override
    public void saveWayData(Way way) {
        osmElementData.saveWay(way);
    }

    @Override
    public List<Way> fetchAllWays() {
        return osmElementData.getWays();
    }

    @Override
    public Node fetchNodeById(long id) {
        return osmElementData.getNode(id);
    }

    @Override
    public Map<Long, Node> fetchAllNodes() {
        return osmElementData.getNodes();
    }

    @Override
    public void clearNodeData() {
        osmElementData.clearNodeData();
    }
}
