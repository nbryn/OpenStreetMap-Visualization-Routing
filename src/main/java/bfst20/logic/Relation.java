package bfst20.logic;

import java.util.ArrayList;

public class Relation {
    private int id, changeset;
    private ArrayList<Tag> tags;

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

    public ArrayList<Tag> getTags(){
        return tags;
    }
}
