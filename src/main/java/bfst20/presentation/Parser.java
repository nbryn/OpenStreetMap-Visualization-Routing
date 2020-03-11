package bfst20.presentation;

import bfst20.logic.entities.Node;
import bfst20.logic.entities.Way;
import bfst20.logic.interfaces.Drawable;
import bfst20.logic.interfaces.OSMElement;

import java.io.*;
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

    public static List<Drawable> parseOSMFile(File file) throws FileNotFoundException, XMLStreamException {
        return parse(XMLInputFactory.newFactory().createXMLStreamReader(new FileReader(file)));
    }

    public static void parseString(String string) throws XMLStreamException {
        Reader stringReader = new StringReader(string);
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(stringReader);
        parse(reader);
    }

    private static List<Drawable> parse(XMLStreamReader reader) throws XMLStreamException {
        Map<Long, OSMElement> nodeMap = new HashMap<>();


        List<Drawable> OSMElements = new ArrayList<>();

        while(reader.hasNext()){
            reader.next();

            OSMElement osmElement = null;

            boolean isNested = false;

            switch(reader.getEventType()){
                case START_ELEMENT:
                    isNested = true;
                    String tagName = reader.getLocalName();

                    if(tagName == "node"){
                        osmElement = new Node();
                        osmElement.setReader(reader);
                        osmElement.setValues();
                        nodeMap.put(((Node) osmElement).getId(), osmElement);
                    }else if(tagName == "way"){
                        Way way = new Way();
                        way.setReader(reader);
                        way.setValues();
                        OSMElements.add(way);
       
                    }else if(tagName == "nd" && isNested && OSMElements.get(OSMElements.size()-1) instanceof Way){
                        Way way = (Way) OSMElements.get(OSMElements.size()-1);
                        long id = Long.parseLong(reader.getAttributeValue(null, "ref"));
                        way.addNode(nodeMap.get(id));
                    }


                    break;
                case END_ELEMENT:
                    isNested = false;

                    break;
            }

        }

        return OSMElements;
    }
}