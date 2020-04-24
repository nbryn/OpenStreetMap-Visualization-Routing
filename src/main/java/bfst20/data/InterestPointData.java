package bfst20.data;

import bfst20.logic.entities.InterestPoint;

import java.util.LinkedList;
import java.util.List;

public class InterestPointData {

    private List<InterestPoint> intrestPoints;
    private static InterestPointData interestPointData;

    private InterestPointData(){
        intrestPoints = new LinkedList<>();
    }

    public static InterestPointData getInstance(){
        if(interestPointData == null){
            interestPointData = new InterestPointData();
        }

        return interestPointData;
    }

    public void addIntrestPoint(InterestPoint interestPoint){
        intrestPoints.add(interestPoint);
    }

    public void removeIntrestPoint(int key){
        intrestPoints.remove(key);
    }

    public List<InterestPoint> getAllIntrestPoints(){
        return intrestPoints;
    }

}
