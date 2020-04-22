package bfst20.data;

import bfst20.logic.entities.InterestPoint;

import java.util.HashMap;
import java.util.Map;

public class InterestPointData {
    int amount = 0;

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
        intrestPoints.put(amount++, interestPoint);
    }

    public void removeIntrestPoint(int key){
        intrestPoints.remove(key);
    }

    public Map<Integer, InterestPoint> getAllIntrestPoints(){
        return intrestPoints;
    }

    public Iterable<InterestPoint> iterate(){
        return intrestPoints.values();
    }
}
