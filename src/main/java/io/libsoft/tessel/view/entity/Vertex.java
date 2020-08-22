package io.libsoft.tessel.view.entity;


import io.libsoft.tessel.model.Node;
import java.util.LinkedList;
import java.util.List;
import lombok.Data;

@Data
public class Vertex {

  private double x;
  private double y;
  private int connections;

  public Vertex(Node node) {
    x = node.getX();
    y = node.getY();
    connections = node.getConnections().size();
  }


  public Vertex(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public static List<Vertex> from(List<Node> nodes) {
    List<Vertex> vertices = new LinkedList<>();
    for (Node node : nodes) {
      vertices.add(new Vertex(node));
    }
    return vertices;
  }
}
