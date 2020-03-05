package bfst20.logic.entities;

import bfst20.logic.interfaces.OSMElement;

import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;
//TODO: FIX
public class Relation {
    private int id, changeset;
    private List<Tag> tags;

    public Relation(int id, int changeset){
        this.id = id;
        this.changeset = changeset;
    }

    public int getId() {
        return id;
    }

    public int getChangeset(){
        return changeset;
    }

    public List<Tag> getTags() {
        return tags;
    }
}
