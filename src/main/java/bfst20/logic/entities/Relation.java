package bfst20.logic.entities;

import bfst20.logic.Type;
import bfst20.logic.interfaces.OSMElement;

import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
//TODO: FIX
public class Relation implements OSMElement {

    private long id;
    private XMLStreamReader reader;
    private ArrayList<Long> members;

    private String name;
    private Type type;

    public Relation(){
        members = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(Type type){
        this.type = type;
    }

    public Type getType(){
        return type;
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
