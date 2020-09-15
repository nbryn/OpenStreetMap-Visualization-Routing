package bfst20.logic.controllers.interfaces;

import bfst20.logic.entities.LinePath;
import bfst20.logic.entities.Node;
import bfst20.logic.entities.Relation;
import bfst20.logic.entities.Way;
import bfst20.logic.misc.OSMType;

import java.util.List;
import java.util.Map;

public interface LinePathAPI {

    void init(List<Way> ways, Map<Long, Node> nodes, List<Relation> relations);

    List<LinePath> fetchCoastlines();

    void saveCoastlines(List<LinePath> paths);

    Map<OSMType, List<LinePath>> fetchLinePathData();

    List<LinePath> fetchMotorways();

    void clearData();


}
