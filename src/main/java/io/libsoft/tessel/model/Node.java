package io.libsoft.tessel.model;


import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import lombok.Data;

@Data
public class Node {


  private static Random random = new Random();
  private double x, y;
  private final double K = 1e-8;
  private double xVel, yVel;
  private List<Node> connections = new LinkedList<>();
  private List<Node> consideredNeighbors = new LinkedList<>();
  private double xAcc, yAcc;
  private List<Node> closestNeighbors = new LinkedList<>();


  public Node(double x, double y) {
    this.x = x;
    this.y = y;

//    xVel = random.nextGaussian() * 2e-1;
//    xVel = random.nextGaussian() * 2e-1;
    xVel = random.nextGaussian() * 0;
    yVel = random.nextGaussian() * 0;
  }

  public double getNorm(Node o) {
    return Math.hypot(x - o.x, y - o.y);
  }

  public void update() {

    if (!connections.isEmpty()) {

      for (Node n : connections) {
        double r = n.getNorm(this);

        xAcc += -K * (x - n.x);
        yAcc += -K * (y - n.y);


      }

    }

    this.xVel += xAcc;
    this.yVel += yAcc;

    this.x += xVel * .2;
    this.y += yVel * .2;

    if (x < 5 || x > 995) {
      this.x -= xVel;
      xVel *= -.2;
    }
    if (y < 5 || y > 995) {
      this.y -= yVel;
      yVel *= -.2;
    }


  }

  @Override
  public String toString() {
    return "Node{" +
        "x=" + x +
        ", xVel=" + xVel +
        '}';
  }

  public void reset() {
    getConsideredNeighbors().clear();
    getConnections().clear();
    getClosestNeighbors().clear();
  }
}
