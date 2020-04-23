package bfst20.logic.misc;

import javax.xml.stream.XMLStreamReader;

public interface OSMElement {

    void setValues();

    void setReader(XMLStreamReader reader);

    void setName(String name);
    void setOSMType(OSMType OSMType);
}
