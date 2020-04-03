package bfst20.logic.kdtree;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import bfst20.presentation.LinePath;
import javafx.geometry.Point2D;

import javax.sound.sampled.Line;

public class KdTree{

    private KdNode root;
    private List<LinePath> currentClosestPaths;

    public KdTree(List<LinePath> paths,Rect rect){
        root = new KdNode();
        root.setLinePath(paths.get(0));
        root.setDirection(Direction.Latitudinal);
        root.setSplit(rect.getMinlat() + (rect.getMaxlat()-rect.getMinlat())/2);

        for(int i = 1; i < paths.size(); i++){
            LinePath path = paths.get(i);

            insert(root, path);
        }
    }

    public Iterable<LinePath> query(Rect rect){
        List<LinePath> list = new ArrayList<>();
        range(root,  rect, list);
        return list;
    }

    public Iterable<LinePath> getCurrentClosestPaths(){
        return currentClosestPaths;
    }

    public Iterable<LinePath> query(Rect rect, Point2D point){
        List<LinePath> list = new ArrayList<>();
        currentClosestPaths = new ArrayList<>();
        range(root,  rect, list, point);
        return list;
    }

    private void range(KdNode node, Rect rect, List<LinePath> list){
        if (node == null) return;

        if (rect.contains(node)) {
            list.add(node.getLinePath());

        }

        if(rect.intersects(node)){
            range(node.getRightNode(), rect, list);
            range(node.getLeftNode(), rect, list);
        }

    }

    private void range(KdNode node, Rect rect, List<LinePath> list, Point2D point){
        if (node == null) return;

        if (rect.contains(node)) {
            list.add(node.getLinePath());
            float distance = (float) Math.sqrt(Math.pow(node.getLinePath().getCenterLatitude() - point.getY(), 2) + Math.pow(node.getLinePath().getCenterLongitude() - point.getX(), 2));

            if(distance < 0.001){
                currentClosestPaths.add(node.getLinePath());
            }
        }

        if(rect.intersects(node)){
            range(node.getRightNode(), rect, list, point);
            range(node.getLeftNode(), rect, list, point);
        }

    }

    private KdNode createNewKdNode(LinePath path, Direction direction){
        KdNode node = new KdNode();
        node.setDirection(direction);
        if(direction == Direction.Latitudinal){
            node.setSplit(path.getCenterLatitude());
        }else{
            node.setSplit(path.getCenterLongitude());
        }
        node.setLinePath(path);

        return node;
    }

    private void insertNode(KdNode node, LinePath path){
        //Both nodes are empty
        if(node.getDirection() == Direction.Latitudinal){
            KdNode newNode = createNewKdNode(path, Direction.Longitudinal);
            if(node.getSplit() > path.getCenterLatitude()){
                //Since it is less it would be to the left of the split line for the node.
                node.setLeftNode(newNode);
            }else{
                node.setRightNode(newNode);
            }
        }else{
            KdNode newNode = createNewKdNode(path, Direction.Latitudinal);
            if(node.getSplit() > path.getCenterLongitude()){
                //Since it is less it would be to the left of the split line for the node.
                node.setRightNode(newNode);
            }else{
                node.setLeftNode(newNode);
            }
        }
    }

    private void insertNodeLeftExsists(KdNode node, LinePath path){
        //Left node is there but no right node
        if(node.getDirection() == Direction.Latitudinal){
            KdNode newNode = createNewKdNode(path, Direction.Longitudinal);
            if(node.getSplit() > path.getCenterLatitude()){
                //Since it is less it would be to the left of the split line for the node.
                //node.setLeftNode(newNode);
                insert(node.getLeftNode(), path); // Since left node exists
            }else{
                node.setRightNode(newNode);
            }
        }else{
            KdNode newNode = createNewKdNode(path, Direction.Latitudinal);

            if(node.getSplit() > path.getCenterLongitude()){
                //Since it is less it would be to the left of the split line for the node.
                node.setRightNode(newNode);
            }else{
                //node.setLeftNode(newNode);
                insert(node.getLeftNode(), path); // Since left node exists
            }
        }
    }

    private void insertNodeRightExsists(KdNode node, LinePath path){
        //Right node is there but no left node.
        if(node.getDirection() == Direction.Latitudinal){
            KdNode newNode = createNewKdNode(path, Direction.Longitudinal);
            if(node.getSplit() > path.getCenterLatitude()){
                //Since it is less it would be to the left of the split line for the node.
                node.setLeftNode(newNode);
            }else{
                //node.setRightNode(newNode);
                insert(node.getRightNode(), path); // Since left node exists

            }
        }else{
            KdNode newNode = createNewKdNode(path, Direction.Latitudinal);
            if(node.getSplit() > path.getCenterLongitude()){
                //Since it is less it would be to the left of the split line for the node.
                //node.setRightNode(newNode);
                insert(node.getRightNode(), path); // Since left node exists
            }else{
                node.setLeftNode(newNode);
            }
        }
    }

    private void insertNodeBothExsists(KdNode node, LinePath path){
        //Both nodes are there.
        if(node.getDirection() == Direction.Latitudinal){
            KdNode newNode = createNewKdNode(path, Direction.Longitudinal);
            if(node.getSplit() > path.getCenterLatitude()){
                //Since it is less it would be to the left of the split line for the node.
                //node.setLeftNode(newNode);
                insert(node.getLeftNode(), path); // Since left node exists
            }else{
                insert(node.getRightNode(), path); // Since left node exists
            }
        }else{
            KdNode newNode = createNewKdNode(path, Direction.Latitudinal);
            if(node.getSplit() > path.getCenterLongitude()){
                //Since it is less it would be to the left of the split line for the node.
                //node.setRightNode(newNode);
                insert(node.getRightNode(), path); // Since left node exists
            }else{
                //node.setLeftNode(newNode);
                insert(node.getLeftNode(), path); // Since left node exists
            }
        }
    }

    private void insert(KdNode node, LinePath path){

        if(node.getLeftNode() == null && node.getRightNode() == null){
            insertNode(node, path);
        }else if(node.getLeftNode() != null && node.getRightNode() == null){
            insertNodeLeftExsists(node, path);
        }else if(node.getRightNode() != null && node.getLeftNode() == null){
            insertNodeRightExsists(node, path);
        }else if(node.getLeftNode() != null && node.getRightNode() != null){
            insertNodeBothExsists(node, path);
        }
    }


}
