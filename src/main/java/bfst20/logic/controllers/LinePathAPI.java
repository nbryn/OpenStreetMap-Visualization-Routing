package bfst20.logic.controllers;

import bfst20.logic.entities.LinePath;
import bfst20.logic.misc.OSMType;

import java.util.List;
import java.util.Map;

public interface LinePathAPI {

    List<LinePath> fetchCoastlines();

    void saveCoastlines(List<LinePath> paths);

    Map<OSMType, List<LinePath>> fetchLinePathData();

    List<LinePath> fetchMotorways();

    void clearData();


}
