package org.example;

public class App {
    public static void main(String[] args) {
        AVLTree tree = new AVLTree();
        tree.insert(3);
        tree.insert(4);
        tree.insert(5);
        tree.insert(6);

        System.out.println(tree.serialize());

        tree.delete(6);
        System.out.println(tree.serialize()); 

        AVLTree rebuilt = AVLTree.deserialize(tree.serialize());
        System.out.println(rebuilt.serialize()); 
    }
}
