package bfst20.logic.interfaces;

import javax.xml.stream.XMLStreamReader;

import bfst20.logic.Type;

public interface OSMElement {

    void setValues();

    void setReader(XMLStreamReader reader);

    void setName(String name);
    void setType(Type type);
}
