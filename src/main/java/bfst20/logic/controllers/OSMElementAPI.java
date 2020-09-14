package bfst20.logic.controllers;

import bfst20.logic.entities.Bounds;
import bfst20.logic.entities.Node;
import bfst20.logic.entities.Relation;
import bfst20.logic.entities.Way;

public interface OSMElementAPI {

    Bounds fetchBoundsData();

    void saveRelationData(Relation relation);

    void saveBoundsData(Bounds bounds);

    void saveNodeData(long id, Node node);

    void saveWayData(Way way);

    Node fetchNodeById(long id);
}
