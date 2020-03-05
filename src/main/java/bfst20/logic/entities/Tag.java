package bfst20.logic.entities;

import bfst20.logic.interfaces.OSMElement;

import javax.xml.stream.XMLStreamReader;

public class Tag implements OSMElement{
    private String key, value;
    private XMLStreamReader reader;

    public Tag(){}

    public String getKey(){
        return key;
    }

    public String getValue(){ return value; }

    @Override
    public void setValues() {
        key = String.valueOf(reader.getAttributeValue(null, "k"));
        value = String.valueOf(reader.getAttributeValue(null, "v"));
    }

    @Override
    public void addSubElements(OSMElement subElement) {

    }

    @Override
    public void setReader(XMLStreamReader reader) {
        this.reader = reader;
    }
}
