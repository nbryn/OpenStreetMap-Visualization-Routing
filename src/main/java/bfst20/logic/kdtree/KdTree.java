package bfst20.logic.kdtree;

import java.util.ArrayList;
import java.util.List;

import bfst20.presentation.LinePath;
 
public class KdTree{

    private KdNode root;

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

    private void range(KdNode node, Rect rect, List<LinePath> list ){
        if (node == null) return;
        // If the current point is in the input rectangle, enqueue that point
        if (rect.contains(node)) {
            list.add(node.getLinePath());
        }
        // Check the left and right subtrees if the input rectangle intersects
        // the current rectangle
        /*if (rect.intersects(node)) {
            range(node.getLeftNode(), rect, list);
            range(node.getRightNode(), rect, list);
        }*/

        if(rect.intersectsRight(node)){
            range(node.getRightNode(), rect, list);
        }

        if(rect.intersectsLeft(node)){
            range(node.getLeftNode(), rect, list);
        }
    }

   /* private void insert(KdNode node, LinePath path){
        if(node.getLeftNode() == null && node.getRightNode() == null){
            //Both nodes are empty
            KdNode newNode = new KdNode();
            newNode.setLinePath(path);
            if(node.getDirection() == Direction.Latitudinal){
                newNode.setDirection(Direction.Longitudinal);
                newNode.setSplit(path.getCenterLongitude());
                if(node.getSplit() > path.getCenterLatitude()){
                    //Since it is less it would be to the left of the split line for the node.
                    node.setLeftNode(newNode);
                }else{
                    node.setRightNode(newNode);
                }
            }else{
                newNode.setDirection(Direction.Latitudinal);
                newNode.setSplit(path.getCenterLatitude());
                if(node.getSplit() > path.getCenterLongitude()){
                    //Since it is less it would be to the left of the split line for the node.
                    node.setRightNode(newNode);
                }else{
                    node.setLeftNode(newNode);
                }
            }
        }
    }*/

    private void insert(KdNode node, LinePath path){

        if(node.getLeftNode() == null && node.getRightNode() == null){
            //Both nodes are empty
            KdNode newNode = new KdNode();
            if(node.getDirection() == Direction.Latitudinal){
                newNode.setDirection(Direction.Longitudinal);
                newNode.setSplit(path.getCenterLongitude());
                newNode.setLinePath(path);
                if(node.getSplit() > path.getCenterLatitude()){
                    //Since it is less it would be to the left of the split line for the node.
                    node.setLeftNode(newNode);
                }else{
                    node.setRightNode(newNode);
                }
            }else{
                newNode.setDirection(Direction.Latitudinal);
                newNode.setSplit(path.getCenterLatitude());
                newNode.setLinePath(path);
                if(node.getSplit() > path.getCenterLongitude()){
                    //Since it is less it would be to the left of the split line for the node.
                    node.setRightNode(newNode);
                }else{
                    node.setLeftNode(newNode);
                }
            }
        }else if(node.getLeftNode() != null && node.getRightNode() == null){
            //Left node is there but no right node
            KdNode newNode = new KdNode();
            if(node.getDirection() == Direction.Latitudinal){
                newNode.setDirection(Direction.Longitudinal);
                newNode.setSplit(path.getCenterLongitude());
                newNode.setLinePath(path);
                if(node.getSplit() > path.getCenterLatitude()){
                    //Since it is less it would be to the left of the split line for the node.
                    //node.setLeftNode(newNode);
                    insert(node.getLeftNode(), path); // Since left node exists
                }else{
                    node.setRightNode(newNode);
                }
            }else{
                newNode.setDirection(Direction.Latitudinal);
                newNode.setSplit(path.getCenterLatitude());
                newNode.setLinePath(path);
                if(node.getSplit() > path.getCenterLongitude()){
                    //Since it is less it would be to the left of the split line for the node.
                    node.setRightNode(newNode);
                }else{
                    //node.setLeftNode(newNode);
                    insert(node.getLeftNode(), path); // Since left node exists
                }
            }
        }else if(node.getRightNode() != null && node.getLeftNode() == null){
            //Right node is there but no left node.
            KdNode newNode = new KdNode();
            if(node.getDirection() == Direction.Latitudinal){
                newNode.setDirection(Direction.Longitudinal);
                newNode.setSplit(path.getCenterLongitude());
                newNode.setLinePath(path);
                if(node.getSplit() > path.getCenterLatitude()){
                    //Since it is less it would be to the left of the split line for the node.
                    node.setLeftNode(newNode);
                }else{
                    //node.setRightNode(newNode);
                    insert(node.getRightNode(), path); // Since left node exists

                }
            }else{
                newNode.setDirection(Direction.Latitudinal);
                newNode.setSplit(path.getCenterLatitude());
                newNode.setLinePath(path);
                if(node.getSplit() > path.getCenterLongitude()){
                    //Since it is less it would be to the left of the split line for the node.
                    //node.setRightNode(newNode);
                    insert(node.getRightNode(), path); // Since left node exists
                }else{
                    node.setLeftNode(newNode);
                }
            }
        }else if(node.getLeftNode() != null && node.getRightNode() != null){
            //Both nodes are there.
            KdNode newNode = new KdNode();
            if(node.getDirection() == Direction.Latitudinal){
                newNode.setDirection(Direction.Longitudinal);
                newNode.setSplit(path.getCenterLongitude());
                newNode.setLinePath(path);
                if(node.getSplit() > path.getCenterLatitude()){
                    //Since it is less it would be to the left of the split line for the node.
                    //node.setLeftNode(newNode);
                    insert(node.getLeftNode(), path); // Since left node exists
                }else{
                    insert(node.getRightNode(), path); // Since left node exists
                }
            }else{
                newNode.setDirection(Direction.Latitudinal);
                newNode.setSplit(path.getCenterLatitude());
                newNode.setLinePath(path);
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
    }


}
