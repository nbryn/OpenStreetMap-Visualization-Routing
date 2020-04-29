package bfst20.logic.misc;


public interface OSMElement {

    //TODO: Remove setName?
    void setName(String name);

    void setMultipolygon(boolean multipolygon);

    void setOSMType(OSMType OSMType);
}
