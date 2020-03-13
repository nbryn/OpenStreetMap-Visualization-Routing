package bfst20.presentation;

import bfst20.logic.entities.Node;
import bfst20.logic.entities.Way;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import static javax.xml.stream.XMLStreamConstants.*;

public class Parser {


    private static List<Way> osmWays;
    private static Map<Long, Node> nodeMap;

    private Parser() {
    }

    public static List<Way> parseOSMFile(File file) throws FileNotFoundException, XMLStreamException {
        List<Way> s = null;

        System.out.println(new File(".").getAbsolutePath());
        try {
            // TODO: Wrong path
            s = parse(XMLInputFactory.newFactory().createXMLStreamReader(new FileReader("/home/nbryn/Desktop/ITU/2. Semester/BFST20Gruppe17/src/main/resources/samsoe.osm")));
        } catch (Exception e) {
            System.out.println("E is: " + e);
        }

        return s;
    }

    public static void parseString(String string) throws XMLStreamException {
        Reader stringReader = new StringReader(string);
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(stringReader);
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
                        addNodeToMap(reader);
                    } else if (tagName.equals("way")) {
                        addWayToList(reader);
                    } else if (tagName.equals("nd")) {
                        addSubElementToWay(reader);
                    }
                    break;
                case END_ELEMENT:

                    break;
            }

        }


        return osmWays;
    }

    private static void addNodeToMap(XMLStreamReader reader) {
        Node node = new Node();
        node.setReader(reader);
        node.setValues();
        nodeMap.put(node.getId(), node);


    }

    private static void addWayToList(XMLStreamReader reader) {
        Way way = new Way();
        way.setReader(reader);
        way.setValues();
        osmWays.add(way);

    }

    private static void addSubElementToWay(XMLStreamReader reader) {
        Way way = osmWays.get(osmWays.size() - 1);
        long id = Long.parseLong(reader.getAttributeValue(null, "ref"));
        way.addNode(nodeMap.get(id));


    }
}



