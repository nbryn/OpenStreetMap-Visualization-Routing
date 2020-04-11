package bfst20.presentation;

import bfst20.logic.AppController;
import bfst20.logic.entities.Bounds;
import bfst20.logic.kdtree.Rect;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bfst20.logic.Type;
import bfst20.logic.kdtree.KdTree;
import javafx.scene.transform.NonInvertibleTransformException;

public class View {

    private AppController appController;
    private Affine trans = new Affine();
    private Canvas canvas;
    private GraphicsContext gc;
    private Map<Type, List<LinePath>> linePaths;
    private Map<Type, KdTree> kdTrees;
    private List<LinePath> coastLine;
    private boolean kd;


    public View(Canvas canvas) {
        appController = new AppController();
        kd = false;
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }


    public void initializeData() throws IOException {

        if (appController.isBinary()) {

            linePaths = appController.getLinePathsFromModel();

        } else {
            appController.createLinePaths();
            //appController.generateBinary();
            linePaths = appController.getLinePathsFromModel();

        }
        appController.clearLinePathData();

        createKDTrees();
    }

    private void createKDTrees() {

        Bounds bounds = appController.getBoundsFromModel();

        float minLon = bounds.getMinLon();
        float maxLon = bounds.getMaxLon();
        float minLat = bounds.getMinLat();
        float maxLat = bounds.getMaxLat();

        kdTrees = new HashMap<>();
        Rect rect = new Rect(minLat, maxLat, minLon, maxLon);
        for (Map.Entry<Type, List<LinePath>> entry : linePaths.entrySet()) {

            if (entry.getValue().size() != 0) {
                kdTrees.put(entry.getKey(), new KdTree(entry.getValue(), rect));
            }
        }
        coastLine = linePaths.get(Type.COASTLINE);

        linePaths = null;
        System.gc();

        pan(-minLon, -minLat);
        zoom(canvas.getHeight() / (maxLon - minLon), (minLat - maxLat) / 2, 0);

        repaint();
    }

    public void repaint() {
        gc.setTransform(new Affine());
        gc.setFill(Color.LIGHTBLUE);

        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setTransform(trans);

        double pixelwidth = 1 / Math.sqrt(Math.abs(trans.determinant()));
        gc.setLineWidth(pixelwidth);

        int boxSize = 300;

        Point2D mc1 = toModelCoords((canvas.getWidth() / 2) - boxSize, (canvas.getHeight() / 2) - boxSize);
        Point2D mc2 = toModelCoords((canvas.getWidth() / 2) + boxSize, (canvas.getHeight() / 2) + boxSize);
        Rect rect = new Rect((float) mc1.getY(), (float) mc2.getY(), (float) mc1.getX(), (float) mc2.getX());


        // I still don't know why these constants are needed.
        Point2D mouse = toModelCoords(
                MouseInfo.getPointerInfo().getLocation().getX() - 490,
                MouseInfo.getPointerInfo().getLocation().getY() - 140);


        for (LinePath linePath : coastLine) {
            linePath.draw(gc);
            gc.fill();
        }

        drawTypeKdTree(Type.FARMLAND, rect);
        drawTypeKdTree(Type.RESIDENTIAL, rect);
        drawTypeKdTree(Type.HEATH, rect);
        drawTypeKdTree(Type.WOOD, rect);
        drawTypeKdTree(Type.TREE_ROW, rect);
        drawTypeKdTree(Type.WATER, rect);
        drawTypeKdTree(Type.FOREST, rect);
        drawTypeKdTree(Type.BUILDING, rect);
        drawTypeKdTree(Type.HIGHWAY, rect, mouse);
        drawTypeKdTree(Type.TERTIARY, rect, mouse);
        drawTypeKdTree(Type.RESIDENTIAL_HIGHWAY, rect, mouse);
        drawTypeKdTree(Type.UNCLASSIFIED_HIGHWAY, rect, mouse);

        // System.out.println(kdTrees.get(Type.HIGHWAY).getClosetsLinepath().getWay().getName());

        gc.setStroke(Color.PURPLE);
        gc.strokeRect(mouse.getX(), mouse.getY(), 0.001, 0.001);
        gc.strokeRect(mc1.getX(), mc1.getY(), mc2.getX() - mc1.getX(), mc2.getY() - mc1.getY());
    }


    public void drawTypeKdTree(Type type, Rect rect) {
        for (LinePath linePath : kdTrees.get(type).query(rect, trans.determinant())) {
            linePath.draw(gc);
            gc.fill();
        }
    }

    public void drawTypeKdTree(Type type, Rect rect, Point2D point) {
        for (LinePath linePath : kdTrees.get(type).query(rect, trans.determinant(), point)) {
            linePath.draw(gc);
            gc.fill();
        }
    }

    private Point2D toModelCoords(double x, double y) {
        try {
            return trans.inverseTransform(x, y);
        } catch (NonInvertibleTransformException e) {
            // Troels siger at det her ikke kan ske
            e.printStackTrace();
            return null;
        }
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