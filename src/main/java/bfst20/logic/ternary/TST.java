package bfst20.logic.ternary;

import bfst20.logic.entities.Address;

public class TST {
    private class Node{
        private Node left, mid, right;
        private char key;
        private Address value;

        public Node(char key, Address value){
            this.key = key;
            this.value = value;
        }

        public Node getLeft()               {return left;}
        public Node getMid()                {return mid;}
        public Node getRight()              {return right;}

        public char getKey()                {return key;}

        public Address getValue()           {return value;}

        public void setLeft(Node left)      {this.left = left;}
        public void setMid(Node mid)        {this.mid = mid;}
        public void setRight(Node right)    {this.right = right;}
    }

    private Node root;
    private int size;

    public void put(String key, Address value){
        root = put(root, key, value, 0);
    }

    private Node put(Node parent, String key, Address value, int index){

        char charecter = key.charAt(index);

        if(parent == null){
            size++;
            parent = new Node(charecter, value);
        }

        if(charecter > parent.getKey()){
            parent.setRight(put(parent.getRight(), key, value, index));
        }else if(charecter < parent.getKey()){
            parent.setLeft(put(parent.getLeft(), key, value, index));
        }else if(index < key.length() - 1){
            parent.setMid(put(parent.getMid(), key, value, index+1));
        }

        return parent;
    }

    public Address get(String key){

        Node result = get(root, key, 0);

        return result == null ? null : result.getValue();
    }

    private Node get(Node parent, String key, int index){
        char charecter = key.charAt(index);
        if(charecter > parent.getKey()){
            return get(parent.getRight(), key, index);
        }else if(charecter < parent.getKey()){
            return get(parent.getLeft(), key, index);
        }else if(index < key.length() - 1){
            return get(parent.getMid(), key, index+1);
        }else{
            return parent;
        }
    }

    public int getSize(){
        return size;
    }

}
