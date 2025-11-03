package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AVLTreeTest {

    @Test
    void insertAndSerializeExample() {
        AVLTree t = new AVLTree();
        t.insert(3); t.insert(4); t.insert(5); t.insert(6);
        assertEquals("4,3,5,nil,nil,nil,6", t.serialize());
    }

    @Test
    void deleteAndSerializeExample() {
        AVLTree t = new AVLTree();
        t.insert(3); t.insert(4); t.insert(5); t.insert(6);
        t.delete(6);
        assertEquals("4,3,5", t.serialize());
    }

    @Test
    void deserializeRebuildsValues() {
        String s = "4,3,5,nil,nil,nil,6";
        AVLTree t = AVLTree.deserialize(s);
        assertTrue(t.contains(4) && t.contains(3) && t.contains(5) && t.contains(6));
        assertEquals(s, new AVLTree() {{ insert(3); insert(4); insert(5); insert(6); }}.serialize());
    }

    @Test
    void handlesEmptyAndDuplicates() {
        AVLTree t = new AVLTree();
        assertEquals("", t.serialize());
        t.insert(10); t.insert(10); // duplicate ignored
        assertEquals("10", t.serialize());
    }
}
