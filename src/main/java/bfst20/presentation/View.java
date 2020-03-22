package bfst20.presentation;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

import java.util.List;
import java.util.Map;

import bfst20.data.Model;
import bfst20.logic.DrawableGenerator;
import bfst20.logic.Type;
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
    Map<Type, List<Drawable>> drawables;

    public View(Canvas canvas) {
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void update() {

    }

    public void initializeData() {
        Model model = Model.getInstance();
        float minlon = model.getMinLon();
        float maxlon = model.getMaxLon();
        float minlat = model.getMinLat();
        float maxlat = model.getMaxLat();

        DrawableGenerator drawableGenerator = DrawableGenerator.getInstance();

        drawables = drawableGenerator.createDrawables();

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

        

        for (Drawable element : drawables.get(Type.COASTLINE)) {
            element.draw(gc);
            gc.fill();
        }
        for (Drawable element : drawables.get(Type.HIGHWAY)){
            element.draw(gc);
            gc.fill();
        }
        for (Drawable element : drawables.get(Type.BUILDING)) {
            element.draw(gc);
            gc.fill();
        }
        for (Drawable element : drawables.get(Type.NATURAL)) {
            element.draw(gc);
            gc.fill();
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