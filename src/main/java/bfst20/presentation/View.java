package bfst20.presentation;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

import java.util.List;

import bfst20.logic.drawables.DrawableFactory;
import bfst20.logic.drawables.Type;
import bfst20.logic.entities.Relation;
import bfst20.logic.entities.Way;
import bfst20.logic.interfaces.Drawable;


import java.awt.*;

public class View {

    Affine trans = new Affine();
    List<Way> data;
    List<Relation> islandRelations;
    Canvas canvas;
    GraphicsContext gc;

    public View(Canvas canvas) {
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void update() {

    }

    public void initializeData() {
        Parser parser = Parser.getInstance();
        float minlon = parser.getMinLon();
        float maxlon = parser.getMaxLon();
        float minlat = parser.getMinLat();
        float maxlat = parser.getMaxLat();

        pan(-minlon, -minlat);
        zoom(canvas.getHeight() / (maxlon- minlon), (minlat- maxlat)/2, 0);


        repaint();
    }

    public void repaint() {

        gc.setTransform(new Affine());
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setTransform(trans);
        
        double pixelwidth = 1 / Math.sqrt(Math.abs(trans.determinant()));
        gc.setLineWidth(pixelwidth);

        for (Drawable element : DrawableFactory.createDrawables().get(Type.HIGHWAY)){
            element.draw(gc);
        }
    }

    public void drawWay(){

    }

    public void zoom(double factor, double x, double y) {
        trans.prependScale(factor, factor, x, y);
        repaint();
    }

    public void pan(double dx, double dy) {
        trans.prependTranslation(dx, dy);
        repaint();
	}
}