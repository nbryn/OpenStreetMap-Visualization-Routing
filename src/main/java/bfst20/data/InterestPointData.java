package bfst20.data;

import bfst20.logic.entities.InterestPoint;

import java.util.HashMap;
import java.util.Map;

public class InterestPointData {

    private Map<Integer, InterestPoint> intrestPoints;
    private static InterestPointData interestPointData;

    private InterestPointData(){
        intrestPoints = new HashMap<>();
    }

    public static InterestPointData getInstance(){
        if(interestPointData == null){
            interestPointData = new InterestPointData();
        }

        return interestPointData;
    }

    public void addIntrestPoint(InterestPoint interestPoint){
        intrestPoints.put(intrestPoints.size(), interestPoint);
    }

    public void removeIntrestPoint(int id){
        intrestPoints.remove(id);
    }

    public Map<Integer, InterestPoint> getAllIntrestPoints(){
        return intrestPoints;
    }

    public Iterable<InterestPoint> iterate(){
        return intrestPoints.values();
    }
}
