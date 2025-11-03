package org.example;

import java.util.*;

public class AVLTree {
    private static final String NIL = "nil";

    private static final class Node {
        int key, height;
        Node left, right;
        Node(int k) { key = k; height = 1; }
    }

    private Node root;

    public void insert(int value) { root = insert(root, value); }
    public void delete(int value) { root = delete(root, value); }

    public String serialize() {
        if (root == null) return "";
        List<String> out = new ArrayList<>();
        Queue<Node> q = new LinkedList<>(); // the plan was to use ArrayDeque<>(); but these dont allow null elements so rip
        q.add(root);
        int nonNullInQueue = 1;
        while (!q.isEmpty() && nonNullInQueue > 0) {
            Node n = q.poll();
            if (n == null) {
                out.add(NIL);
                q.add(null); q.add(null); 
            } else {
                out.add(Integer.toString(n.key));
                nonNullInQueue--; // this node was a non-null pending item
                if (n.left != null) nonNullInQueue++;
                if (n.right != null) nonNullInQueue++;
                q.add(n.left);
                q.add(n.right);
            }
        }

        int i = out.size() - 1;
        while (i >= 0 && out.get(i).equals(NIL)) i--; // this is to get rid of trailing nils
        return String.join(",", out.subList(0, i + 1));
    }

    public static AVLTree deserialize(String s) {
        AVLTree t = new AVLTree();
        if (s == null || s.isEmpty()) return t;
        for (String tok : s.split(",")) {
            tok = tok.trim();
            if (!tok.isEmpty() && !tok.equalsIgnoreCase(NIL)) {
                t.insert(Integer.parseInt(tok));
            }
        }
        return t;
    }

    private int h(Node n) { return n == null ? 0 : n.height; }

    private int balance(Node n) { return n == null ? 0 : h(n.left) - h(n.right); }

    private void update(Node n) {
        n.height = Math.max(h(n.left), h(n.right)) + 1;
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node t2 = x.right;
        x.right = y;
        y.left = t2;
        update(y); update(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node t2 = y.left;
        y.left = x;
        x.right = t2;
        update(x); update(y);
        return y;
    }

    private Node rebalance(Node n) {
        update(n);
        int bf = balance(n);
        if (bf > 1) {  
            if (balance(n.left) < 0) 
                n.left = rotateLeft(n.left);
            return rotateRight(n);    
        }
        if (bf < -1) {               
            if (balance(n.right) > 0) 
                n.right = rotateRight(n.right);
            return rotateLeft(n);   
        }
        return n;
    }

    private Node insert(Node n, int key) {
        if (n == null) return new Node(key);
        if (key < n.key) n.left = insert(n.left, key);
        else if (key > n.key) n.right = insert(n.right, key);
        else return n; // duplicates
        return rebalance(n);
    }

    private Node minNode(Node n) {
        while (n.left != null) n = n.left;
        return n;
    }

    private Node delete(Node n, int key) {
        if (n == null) return null;
        if (key < n.key) n.left = delete(n.left, key);
        else if (key > n.key) n.right = delete(n.right, key);
        else {
            if (n.left == null || n.right == null) {
                n = (n.left != null) ? n.left : n.right;
            } else {
                Node succ = minNode(n.right);
                n.key = succ.key;
                n.right = delete(n.right, succ.key);
            }
        }
        if (n == null) return null;
        return rebalance(n);
    }

    // To help with unit tests
    public boolean contains(int v) { return contains(root, v); }
    private boolean contains(Node n, int v) {
        if (n == null) return false;
        if (v == n.key) return true;
        return v < n.key ? contains(n.left, v) : contains(n.right, v);
    }
}
