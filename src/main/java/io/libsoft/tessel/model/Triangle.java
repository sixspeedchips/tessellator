package io.libsoft.tessel.model;

import io.libsoft.tessel.util.Props;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


public class Triangle {


  private final HashSet<Edge> edges = new HashSet<>();
  private Node centroid = new Node(0, 0);
  private Node circumCenter = new Node(0, 0);
  private double radius;
  private double ax;
  private double ay;
  private double bx;
  private double by;
  private double cx;
  private double cy;
  private final HashSet<Node> nodes = new HashSet<Node>() {
    @Override
    public boolean add(Node node) {
      if (size() > 2) {
        return false;
      }
      super.add(node);
      if (size() == 3) {
        computeProperties();
      }
      return true;
    }

    @Override
    public boolean addAll(Collection<? extends Node> c) {
      for (Node node : c) {
        add(node);
      }
      return true;
    }
  };


  public Triangle() {
  }

  public Triangle(Edge edge, Node center) {
    nodes.addAll(edge.getTerminals());
    nodes.add(center);
  }

  public static List<Triangle> fromPolygon(HashSet<Edge> polygon, Node node) {
    List<Triangle> newTriangles = new LinkedList<>();
    for (Edge edge : polygon) {
      newTriangles.add(new Triangle(edge, node));
    }
    return newTriangles;
  }



  public boolean ccw() {
    return (bx - ax) * (cy - ay) - (cx - ax) * (by - ay) > 0;
  }

  public void computeCC() {

    double a = bx - ax;
    double b = by - ay;
    double c = cx - ax;
    double d = cy - ay;
    double e = a * (ax + bx) + b * (ay + by);
    double f = c * (ax + cx) + d * (ay + cy);
    double g = 2 * (a * (cy - by) - b * (cx - bx));

    circumCenter = new Node((d * e - b * f) / g, (a * f - c * e) / g);

    radius = Math.sqrt((circumCenter.getX() - ax) * (circumCenter.getX() - ax) + (circumCenter.getY() - ay) *
        (circumCenter.getY() - ay));
  }

  private void computeProperties() {
    double x = 0;
    double y = 0;

    for (Node vertex : nodes) {
      x += vertex.getX();
      y += vertex.getY();
    }
    x /= 3;
    y /= 3;
    centroid = new Node(x, y);
    List<Node> sortedNodes = new LinkedList<>(nodes);
    edges.add(new Edge(sortedNodes.get(0), sortedNodes.get(1)));
    edges.add(new Edge(sortedNodes.get(1), sortedNodes.get(2)));
    edges.add(new Edge(sortedNodes.get(0), sortedNodes.get(2)));
    sortedNodes.sort((o1, o2) -> {
      double t1 = Math.tan((o1.getY() - centroid.getY()) / (o1.getX() - centroid.getX()));
      double t2 = Math.tan((o2.getY() - centroid.getY()) / (o2.getX() - centroid.getX()));
      return Double.compare(t2, t1);
    });
    ax = sortedNodes.get(0).getX();
    ay = sortedNodes.get(0).getY();
    bx = sortedNodes.get(1).getX();
    by = sortedNodes.get(1).getY();
    cx = sortedNodes.get(2).getX();
    cy = sortedNodes.get(2).getY();

    computeCC();

  }

  public boolean containsNode(Node node) {

    double dx = node.getX();
    double dy = node.getY();

    double ax_ = ax - dx;
    double ay_ = ay - dy;
    double bx_ = bx - dx;
    double by_ = by - dy;
    double cx_ = cx - dx;
    double cy_ = cy - dy;

    boolean inside = (ax_ * ax_ + ay_ * ay_) * (bx_ * cy_ - cx_ * by_) -
        (bx_ * bx_ + by_ * by_) * (ax_ * cy_ - cx_ * ay_) +
        (cx_ * cx_ + cy_ * cy_) * (ax_ * by_ - bx_ * ay_) > 0;
    return inside;
  }

  public double[] getXPts() {
    double[] pts = new double[3];
    int i = 0;
    for (Node vertex : nodes) {
      pts[i++] = vertex.getX();
    }

    return pts;
  }

  public double[] getYPts() {
    double[] pts = new double[3];
    int i = 0;
    for (Node vertex : nodes) {
      pts[i++] = vertex.getY();
    }
    return pts;
  }

  public Node getCentroid() {
    return centroid;
  }

  public double getRadius() {
    return radius;
  }

  public Node getCircumCenter() {
    return circumCenter;
  }

  public static Triangle bounding() {
    Triangle t = new Triangle();

    t.nodes.add(new Node(0, 0));
    t.nodes.add(new Node(0, Props.get().getBounds() * 2));
    t.nodes.add(new Node(Props.get().getBounds() * 2, 0));
    return t;
  }

  public HashSet<Edge> getEdges() {
    return edges;
  }

  public boolean containsEdge(Edge edge) {
    return edges.contains(edge);
  }

  @Override
  public String toString() {
    return "Triangle{" +
        "nodes=" + nodes +
        '}';
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Triangle triangle = (Triangle) o;

    return edges.equals(triangle.edges);
  }

  @Override
  public int hashCode() {
    return edges.hashCode();
  }
}
