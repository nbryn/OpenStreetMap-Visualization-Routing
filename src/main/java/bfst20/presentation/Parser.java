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

    public static List<Way> parseOSMFile(File file) throws FileNotFoundException, XMLStreamException {
        return parse(XMLInputFactory.newFactory().createXMLStreamReader(new FileReader(file)));
    }

    public static void parseString(String string) throws XMLStreamException {
        Reader stringReader = new StringReader(string);
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(stringReader);
        parse(reader);
    }

    private static List<Way> parse(XMLStreamReader reader) throws XMLStreamException {
        Map<Long, Node> nodeMap = new HashMap<>();

        List<Way> OSMways = new ArrayList<>();

        while(reader.hasNext()){
            reader.next();

            switch(reader.getEventType()){
                case START_ELEMENT:
                    String tagName = reader.getLocalName();

                    if(tagName.equals("node")){
                        Node node = new Node();
                        node.setReader(reader);
                        node.setValues();
                        nodeMap.put(node.getId(), node);
                    }else if(tagName.equals("way")){
                        Way way = new Way();
                        way.setReader(reader);
                        way.setValues();
                        OSMways.add(way);
                    }else if(tagName.equals("nd")){
                        Way way = (Way) OSMways.get(OSMways.size()-1);
                        long id = Long.parseLong(reader.getAttributeValue(null, "ref"));
                        way.addNode(nodeMap.get(id));
                    }
                    break;
                case END_ELEMENT:

                    break;
            }

        }

        return OSMways;
    }
}