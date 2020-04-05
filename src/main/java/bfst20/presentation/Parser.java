package bfst20.presentation;

import bfst20.logic.AppController;
import bfst20.logic.Type;
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

    private static boolean isLoaded = false;
    private static Parser parser;
    private List<Relation> tempOSMRelations;
    private AppController appController;

    private Parser() {
        appController = new AppController();
        tempOSMRelations = new ArrayList<>();

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

    private List<Drawable> getDrawables() {
        List<Drawable> drawables = new ArrayList<>();

        tempOSMRelations = null;
        System.gc();

        return drawables;
    }

    private void parse(XMLStreamReader reader) throws XMLStreamException {

        OSMElement lastElementParsed = null;
        HashMap<String, String> tags = null;
        String[] firstTag = new String[2];

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
                            lastElementParsed = addWayToList(reader);
                            tags = new HashMap<>();
                            break;
                        case "nd":
                            if (lastElementParsed instanceof Way) {
                                addSubElementToWay(reader, (Way) lastElementParsed);
                            }
                            break;
                        case "relation":
                            lastElementParsed = addRelationToList(reader);
                            tags = new HashMap<>();
                            break;
                        case "tag":
                            if(tags == null) break;

                            String key = reader.getAttributeValue(null, "k");
                            String value = reader.getAttributeValue(null, "v");

                            if(firstTag[0] == null){
                                firstTag[0] = key;
                                firstTag[1] = value;
                            }

                            tags.put(key, value);
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
                            appController.addRelationToModel(relation);
                            parseTags(reader, lastElementParsed, tags, firstTag);
                            break;
                        case "way":
                            parseTags(reader, lastElementParsed, tags, firstTag);
                            break;
                        default:
                            break;
                    }
                    break;
            }
        }
    }

    private void parseTags( XMLStreamReader reader, 
                            OSMElement lastElementParsed, 
                            HashMap<String, 
                            String> tags, 
                            String[] firstTag){


        try {
            if(tags.containsKey("name")){
                lastElementParsed.setName(tags.get("name"));
            }

            if (tags.containsKey("landuse") || tags.containsKey("natural")) {
                if (tags.containsKey("natural")) {
                    lastElementParsed.setType(Type.valueOf(tags.get("natural").toUpperCase()));
                } else {
                    lastElementParsed.setType(Type.valueOf(tags.get("landuse").toUpperCase()));
                }
            } else if(tags.containsKey("building")) {
                lastElementParsed.setType(Type.BUILDING);
            }else if(tags.containsKey("highway")) {
                lastElementParsed.setType(Type.HIGHWAY);
            }else{
                lastElementParsed.setType(Type.valueOf(firstTag[0].toUpperCase()));
            }

        } catch (Exception err) {
        }
    }

    private void setBounds(XMLStreamReader reader) {
        float minlat = -Float.parseFloat(reader.getAttributeValue(null, "maxlat"));
        float maxlon = 0.56f * Float.parseFloat(reader.getAttributeValue(null, "maxlon"));
        float maxlat = -Float.parseFloat(reader.getAttributeValue(null, "minlat"));
        float minlon = 0.56f * Float.parseFloat(reader.getAttributeValue(null, "minlon"));

        appController.setBoundsOnModel(minlat, maxlon, maxlat, minlon);
    }

    private void addNodeToMap(XMLStreamReader reader) {
        Node node = new Node();
        node.setReader(reader);
        node.setValues();
        appController.addNodeToModel(node.getId(), node);

    }

    //1. Adding relation to temp
    //2. Adding sub elements to the temp relation
    //3. Adding final relation to the real realtions list.
    //Why: to have all sub elements in the final relations.
    private Relation addRelationToList(XMLStreamReader reader) {
        Relation relation = new Relation();
        relation.setReader(reader);
        relation.setValues();

        tempOSMRelations.add(relation);

        return relation;
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
        appController.addWayToModel(way);

        return way;
    }

    private void addSubElementToWay(XMLStreamReader reader, Way lastWay) {
        long id = Long.parseLong(reader.getAttributeValue(null, "ref"));
        lastWay.addNodeId(id);
    }
}
