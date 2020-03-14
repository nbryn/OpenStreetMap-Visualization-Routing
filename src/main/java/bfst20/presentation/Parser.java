package bfst20.presentation;

import bfst20.logic.entities.Node;
import bfst20.logic.entities.Way;
import bfst20.logic.entities.Relation;
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


    private static List<Way> OSMWays;
    private static Map<Long, Node> nodeMap;
    private static List<Relation> OSMRelations;


    private Parser() {

    }

    public static List<Way> parseOSMFile(File file) throws FileNotFoundException, XMLStreamException {
        List<Way> s = null;

        try {

            s = parse(XMLInputFactory.newFactory().createXMLStreamReader(new FileReader(file)));
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
        nodeMap = new HashMap<>();
        OSMWays = new ArrayList<>();
        OSMRelations = new ArrayList<>();

        while (reader.hasNext()) {
            OSMElement lastElement = null;

            while (reader.hasNext()) {
                reader.next();

                switch (reader.getEventType()) {
                    case START_ELEMENT:
                        String tagName = reader.getLocalName();

                        switch (tagName) {
                            case "node":
                                addNodeToMap(reader);
                                break;
                            case "way":
                                addWayToList(reader);
                                break;
                            case "nd":
                                addSubElementToWay(reader);
                                break;
                            case "relation":
                                lastElement = addRelationToList(reader);
                                break;
                            case "tag":
                                if (lastElement instanceof Relation) {
                                     addTagToRelation(reader);
                                }
                                break;
                            case "member":
                                addMemberToRelation(reader);
                                break;
                        }
                        break;
                    case END_ELEMENT:

                        break;
                }
            }
        }
        return OSMWays;
    }

    private static void addNodeToMap(XMLStreamReader reader) {
        Node node = new Node();
        node.setReader(reader);
        node.setValues();
        nodeMap.put(node.getId(), node);

    }

    private static Relation addRelationToList(XMLStreamReader reader) {
        Relation relation = new Relation();
        OSMRelations.add(relation);

        return relation;
    }

    private static void addTagToRelation(XMLStreamReader reader) {
        Relation relation = OSMRelations.get(OSMRelations.size() - 1);
        String key = reader.getAttributeValue(null, "k");
        String value = reader.getAttributeValue(null, "v");
        relation.addTag(key, value);

    }

    private static void addMemberToRelation(XMLStreamReader reader) {
        Relation relation = OSMRelations.get(OSMRelations.size() - 1);
        long member = Long.parseLong(reader.getAttributeValue(null, "ref"));
        relation.addMember(member);

    }

    private static void addWayToList(XMLStreamReader reader) {
        Way way = new Way();
        way.setReader(reader);
        way.setValues();
        OSMWays.add(way);

    }

    private static void addSubElementToWay(XMLStreamReader reader) {
        Way way = OSMWays.get(OSMWays.size() - 1);
        long id = Long.parseLong(reader.getAttributeValue(null, "ref"));
        way.addNode(nodeMap.get(id));


    }
}





