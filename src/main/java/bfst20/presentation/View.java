package bfst20.presentation;

import bfst20.data.AddressModel;
import bfst20.logic.AppController;
import bfst20.logic.entities.Address;
import bfst20.logic.entities.Bounds;
import bfst20.logic.entities.LinePath;
import bfst20.logic.kdtree.Rect;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bfst20.logic.Type;
import javafx.scene.transform.NonInvertibleTransformException;

public class View {

    private AppController appController;
    private Affine trans = new Affine();
    private Canvas canvas;
    private GraphicsContext gc;
    private Map<Type, List<LinePath>> linePaths;
    private List<LinePath> coastLine;
    private boolean kd;
    private boolean isColorBlindMode = false;
    private String addressString;

    Label mouseLocationLabel;

    public View(Canvas canvas) {
        appController = new AppController();
        kd = false;
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }


    public void initializeData() throws IOException {
        if (!appController.isBinary()) {
            appController.createLinePaths();
            //appController.generateBinary();
        }
        linePaths = appController.getLinePathsFromModel();
        appController.clearLinePathData();

        createKDTrees();
    }

    private void createKDTrees() {
        appController.setupRect();

        for (Map.Entry<Type, List<LinePath>> entry : linePaths.entrySet()) {
            if (entry.getKey() == Type.HIGHWAY || entry.getKey() == Type.RESIDENTIAL_HIGHWAY || entry.getKey() == Type.TERTIARY || entry.getKey() == Type.UNCLASSIFIED_HIGHWAY)
                continue;
            if (entry.getValue().size() != 0) {
                appController.addKDTreeToModel(entry.getKey(), entry.getValue());
            }
        }

        List<LinePath> allHighways = new ArrayList<>();
        allHighways.addAll(linePaths.get(Type.HIGHWAY));
        allHighways.addAll(linePaths.get(Type.TERTIARY));
        allHighways.addAll(linePaths.get(Type.UNCLASSIFIED_HIGHWAY));
        allHighways.addAll(linePaths.get(Type.RESIDENTIAL_HIGHWAY));
        if (linePaths.get(Type.MOTORWAY) != null) allHighways.addAll(linePaths.get(Type.MOTORWAY));

        appController.addKDTreeToModel(Type.HIGHWAY, allHighways);
        appController.addKDTreeToModel(Type.COASTLINE, linePaths.get(Type.COASTLINE));

        linePaths = null;
        System.gc();

        Bounds bounds = appController.getBoundsFromModel();

        float minLon = bounds.getMinLon();
        float maxLon = bounds.getMaxLon();
        float minLat = bounds.getMinLat();
        float maxLat = bounds.getMaxLat();

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

        int boxSize = 300;

        Point2D mc1 = toModelCoords((canvas.getWidth() / 2) - boxSize, (canvas.getHeight() / 2) - boxSize);
        Point2D mc2 = toModelCoords((canvas.getWidth() / 2) + boxSize, (canvas.getHeight() / 2) + boxSize);
        Rect rect = new Rect((float) mc1.getY(), (float) mc2.getY(), (float) mc1.getX(), (float) mc2.getX());


        // I still don't know why these constants are needed.
        Point2D mouse = toModelCoords(
                MouseInfo.getPointerInfo().getLocation().getX() - 490,
                MouseInfo.getPointerInfo().getLocation().getY() - 140);


        drawTypeKdTree(Type.COASTLINE, rect, pixelwidth);

        drawTypeKdTree(Type.FARMLAND, rect, pixelwidth);
        drawTypeKdTree(Type.RESIDENTIAL, rect, pixelwidth);
        drawTypeKdTree(Type.HEATH, rect, pixelwidth);
        drawTypeKdTree(Type.WOOD, rect, pixelwidth);
        drawTypeKdTree(Type.TREE_ROW, rect, pixelwidth);
        drawTypeKdTree(Type.WATER, rect, pixelwidth);
        drawTypeKdTree(Type.FOREST, rect, pixelwidth);
        drawTypeKdTree(Type.BUILDING, rect, pixelwidth);

        drawTypeKdTree(Type.HIGHWAY, rect, pixelwidth, mouse);

        /*drawTypeKdTree(Type.HIGHWAY, rect, pixelwidth, mouse);
        drawTypeKdTree(Type.TERTIARY, rect, pixelwidth, mouse);
        drawTypeKdTree(Type.RESIDENTIAL_HIGHWAY, rect, pixelwidth, mouse);
        drawTypeKdTree(Type.UNCLASSIFIED_HIGHWAY, rect, pixelwidth, mouse);*/

        //mouseLocationLabel.setText(kdTrees.get(Type.HIGHWAY).getClosetsLinepath().getName());

        mouseLocationLabel.setText(appController.getKDTreeFromModel(Type.HIGHWAY).getClosetsLinepath().getName());

        gc.setStroke(Color.PURPLE);
        //gc.strokeRect(mouse.getX(), mouse.getY(), 0.001, 0.001);
        gc.strokeRect(mc1.getX(), mc1.getY(), mc2.getX() - mc1.getX(), mc2.getY() - mc1.getY());

        drawSearchLocation(pixelwidth);
    }

    public void setSearchString(String addressString){
        this.addressString = addressString;

        repaint();
    }

    public void drawSearchLocation(double lineWidth){
        if(addressString == null) return;
        AddressModel addressModel = AddressModel.getInstance();
        Address address = addressModel.search(addressString);

        int bubbleSize = 30;

        gc.strokeOval(address.getLon()-(lineWidth*bubbleSize/2), address.getLat()-(lineWidth*bubbleSize*1.4), lineWidth*bubbleSize, lineWidth*bubbleSize);
        gc.moveTo(address.getLon()-(lineWidth*bubbleSize/2), address.getLat()-(lineWidth*bubbleSize));
        gc.lineTo(address.getLon(), address.getLat());
        gc.moveTo(address.getLon()+(lineWidth*bubbleSize/2), address.getLat()-(lineWidth*bubbleSize));
        gc.lineTo(address.getLon(), address.getLat());
        gc.stroke();
        //gc.strokeRect(address.getLon(), address.getLat(), 1, 1);
    }

    public void drawTypeKdTree(Type type, Rect rect, double lineWidth) {
        for (LinePath linePath : appController.getKDTreeFromModel(type).query(rect, trans.determinant())) {

            drawLinePath(linePath, lineWidth);
            gc.fill();
        }
    }

    public void drawTypeKdTree(Type type, Rect rect, double lineWidth, Point2D point) {
        for (LinePath linePath : appController.getKDTreeFromModel(type).query(rect, trans.determinant(), point)) {

            drawLinePath(linePath, lineWidth);
            gc.fill();
        }
    }

    private void drawLinePath(LinePath linePath, double lineWidth) {
        Type type = linePath.getType();
        gc.setLineWidth(Type.getLineWidth(type, lineWidth));
        gc.beginPath();
        gc.setStroke(Type.getColor(type, isColorBlindMode));
        gc.setFill(linePath.getFill() ? Type.getColor(type, isColorBlindMode) : Color.TRANSPARENT);

        /*if(way.getTagValue("name") != null){
            gc.setFill(Color.BLACK);
            gc.setFont(new Font(0.00022));
            gc.fillText(way.getTagValue("name"), coords[0], coords[1]);
            gc.setFill(fill ? color : Color.TRANSPARENT);
        }*/
        //gc.setStroke(Color.BLUE);
        //gc.strokeRect(minY, minX, maxY-minY, maxX-minX);
        //  gc.setStroke(color);

        trace(linePath, gc);
        gc.stroke();
    }

    private void trace(LinePath linePath, GraphicsContext gc) {
        float[] coords = linePath.getCoords();
        gc.moveTo(coords[0], coords[1]);
        for (int i = 2; i <= coords.length; i += 2) {
            gc.lineTo(coords[i - 2], coords[i - 1]);
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

    public void changeToColorBlindMode(boolean isColorBlindMode) {
        this.isColorBlindMode = isColorBlindMode;
    }

    public void setMouseLocationView(Label mouseLocationLabel) {
        this.mouseLocationLabel = mouseLocationLabel;
    }
}