package bfst20.logic.controllers;

import bfst20.data.OSMElementData;
import bfst20.logic.entities.Bounds;

public class OSMElementController implements OSMElementAPI {
    private OSMElementData osmElementData;

    public OSMElementController(OSMElementData osmElementData) {
        this.osmElementData = osmElementData;
    }

    @Override
    public Bounds fetchBoundsData() {
        return osmElementData.getBounds();
    }
}
