package io.libsoft.tessel.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class EdgeTest {


  @Test
  void name() {
    Node node1 = new Node(0, 0);
    Node node2 = new Node(1, 0);
    Node node3 = new Node(2, 0);

    Edge edge1 = new Edge(node1, node2);
    Edge edge2 = new Edge(node2, node1);
    Edge edge3 = new Edge(node1, node2);
    Edge edge4 = new Edge(node1, node3);

    assertTrue(edge1.equals(edge3));
    assertTrue(edge1.equals(edge2));
    assertTrue(edge1.equals(edge1));
    assertFalse(edge1.equals(edge4));
  }


  @Test
  void equalities() {
    Triangle t1 = new Triangle(new Node(0, 0), new Node(1, 0), new Node(0, 1));
    Triangle t2 = new Triangle(new Node(1, 0), new Node(0, 0), new Node(0, 1));

    assertTrue(t1.equals(t2));
  }
}