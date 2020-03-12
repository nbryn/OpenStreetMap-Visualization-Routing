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

    private static XMLStreamReader reader;
    private static List<Way> osmWays;
    private static Map<Long, Node> nodeMap;

    private Parser() {
    }

    public static List<Way> parseOSMFile(File file) throws FileNotFoundException, XMLStreamException {
        List<Way> s = null;
        System.out.println("ParseOSMFILE");
        System.out.println(file);
        try {
            // TODO: Wrong path
            s = parse(XMLInputFactory.newFactory().createXMLStreamReader(new FileReader(file)));
        } catch (Exception e) {
            System.out.println(e);
        }

        return s;
    }

    public static void parseString(String string) throws XMLStreamException {
        Reader stringReader = new StringReader(string);
        XMLInputFactory factory = XMLInputFactory.newInstance();
        reader = factory.createXMLStreamReader(stringReader);
        parse(reader);
    }

    private static List<Way> parse(XMLStreamReader reader) throws XMLStreamException {
        System.out.println("Parse");
        nodeMap = new HashMap<>();
        osmWays = new ArrayList<>();

        while (reader.hasNext()) {
            reader.next();

            switch (reader.getEventType()) {
                case START_ELEMENT:
                    String tagName = reader.getLocalName();

                    if (tagName.equals("node")) {
                        addNodeToMap();
                    } else if (tagName.equals("way")) {
                        addWayToList();
                    } else if (tagName.equals("nd")) {
                        addSubElementToWay();
                    }
                    break;
                case END_ELEMENT:

                    break;
            }

        }

        System.out.println(osmWays);
        return osmWays;
    }

    private static void addNodeToMap() {
        Node node = new Node();
        node.setReader(reader);
        node.setValues();
        nodeMap.put(node.getId(), node);


    }

    private static void addWayToList() {
        Way way = new Way();
        way.setReader(reader);
        way.setValues();
        osmWays.add(way);

    }

    private static void addSubElementToWay() {
        Way way = (Way) osmWays.get(osmWays.size() - 1);
        long id = Long.parseLong(reader.getAttributeValue(null, "ref"));
        way.addNode(nodeMap.get(id));


    }
}