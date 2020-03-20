package bfst20.presentation;

import bfst20.logic.entities.Node;
import bfst20.logic.entities.Way;
import bfst20.logic.entities.Relation;
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

    private List<Way> OSMWays;
    private Map<Long, Node> nodeMap;
    private List<Relation> tempOSMRelations;
    private List<Relation> OSMRelations;
    private static boolean isLoaded = false;
    private static Parser parser;
    private float minlat, maxlon, maxlat, minlon;

    private Parser() {
        OSMWays = new ArrayList<>();
        nodeMap = new HashMap<>();
        tempOSMRelations = new ArrayList<>();
        OSMRelations = new ArrayList<>();

    }

    public float getMinLat() {
        return minlat;
    }

    public float getMaxLon() {
        return maxlon;
    }

    public float getMaxLat() {
        return maxlat;
    }

    public float getMinLon() {
        return minlon;
    }

    public List<Way> getOSMWays(){
        return OSMWays;
    }

    public Map<Long, Node> getOSMNodes(){
        return nodeMap;
    }

    public List<Relation> getOSMRelations() {
        return OSMRelations;
    }

    public static Parser getInstance() {
        if (isLoaded == false) {
            isLoaded = true;
            parser = new Parser();
        }

        return parser;
    }

    public void parseOSMFile(File file) throws FileNotFoundException, XMLStreamException {

        try {
            parse(XMLInputFactory.newFactory().createXMLStreamReader(new FileReader(file)));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("E is: " + e);
        }

    }

    public void parseString(String string) throws XMLStreamException {
        Reader stringReader = new StringReader(string);
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(stringReader);
        parse(reader);
    }

    private List<Drawable> getDrawables(){
        List<Drawable> drawables = new ArrayList<>();

        return drawables;
    }

    private void parse(XMLStreamReader reader) throws XMLStreamException {

        OSMElement lastElementParsed = null;
        
        while (reader.hasNext()) {
            reader.next();

            switch (reader.getEventType()) {

                case START_ELEMENT:
                    String tagName = reader.getLocalName();

                    switch (tagName) {
                        case "bounds":
                            setBounds(reader);
                            break;
                        case "node":
                            addNodeToMap(reader);
                            break;
                        case "way":
                            lastElementParsed  = addWayToList(reader);
                            break;
                        case "nd":
                            if (lastElementParsed  instanceof Way) {
                                addSubElementToWay(reader, (Way) lastElementParsed );
                            }
                            break;
                        case "relation":
                            lastElementParsed = addRelationToList(reader);
                            break;
                        case "tag":
                            if (lastElementParsed  instanceof Relation) {
                                addTagToRelation(reader);
                            } else if (lastElementParsed  instanceof Way) {
                                addTagToWay(reader, (Way) lastElementParsed );
                            }
                            break;
                        case "member":
                            addMemberToRelation(reader);
                            break;
                    }
                    break;
                case END_ELEMENT:
                    tagName = reader.getLocalName();

                    switch (tagName) {
                        case "relation":
                            Relation relation = (Relation) lastElementParsed;
                            if( relation.getTag("place") != null && relation.getTag("place").equals("island")
                              ||  relation.getTag("type") != null && relation.getTag("type").equals("boundary")){
                                OSMRelations.add(relation);
                            }
                            break;
                    
                        default:
                            break;
                    }
                    break;
            }
        }
    }

    private void setBounds(XMLStreamReader reader){
        minlat = -Float.parseFloat(reader.getAttributeValue(null, "maxlat"));
        maxlon = 0.56f * Float.parseFloat(reader.getAttributeValue(null, "maxlon"));
        maxlat = -Float.parseFloat(reader.getAttributeValue(null, "minlat"));
        minlon = 0.56f * Float.parseFloat(reader.getAttributeValue(null, "minlon"));
    }

    private void addNodeToMap(XMLStreamReader reader) {
        Node node = new Node();
        node.setReader(reader);
        node.setValues();
        nodeMap.put(node.getId(), node);

    }

    private Relation addRelationToList(XMLStreamReader reader) {
        Relation relation = new Relation();
        relation.setReader(reader);
        relation.setValues();

        tempOSMRelations.add(relation);

        return relation;
    }

    private void addTagToRelation(XMLStreamReader reader) {
        Relation relation = tempOSMRelations.get(tempOSMRelations.size() - 1);
        String key = reader.getAttributeValue(null, "k");
        String value = reader.getAttributeValue(null, "v");

        relation.addTag(key, value);

    }

    private void addTagToWay(XMLStreamReader reader, Way way) {
        String key = reader.getAttributeValue(null, "k");
        String value = reader.getAttributeValue(null, "v");
        way.addTag(key, value);

    }

    private void addMemberToRelation(XMLStreamReader reader) {
        Relation relation = tempOSMRelations.get(tempOSMRelations.size() - 1);
        long member = Long.parseLong(reader.getAttributeValue(null, "ref"));
        String type = reader.getAttributeValue(null, "type");
        relation.addMember(member, type);
    }

    private Way addWayToList(XMLStreamReader reader) {
        Way way = new Way();
        way.setReader(reader);
        way.setValues();
        OSMWays.add(way);

        return way;
    }

    private void addSubElementToWay(XMLStreamReader reader, Way lastWay) {
        long id = Long.parseLong(reader.getAttributeValue(null, "ref"));
        lastWay.addNodeId(id);
    }
}
