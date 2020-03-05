package bfst20.presentation;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;

public class MapCanvas extends Canvas {
    private int dimension;

    public MapCanvas(Dimension dimension){
        setWidth(dimension.getWidth());
        setHeight(dimension.getHeight());

        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0, 0, dimension.getWidth(), dimension.getHeight());
    }
}
