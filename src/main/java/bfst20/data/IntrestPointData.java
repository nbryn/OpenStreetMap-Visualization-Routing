package bfst20.data;

import bfst20.logic.entities.Address;
import bfst20.logic.entities.IntrestPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntrestPointData {

    private Map<Integer, IntrestPoint> intrestPoints;
    private static IntrestPointData intrestPointData;

    private IntrestPointData(){
        intrestPoints = new HashMap<>();
    }

    public static IntrestPointData getInstance(){
        if(intrestPointData == null){
            intrestPointData = new IntrestPointData();
        }

        return intrestPointData;
    }

    public void addIntrestPoint(IntrestPoint intrestPoint){
        intrestPoints.put(intrestPoints.size(), intrestPoint);
    }

    public void removeIntrestPoint(int id){
        intrestPoints.remove(id);
    }

    public Map<Integer, IntrestPoint> getAllIntrestPoints(){
        return intrestPoints;
    }

    public Iterable<IntrestPoint> iterate(){
        return intrestPoints.values();
    }
}
