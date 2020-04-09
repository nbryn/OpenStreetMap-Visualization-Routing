package bfst20.presentation;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import bfst20.logic.entities.Node;
import bfst20.logic.entities.Way;
import bfst20.logic.interfaces.Drawable;
import javafx.scene.canvas.GraphicsContext;
import bfst20.logic.Type;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class LinePath implements Drawable {
    float[] coords;
    Type type;
    Color color;
    Boolean fill;
    float minY,minX,maxY,maxX, centerLatitude, centerLongitude;

    public LinePath(Way way, Type type, Map<Long, Node> OSMNodes, Color color, Boolean fill) {
        this.color = color;
        this.fill = fill;
        List<Long> nodeIds = way.getNodeIds();

        minY = Float.POSITIVE_INFINITY;
        minX = Float.POSITIVE_INFINITY;
        maxY = Float.NEGATIVE_INFINITY;
        maxX = Float.NEGATIVE_INFINITY;

        coords = new float[nodeIds.size() * 2];
        for (int i = 0; i < nodeIds.size(); ++i) {
            coords[i * 2] = OSMNodes.get(nodeIds.get(i)).getLongitude();
            coords[i * 2 + 1] = OSMNodes.get(nodeIds.get(i)).getLatitude();

            if(minX > coords[i*2+1]) minX = coords[i*2+1];
            if(minY > coords[i*2]) minY = coords[i*2];
            if(maxX < coords[i*2+1]) maxX = coords[i*2+1];
            if(maxY < coords[i*2]) maxY = coords[i*2];

        }

        centerLatitude = (maxX - minX)/2 + minX;
        centerLongitude = (maxY - minY)/2 + minY;

        this.type = type;
    }


    public float getCenterLatitude() {
        return centerLatitude;
    }

    public float getCenterLongitude() {
        return centerLongitude;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMaxY() {
        return maxY;
    }

    public float getMinX() {
        return minX;
    }

    public float getMinY() {
        return minY;
    }

    public float[] getCoords(){
        return coords;
    }

    @Override
    public void draw(GraphicsContext gc) {

        gc.beginPath();
        gc.setStroke(color);
        gc.setFill(fill ? color : Color.TRANSPARENT);

        /*if(way.getTagValue("name") != null){
            gc.setFill(Color.BLACK);
            gc.setFont(new Font(0.00022));
            gc.fillText(way.getTagValue("name"), coords[0], coords[1]);
            gc.setFill(fill ? color : Color.TRANSPARENT);
        }*/
        //gc.setStroke(Color.BLUE);
        //gc.strokeRect(minY, minX, maxY-minY, maxX-minX);
      //  gc.setStroke(color);



        trace(gc);
        gc.stroke();
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setFill(boolean fill) {
        this.fill = fill;
    }


    @Override
    public Type getType() {
        return type;
    }


    private void trace(GraphicsContext gc) {
        gc.moveTo(coords[0], coords[1]);
        for (int i = 2; i <= coords.length; i += 2) {
            gc.lineTo(coords[i-2], coords[i - 1]);
        }
    }
}