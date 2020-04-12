package bfst20.logic;

import bfst20.logic.entities.*;
import bfst20.logic.interfaces.OSMElement;
import bfst20.presentation.LinePath;

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
        if (!isLoaded) {
            isLoaded = true;
            parser = new Parser();
        }
        return parser;
    }

    public void parseBinary(File file) throws FileNotFoundException {
        try (var in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            try {

                Map<Type, List<LinePath>> drawables = (Map<Type, List<LinePath>>) in.readObject();
                Bounds bounds = drawables.get(Type.BOUNDS).get(0).getBounds();

                appController.setBoundsOnModel(bounds);
                appController.setLinePathsOnModel(drawables);
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseOSMFile(File file) throws FileNotFoundException, XMLStreamException {

        try {
            parse(XMLInputFactory.newFactory().createXMLStreamReader(new FileReader(file)));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("E is: " + e);
        }

        tempOSMRelations = null;
        System.gc();

    }

    public void parseString(String string) throws XMLStreamException {
        Reader stringReader = new StringReader(string);
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(stringReader);
        parse(reader);
    }


    private void parse(XMLStreamReader reader) throws XMLStreamException {
        OSMElement lastElementParsed = null;
        HashMap<String, String> tags = null;
        String[] firstTag = new String[2];
        long lastNodeId = 0;

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
                            lastNodeId = Long.parseLong(reader.getAttributeValue(null, "id"));
                            addNodeToMap(reader);
                            tags = new HashMap<>();
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
                            if (tags == null) break;

                            String key = reader.getAttributeValue(null, "k");
                            String value = reader.getAttributeValue(null, "v");

                            if (firstTag[0] == null) {
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
                        case "node":
                            parseTagsAddress(lastNodeId, tags);
                            break;
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

    private void parseTagsAddress(long lastNodeId, HashMap<String, String> tags){
        if(tags.size() == 0) return;

        String city = tags.get("addr:city");
        String housenumber = tags.get("addr:housenumber");
        String postcode = tags.get("addr:postcode");
        String street = tags.get("addr:street");

        if(city == null) return;

        Address address = new Address(city, housenumber, postcode, street);
        appController.putAddressToModel(lastNodeId, address);
    }

    private void parseTags(XMLStreamReader reader,
                           OSMElement lastElementParsed,
                           HashMap<String,
                                   String> tags,
                           String[] firstTag) {

        try {
            if (tags.containsKey("name")) {
                lastElementParsed.setName(tags.get("name"));
            }

            if (tags.containsKey("landuse") || tags.containsKey("natural")) {
                if (tags.containsKey("natural")) {
                    lastElementParsed.setType(Type.valueOf(tags.get("natural").toUpperCase()));
                } else {
                    lastElementParsed.setType(Type.valueOf(tags.get("landuse").toUpperCase()));
                }
            } else if (tags.containsKey("building")) {
                lastElementParsed.setType(Type.BUILDING);
            } else if (tags.containsKey("highway")) {
                Type type = Type.HIGHWAY;

                try{
                    type = Type.valueOf(tags.get("highway").toUpperCase());

                    if(type == Type.RESIDENTIAL){
                        type = Type.RESIDENTIAL_HIGHWAY;
                    }else if(type == Type.UNCLASSIFIED){
                        type = Type.UNCLASSIFIED_HIGHWAY;
                    }
                }catch (Exception e){}

                lastElementParsed.setType(type);
            } else {
                lastElementParsed.setType(Type.valueOf(firstTag[0].toUpperCase()));
            }

        } catch (Exception err) {
        }
    }

    private void setBounds(XMLStreamReader reader) {
        float minLat = -Float.parseFloat(reader.getAttributeValue(null, "maxlat"));
        float maxLon = 0.56f * Float.parseFloat(reader.getAttributeValue(null, "maxlon"));
        float maxLat = -Float.parseFloat(reader.getAttributeValue(null, "minlat"));
        float minLon = 0.56f * Float.parseFloat(reader.getAttributeValue(null, "minlon"));

        appController.setBoundsOnModel(new Bounds(maxLat, minLat, maxLon, minLon));
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
