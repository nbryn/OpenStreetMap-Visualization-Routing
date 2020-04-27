package bfst20.presentation;

import bfst20.data.AddressData;
import bfst20.data.InterestPointData;
import bfst20.logic.AppController;
import bfst20.logic.kdtree.KDNode;
import bfst20.logic.kdtree.KDTree;
import bfst20.logic.misc.OSMType;
import bfst20.logic.entities.*;
import bfst20.logic.kdtree.Rect;
import bfst20.logic.misc.Vehicle;
import bfst20.logic.routing.Edge;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.transform.Affine;


import java.util.*;
import java.util.List;

import javafx.scene.transform.NonInvertibleTransformException;


public class View {

    private AppController appController;
    private Affine trans = new Affine();
    private Canvas canvas;
    private GraphicsContext gc;
    private Map<OSMType, List<LinePath>> linePaths;
    private List<LinePath> coastLine;
    private boolean kd;
    private boolean isColorBlindMode = false;
    private String addressString;
    private Point2D mousePos;
    private String address1, address2;


    Label mouseLocationLabel;

    double zoomLevel = 1.0;
    double timesZoomed = 0.0;
    double sliderValue = 0;

    public View(Canvas canvas) {
        address1 = "";
        address2 = "";
        mousePos = new Point2D(0, 0);
        appController = new AppController();
        kd = false;
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void initialize() {
        trans = new Affine();
        linePaths = appController.getLinePathsFromModel();
        appController.clearLinePathData();

        createKDTrees();
    }

    public void setMousePos(Point2D mousePos) {
        this.mousePos = mousePos;
    }

    private void createKDTrees() {
        appController.setupRect();

        for (Map.Entry<OSMType, List<LinePath>> entry : linePaths.entrySet()) {
            if (entry.getKey() == OSMType.HIGHWAY || entry.getKey() == OSMType.RESIDENTIAL_HIGHWAY || entry.getKey() == OSMType.TERTIARY || entry.getKey() == OSMType.UNCLASSIFIED_HIGHWAY)
                continue;
            if (entry.getValue().size() != 0) {
                appController.addKDTreeToModel(entry.getKey(), entry.getValue());
            }
        }

        List<LinePath> highways = appController.getHighwaysFromModel();

        appController.addKDTreeToModel(OSMType.HIGHWAY, highways);
        appController.addKDTreeToModel(OSMType.COASTLINE, linePaths.get(OSMType.COASTLINE));
        coastLine = linePaths.get(OSMType.COASTLINE);
        linePaths = null;
        System.gc();

        Bounds bounds = appController.getBoundsFromModel();

        float minLon = bounds.getMinLon();
        float maxLon = bounds.getMaxLon();
        float minLat = bounds.getMinLat();
        float maxLat = bounds.getMaxLat();

        pan(-minLon, -minLat);
        zoom(canvas.getHeight() / (maxLon - minLon), (minLat - maxLat) / 2, 0,1);

        repaint();
    }

    private long lastTime = 0;

    private boolean fps(){
        Date date = new Date();

        if(lastTime == 0){
            lastTime = date.getTime();
            return false;
        }else{
            if((date.getTime() - 30) < lastTime){
                return true;
            }
        }

        lastTime = date.getTime();

        return false;
    }

    double pixelwidth;

    public void repaint() {

        if(fps()) return;

        gc.setTransform(new Affine());
        gc.setFill(Color.LIGHTBLUE);

        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.strokeRect(0,0, canvas.getWidth(), canvas.getHeight());
        gc.setTransform(trans);

        pixelwidth = 1 / Math.sqrt(Math.abs(trans.determinant()));

        int boxSize = 300;

        Rect rect = createRect(boxSize);

        Point2D mouse = toModelCoords(
                mousePos.getX(),
                mousePos.getY());


        //drawTypeKdTree(OSMType.COASTLINE, rect, pixelwidth);

        for(LinePath path : coastLine){
            drawLinePath(path, pixelwidth);
        }

        drawTypeKdTree(OSMType.BEACH, rect, pixelwidth);
        drawTypeKdTree(OSMType.FARMLAND, rect, pixelwidth);
        drawTypeKdTree(OSMType.RESIDENTIAL, rect, pixelwidth);
        drawTypeKdTree(OSMType.HEATH, rect, pixelwidth);
        drawTypeKdTree(OSMType.WOOD, rect, pixelwidth);
        //drawTypeKdTree(OSMType.TREE_ROW, rect, pixelwidth);
        drawTypeKdTree(OSMType.WATER, rect, pixelwidth);
        drawTypeKdTree(OSMType.FOREST, rect, pixelwidth);
        drawTypeKdTree(OSMType.BUILDING, rect, pixelwidth);
        //drawTypeKdTree(OSMType.MEADOW, rect, pixelwidth);

        drawTypeKdTree(OSMType.HIGHWAY, rect, pixelwidth, mouse);
        //drawKdTest();

        /*drawTypeKdTree(Type.HIGHWAY, rect, pixelwidth, mouse);
        drawTypeKdTree(Type.TERTIARY, rect, pixelwidth, mouse);
        drawTypeKdTree(Type.RESIDENTIAL_HIGHWAY, rect, pixelwidth, mouse);
        drawTypeKdTree(Type.UNCLASSIFIED_HIGHWAY, rect, pixelwidth, mouse);*/

        //mouseLocationLabel.setText(kdTrees.get(Type.HIGHWAY).getClosetsLinepath().getName());

        mouseLocationLabel.setText(appController.getKDTreeFromModel(OSMType.HIGHWAY).getClosetsLinepath().getName());

        Point2D mc1 = toModelCoords((canvas.getWidth() / 2) - boxSize, (canvas.getHeight() / 2) - boxSize);
        Point2D mc2 = toModelCoords((canvas.getWidth() / 2) + boxSize, (canvas.getHeight() / 2) + boxSize);

        //gc.setStroke(Color.PURPLE);
        //gc.strokeRect(mouse.getX(), mouse.getY(), 0.001, 0.001);
        gc.strokeRect(mc1.getX(), mc1.getY(), mc2.getX() - mc1.getX(), mc2.getY() - mc1.getY());

        drawSearchLocation(pixelwidth);
        drawInterestPoints(pixelwidth);

        /*shortestPath("Sølyst 3", "Vestergade 39" , Vehicle.CAR, pixelwidth);

        if(!address1.equals("") && !address2.equals("")){
            shortestPath(address1, address2,Vehicle.CAR, pixelwidth);
        }*/

        drawInterestPoints(pixelwidth);

        if(route != null){
            for (Edge edge : route) {
                drawRoute(edge, pixelwidth);
            }
        }
    }


    public void setAddress(String address1, String address2){
        this.address2 = address2;
        this.address1 = address1;
    }

    private Rect createRect(int boxSize) {
        Point2D mc1 = toModelCoords((canvas.getWidth() / 2) - boxSize, (canvas.getHeight() / 2) - boxSize);
        Point2D mc2 = toModelCoords((canvas.getWidth() / 2) + boxSize, (canvas.getHeight() / 2) + boxSize);
        return new Rect((float) mc1.getY(), (float) mc2.getY(), (float) mc1.getX(), (float) mc2.getX());
    }

    private void drawInterestPoints(double lineWidth) {
        InterestPointData interestPointData = InterestPointData.getInstance();

        for (InterestPoint interestPoint : interestPointData.getAllIntrestPoints()) {
            int bubbleSize = 30;

            drawLocation(lineWidth, bubbleSize, interestPoint.getLongitude(), interestPoint.getLatitude());
        }
    }

    
    public void searchRoute(){
       //ItshortestPath("Sølyst 3", "Vestergade 39" , Vehicle.CAR);
       //shortestPath("Oven Bæltet 13", "Sommerbyen 93", Vehicle.CAR);
        shortestPath("Sølyst 3", "Vestergade 37", Vehicle.CAR);
    }

    List<Edge> route = null;


    private void shortestPath(String sourceQuery, String targetQuery, Vehicle vehicle) {

        double distance = appController.initializeRouting(sourceQuery, targetQuery, vehicle);

        route = appController.getRouteFromModel();

        repaint();
    }

    public void setSearchString(String addressString) {
        this.addressString = addressString;

        repaint();
    }

    public void drawSearchLocation(double lineWidth) {
        if (addressString == null) return;
        AddressData addressData = AddressData.getInstance();
        Address address = addressData.search(addressString);

        if (address == null) {
            System.out.println("Missing");
            return;
        }

        int bubbleSize = 30;

        drawLocation(lineWidth, bubbleSize, address.getLon(), address.getLat());
        //gc.strokeRect(address.getLon(), address.getLat(), 1, 1);
    }

    private void drawLocation(double lineWidth, int bubbleSize, float lon, float lat) {
        gc.strokeOval(lon - (lineWidth * bubbleSize / 2), lat - (lineWidth * bubbleSize * 1.4), lineWidth * bubbleSize, lineWidth * bubbleSize);
        gc.moveTo(lon - (lineWidth * bubbleSize / 2), lat - (lineWidth * bubbleSize));
        gc.lineTo(lon, lat);
        gc.moveTo(lon + (lineWidth * bubbleSize / 2), lat - (lineWidth * bubbleSize));
        gc.lineTo(lon, lat);
        gc.stroke();
    }

    public void drawTypeKdTree(OSMType OSMType, Rect rect, double lineWidth) {
        for (LinePath linePath : appController.getKDTreeFromModel(OSMType).query(rect, trans.determinant())) {

            drawLinePath(linePath, lineWidth);
            gc.fill();
        }
    }

    public void drawTypeKdTree(OSMType OSMType, Rect rect, double lineWidth, Point2D point) {
        for (LinePath linePath : appController.getKDTreeFromModel(OSMType).query(rect, trans.determinant(), point)) {

            drawLinePath(linePath, lineWidth);
            gc.fill();
        }
    }

    public void drawKdTest(){
        KDNode root = appController.getKDTreeFromModel(OSMType.HIGHWAY).getRoot();

        gc.setStroke(Color.BLUE);
        gc.moveTo(appController.getRectFromModel().getMinLon(), root.getSplit());
        gc.lineTo(appController.getRectFromModel().getMaxLon(), root.getSplit());
        //System.out.println(root.getSplit());
        gc.stroke();
    }

    private void drawRoute(Edge edge, double lineWidth) {
        gc.setLineWidth(lineWidth);
        gc.beginPath();
        gc.setStroke(OSMType.getColor(OSMType.ROUTING, false));



        traceEdge(edge, gc);
        gc.stroke();
    }

    private void traceEdge(Edge edge, GraphicsContext gc) {
        Node sourceNode = edge.getSource();
        Node targetNode = edge.getTarget();

        float[] coords = new float[]{sourceNode.getLongitude(), sourceNode.getLatitude(), targetNode.getLongitude(), targetNode.getLatitude()};
        gc.setStroke(OSMType.getColor(OSMType.ROUTING, false));
        gc.moveTo(coords[0], coords[1]);
        for (int i = 2; i <= coords.length; i += 2) {
            gc.lineTo(coords[i - 2], coords[i - 1]);
        }
    }


    private void drawLinePath(LinePath linePath, double lineWidth) {

        if(linePath.getOSMType() == OSMType.TREE_ROW && linePath.getWayId() == 165460372){
            String i = "";
        }


        OSMType OSMType = linePath.getOSMType();
        gc.setLineWidth(OSMType.getLineWidth(OSMType, lineWidth));
        gc.beginPath();
        gc.setStroke(OSMType.getColor(OSMType, isColorBlindMode));
        gc.setFill(linePath.getFill() ? OSMType.getColor(OSMType, isColorBlindMode) : Color.TRANSPARENT);

        //System.out.println(linePath.getOSMType());

        if(linePath.isMultiploygon()){
            traceMultipolygon(linePath, gc);
        }else{
            trace(linePath, gc);
            gc.stroke();

            if(OSMType.getFill(linePath.getOSMType())){
                gc.fill();
            }
        }

    }

    private void traceMultipolygon(LinePath linePath, GraphicsContext gc) {
        gc.setFillRule(FillRule.EVEN_ODD);
        float[] coords = linePath.getCoords();
        gc.moveTo(coords[0], coords[1]);
        for (int i = 2; i <= coords.length; i += 2) {
            if(coords[i-2] == -99999.0){
                gc.moveTo(coords[i-1], coords[i]);
            }else{
                gc.lineTo(coords[i - 2], coords[i - 1]);
            }
        }
        gc.fill();
    }



    private void trace(LinePath linePath, GraphicsContext gc) {
        float[] coords = linePath.getCoords();
        gc.moveTo(coords[0], coords[1]);
        for (int i = 2; i <= coords.length; i += 2) {
            gc.lineTo(coords[i - 2], coords[i - 1]);
        }
    }

    public Point2D toModelCoords(double x, double y) {
        try {
            return trans.inverseTransform(x, y);
        } catch (NonInvertibleTransformException e) {
            //It is not possible for java to throw this exeption, but the try/catch is needed anyways.
            // Troels siger at det her ikke kan ske
            return null;
        }
    }

    public void zoom(double factor, double x, double y, double deltaY) {
        if (deltaY<0 && zoomLevel > 1.0)
        {
            scale(factor,x,y,deltaY);
        }
        if (deltaY>0 && zoomLevel <= 150) {
            scale(factor,x,y,deltaY);
        }
        reduceZoomLevel();
        reduceTimesZoomed();
    }


    public void scale(double factor, double x, double y, double deltaY)
    {
        trans.prependScale(factor, factor, x, y);
        zoomLevel *= factor;
        timesZoomed += deltaY/40;
        repaint();
    }

    public void reduceZoomLevel() {
        if (zoomLevel > 2500)
        {
            zoomLevel = zoomLevel/2517.0648374271736;
        }
    }

    public void reduceTimesZoomed()
    {
        if (timesZoomed>126)
        {
            timesZoomed = 126;
        }
    }

    public void pan(double dx, double dy) {
        trans.prependTranslation(dx, dy);
        repaint();
    }

    public void displayError(Alert.AlertType type, String text){
        Alert alert = new Alert(type, text, ButtonType.OK);
        alert.showAndWait();
    }

    public void changeToColorBlindMode(boolean isColorBlindMode) {
        this.isColorBlindMode = isColorBlindMode;
    }

    public void setMouseLocationView(Label mouseLocationLabel) {
        this.mouseLocationLabel = mouseLocationLabel;
    }


    public double getTimesZoomed() {
        return  timesZoomed;
    }

    public void setSliderValue(double value)
    {
        sliderValue = value;
    }

    public double getSliderValue()
    {
        return sliderValue;
    }
}