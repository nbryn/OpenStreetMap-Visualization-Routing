package bfst20.logic.entities;

import bfst20.logic.interfaces.OSMElement;
import javafx.scene.canvas.GraphicsContext;

import javax.xml.stream.XMLStreamReader;

public class Tag implements OSMElement {
    private String key, value;
    private XMLStreamReader reader;

    public Tag() {
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void setValues() {
        setKey();
        setValue();
    }

    private void setKey(){
        key = String.valueOf(reader.getAttributeValue(null, "k"));

    }

    private void setValue(){
        value = String.valueOf(reader.getAttributeValue(null, "v"));

    }

    @Override
    public void setReader(XMLStreamReader reader) {
        this.reader = reader;
    }
}
