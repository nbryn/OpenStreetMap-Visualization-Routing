package bfst20.logic.misc;

import javax.xml.stream.XMLStreamReader;

public interface OSMElement {


    void setName(String name);
    void setMultipolygon(boolean multipolygon);
    void setOSMType(OSMType OSMType);
}
