package bfst20.logic.kdtree;

import bfst20.presentation.LinePath;

public class KDNode {
    private LinePath path;
    private KDNode left;
    private KDNode right;
    private Direction direction;
    private float split;

    public void setLeftNode(KDNode node){
        left = node;
    }
    public void setRightNode(KDNode node){
        right = node;
    }
    public void setLinePath(LinePath path){
        this.path = path;
    }
    public void setDirection(Direction direction){
        this.direction = direction;
    }
    public void setSplit(float split) {this.split = split;}

    public KDNode getLeftNode(){return left;}
    public KDNode getRightNode(){return right;}
    public Direction getDirection() {return direction;}
    public float getSplit() {return split;}
    public LinePath getLinePath() {return path;}
}