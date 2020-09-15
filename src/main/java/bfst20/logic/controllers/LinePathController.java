package bfst20.logic.controllers;


import bfst20.data.LinePathData;
import bfst20.logic.controllers.interfaces.LinePathAPI;
import bfst20.logic.entities.LinePath;
import bfst20.logic.entities.Node;
import bfst20.logic.entities.Relation;
import bfst20.logic.entities.Way;
import bfst20.logic.misc.OSMType;
import bfst20.logic.services.LinePathService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LinePathController implements LinePathAPI {
    private LinePathData linePathData;
    private LinePathService linePathService;


    public LinePathController(LinePathData linePathData, LinePathService linePathService) {
        this.linePathData = linePathData;
        this.linePathService = linePathService;
    }


    @Override
    public void init(List<Way> ways, Map<Long, Node> nodes, List<Relation> relations) {
        linePathService.convertWaysToLinePaths(ways, nodes);
        linePathService.convertRelationsToLinePaths(relations);
        linePathService.clearData();

        Map<OSMType, List<LinePath>> linePaths = linePathData.getLinePaths();
        List<LinePath> highWays = new ArrayList<>();

        if (linePathData.getMotorways() != null) highWays.addAll(linePathData.getMotorways());
        for (Map.Entry<OSMType, List<LinePath>> entry : linePaths.entrySet()) {
            highWays.addAll(entry.getValue());
        }

        linePathData.saveHighways(highWays);

    }

    @Override
    public List<LinePath> fetchCoastlines() {
       return linePathData.getMotorways();
    }

    @Override
    public void saveCoastlines(List<LinePath> paths) {
        linePathData.saveCoastlines(paths);
    }

    @Override
    public Map<OSMType, List<LinePath>> fetchLinePathData() {
        return linePathData.getLinePaths();
    }

    @Override
    public List<LinePath> fetchMotorways() {
        return linePathData.getMotorways();
    }

    @Override
    public void clearData() {
        linePathService.clearData();
        linePathData.clearData();
    }
}
