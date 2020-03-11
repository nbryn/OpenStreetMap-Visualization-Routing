package bfst20.logic.interfaces;

import javax.xml.stream.XMLStreamReader;

import javafx.scene.canvas.GraphicsContext;

public interface OSMElement {

    void setValues();

    void setReader(XMLStreamReader reader);
}
