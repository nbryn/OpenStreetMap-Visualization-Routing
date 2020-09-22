package bfst20.logic.entities;

import bfst20.logic.misc.OSMType;

public interface OSMElement {
    void setName(String name);

    void setMultipolygon(boolean multipolygon);

    void setOSMType(OSMType OSMType);
}
