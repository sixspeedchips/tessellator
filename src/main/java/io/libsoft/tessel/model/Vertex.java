package io.libsoft.tessel.model;


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


}
