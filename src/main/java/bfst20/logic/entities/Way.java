package bfst20.logic.entities;

import bfst20.logic.interfaces.OSMElement;

import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Way implements OSMElement {
    private long id;
    private List<OSMElement> subElements;
    private List<Long> nodeIds;
    private XMLStreamReader reader;

    public Way(){
        subElements = new ArrayList<>();
        nodeIds = new ArrayList<>();
    }

    public long getId(){
        return id;
    }

    public List<Long> getNodeIds(){
        return nodeIds;
    }

    public void addId(){
        nodeIds.add(Long.parseLong(reader.getAttributeValue(null, "ref")));
    }

    @Override
    public void setValues() {
        id = Long.parseLong(reader.getAttributeValue(null, "id"));
    }

    @Override
    public void addSubElements(OSMElement osmElement) {
        subElements.add(osmElement);
    }

    @Override
    public void setReader(XMLStreamReader reader) {
        this.reader = reader;
    }
}
