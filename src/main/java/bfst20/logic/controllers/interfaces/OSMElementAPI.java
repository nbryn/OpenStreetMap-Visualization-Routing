package bfst20.logic.controllers.interfaces;

import bfst20.logic.entities.Bounds;
import bfst20.logic.entities.Node;
import bfst20.logic.entities.Relation;
import bfst20.logic.entities.Way;

import java.util.List;
import java.util.Map;

public interface OSMElementAPI {

    Bounds fetchBoundsData();

    void saveRelationData(Relation relation);

    List<Relation> fetchAllRelations();

    void saveBoundsData(Bounds bounds);

    void saveNodeData(long id, Node node);

    void saveWayData(Way way);

    List<Way> fetchAllWays();

    Node fetchNodeById(long id);

    Map<Long, Node> fetchAllNodes();

    void clearNodeData();


}
