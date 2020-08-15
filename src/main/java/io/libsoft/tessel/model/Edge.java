package io.libsoft.tessel.model;


import lombok.Data;

@Data
public class Edge {

  private double points[] = new double[4];
  private int startConnectionSize;

  public Edge(Node start, Node end) {
    startConnectionSize = start.getConnections().size();
    points[0] = start.getX();
    points[1] = start.getY();

    points[2] = end.getX();
    points[3] = end.getY();
  }
}
