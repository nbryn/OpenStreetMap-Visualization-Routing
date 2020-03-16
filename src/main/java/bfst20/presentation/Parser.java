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


    private List<Way> OSMWays;
    private Map<Long, Node> nodeMap;
    private List<Relation> OSMRelations;
    private static boolean isLoaded = false;
    private static Parser parser;
    private float minlat, maxlon, maxlat, minlon;

    private Parser() {
        OSMWays = new ArrayList<>();
        nodeMap = new HashMap<>();
        OSMRelations = new ArrayList<>();
    }

    public List<Way> getOSMWays (){
        return OSMWays;
    }

    public float getMinLat(){return minlat;}
    public float getMaxLon(){return maxlon;}
    public float getMaxLat(){return maxlat;}
    public float getMinLon(){return minlon;}

    public List<Relation> getOSMRelations (){
        return OSMRelations;
    }

    public List<Relation> getIslandRelations (){
        List<Relation> islands = new ArrayList<>();

        for(Relation relation : OSMRelations){
            String place = relation.getTag("place");
            if(place == null) continue;
            if(place.equals("island")){
                islands.add(relation);
            }
        }

        return islands;
    }

    public static Parser getInstance(){
        if(isLoaded == false){
            isLoaded = true;
            parser = new Parser();
        }

        return parser;
    }

    public List<Way> parseOSMFile(File file) throws FileNotFoundException, XMLStreamException {
        List<Way> s = null;

        try {

            s = parse(XMLInputFactory.newFactory().createXMLStreamReader(new FileReader(file)));
        } catch (Exception e) {
            System.out.println("E is: " + e);
        }

        return s;
    }

    public void parseString(String string) throws XMLStreamException {
        Reader stringReader = new StringReader(string);
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(stringReader);
        parse(reader);
    }

    private List<Way> parse(XMLStreamReader reader) throws XMLStreamException {

        while (reader.hasNext()) {
            OSMElement lastElement = null;

            while (reader.hasNext()) {
                reader.next();

                switch (reader.getEventType()) {
                    case START_ELEMENT:
                        String tagName = reader.getLocalName();

                        switch (tagName) {
                            case "bounds":
                                minlat = -Float.parseFloat(reader.getAttributeValue(null, "maxlat"));
                                maxlon = 0.56f * Float.parseFloat(reader.getAttributeValue(null, "maxlon"));
                                maxlat = -Float.parseFloat(reader.getAttributeValue(null, "minlat"));
                                minlon = 0.56f * Float.parseFloat(reader.getAttributeValue(null, "minlon"));
                                break;
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

    private void addNodeToMap(XMLStreamReader reader) {
        Node node = new Node();
        node.setReader(reader);
        node.setValues();
        nodeMap.put(node.getId(), node);

    }

    private Relation addRelationToList(XMLStreamReader reader) {
        Relation relation = new Relation();
        OSMRelations.add(relation);

        return relation;
    }

    private void addTagToRelation(XMLStreamReader reader) {
        Relation relation = OSMRelations.get(OSMRelations.size() - 1);
        String key = reader.getAttributeValue(null, "k");
        String value = reader.getAttributeValue(null, "v");
        relation.addTag(key, value);

    }

    private void addMemberToRelation(XMLStreamReader reader) {
        Relation relation = OSMRelations.get(OSMRelations.size() - 1);
        long member = Long.parseLong(reader.getAttributeValue(null, "ref"));
        relation.addMember(member);

    }

    private void addWayToList(XMLStreamReader reader) {
        Way way = new Way();
        way.setReader(reader);
        way.setValues();
        OSMWays.add(way);

    }

    private void addSubElementToWay(XMLStreamReader reader) {
        Way way = OSMWays.get(OSMWays.size() - 1);
        long id = Long.parseLong(reader.getAttributeValue(null, "ref"));
        way.addNode(nodeMap.get(id));


    }
}





