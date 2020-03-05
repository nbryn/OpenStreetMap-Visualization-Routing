package bfst20.logic.interfaces;

import javax.xml.stream.XMLStreamReader;

public interface OSMElement {

    void setValues();

    void addSubElements(OSMElement subElement);

    void setReader(XMLStreamReader reader);
}
