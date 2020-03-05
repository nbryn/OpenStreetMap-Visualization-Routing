package bfst20.logic;

import java.util.ArrayList;

public class Node {
    private int id;
    private double let;
    private double lon;
    private int changeset;
    private ArrayList<Tag> tags;

    public Node(int id, double let, double lon, int changeset){
        this.id = id;
        this.let = let;
        this.lon = lon;
        this.changeset = changeset;
    }

    public int getId() {
        return id;
    }

    public double getLet(){
        return let;
    }

    public double getLon(){
        return lon;
    }

    public int getChangeset(){
        return changeset;
    }

    public ArrayList<Tag> getTags(){
        return tags;
    }

}
