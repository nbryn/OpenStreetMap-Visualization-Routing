package bfst20.logic.entities;

import bfst20.logic.misc.OSMType;

import java.util.ArrayList;
import java.util.List;

public class Relation implements OSMElement {
    private List<Long> members;
    private boolean multipolygon;
    private OSMType OSMType;
    private String name;
    private long id;

    public Relation(long id) {
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

    public boolean isMultipolygon() {
        return multipolygon;
    }

    public void setOSMType(OSMType type) {
        this.OSMType = type;
    }

    public OSMType getOSMType() {
        return OSMType;
    }

    public String getName() {
        return name;
    }

    public void addMember(long member, String type) {
        if (!type.equals("way")) return;
        members.add(member);
    }

    public List<Long> getMembers() {
        return members;
    }

    public long getId() {
        return id;
    }
}
