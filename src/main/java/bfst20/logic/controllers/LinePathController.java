package bfst20.logic.controllers;


import bfst20.data.LinePathData;
import bfst20.logic.entities.LinePath;
import bfst20.logic.misc.OSMType;
import bfst20.logic.services.LinePathService;

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
