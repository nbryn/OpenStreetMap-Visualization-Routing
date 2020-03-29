package bfst20.logic.kdtree;

import bfst20.logic.kdtree.KdTree;
import bfst20.presentation.LinePath;

public class KdNode{
    private LinePath path;
    private KdNode left;
    private KdNode right;
    private Direction direction;
    private float split;

    public void setLeftNode(KdNode node){
        left = node;
    }
    public void setRightNode(KdNode node){
        right = node;
    }
    public void setLinePath(LinePath path){
        this.path = path;
    }
    public void setDirection(Direction direction){
        this.direction = direction;
    }
    public void setSplit(float split) {this.split = split;}

    public KdNode getLeftNode(){return left;}
    public KdNode getRightNode(){return right;}
    public Direction getDirection() {return direction;}
    public float getSplit() {return split;}
    public LinePath getLinePath() {return path;}
}