package io.libsoft.tessel.model;


import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import lombok.Data;

@Data
public class Node {


  private double x, y;
  private List<Node> connections = new LinkedList<>();
  private List<Node> consideredNeighbors = new LinkedList<>();
  private List<Node> closestNeighbors = new LinkedList<>();


  public Node(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double getNorm(Node o) {
    return Math.hypot(x - o.x, y - o.y);
  }

  public void update() {

  }

  @Override
  public String toString() {
    return "Node{" +
        "x=" + x +
        ", y=" + y +
        '}';
  }

  public void reset() {
    getConsideredNeighbors().clear();
    getConnections().clear();
    getClosestNeighbors().clear();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Node node = (Node) o;

    if (Double.compare(node.x, x) != 0) {
      return false;
    }
    return Double.compare(node.y, y) == 0;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = Double.doubleToLongBits(x);
    result = (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(y);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }
}
