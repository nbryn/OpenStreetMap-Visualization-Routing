package bfst20.presentation;

import bfst20.logic.entities.Node;
import bfst20.logic.entities.Way;
import bfst20.logic.interfaces.OSMElement;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import static javax.xml.stream.XMLStreamConstants.*;

public class Parser {

    private Parser() {
    }

    public static void parseString(String string) throws XMLStreamException {
        Reader stringReader = new StringReader(string);
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(stringReader);

        List<OSMElement> OSMElements = new ArrayList<>();

        while(reader.hasNext()){
            reader.next();

            OSMElement osmElement = null;

            boolean isNested = false;

            switch(reader.getEventType()){
                case START_ELEMENT:
                    isNested = true;
                    String tagName = reader.getLocalName();

                    if(tagName == "node" || tagName == "way"){
                        osmElement = tagName == "node" ? new Node() : new Way();
                        osmElement.setReader(reader);
                        osmElement.setValues();
                        OSMElements.add(osmElement);
                    }else if(tagName == "nd" && isNested && OSMElements.get(OSMElements.size()-1) instanceof Way){
                        Way way = (Way) OSMElements.get(OSMElements.size()-1);
                        way.addId();
                    }


                    break;
                case END_ELEMENT:
                    isNested = false;

                    break;
            }

        }

        System.out.println(OSMElements.size());

        OSMElements.forEach(e -> {
            if(e instanceof Way){
                System.out.println(((Way) e).getNodeIds().size());
            }
        });
    }
}