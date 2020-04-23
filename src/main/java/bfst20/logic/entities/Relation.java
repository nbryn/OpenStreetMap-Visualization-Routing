package bfst20.logic.entities;

import bfst20.logic.misc.OSMType;
import bfst20.logic.misc.OSMElement;

import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;

//TODO: FIX
public class Relation implements OSMElement {

    private long id;
    private XMLStreamReader reader;
    private ArrayList<Long> members;

    private String name;
    private OSMType OSMType;

    public Relation(){
        members = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOSMType(OSMType OSMType){
        this.OSMType = OSMType;
    }

    public OSMType getOSMType(){
        return OSMType;
    }

    public String getName(){
        return name;
    }


    public void addMember(long member, String type){
        if(!type.equals("way")) return;
        members.add(member);
    }

    public ArrayList<Long> getMembers(){
        return members;
    }

    public long getId() {
        return id;
    }

    @Override
    public void setReader(XMLStreamReader reader) {
        this.reader = reader;
    }

    @Override
    public void setValues() {
        id = Long.parseLong(reader.getAttributeValue(null, "id"));
    }

}
