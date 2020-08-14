package io.libsoft.tessel.model;


import java.util.LinkedList;
import java.util.List;
import lombok.Data;

@Data
public class Node {

  private double x, y;

  private List<Node> connections = new LinkedList<>();
  private List<Node> consideredNeighbors = new LinkedList<>();

  public Node(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double getNorm(Node o){
    return Math.hypot(x - o.x, y - o.y);
  }

}
