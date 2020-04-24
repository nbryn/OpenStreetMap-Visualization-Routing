package bfst20.logic.entities;

import bfst20.logic.misc.OSMType;
import bfst20.logic.misc.OSMElement;

import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;

//TODO: FIX
public class Relation implements OSMElement {

    private long id;
    private ArrayList<Long> members;
    private String name;
    private OSMType OSMType;
    private boolean multipolygon;

    public Relation(long id){
        this.id = id;
        members = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setMultipolygon(boolean multipolygon) {
        this.multipolygon = multipolygon;
    }

    public boolean isMultipolygon(){
        return multipolygon;
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

}
