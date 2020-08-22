package io.libsoft.tessel.model;

import io.libsoft.tessel.util.Props;
import io.libsoft.tessel.view.entity.Line;
import io.libsoft.tessel.view.entity.Vertex;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import lombok.Data;

@Data
public class Graph {

  private static final Random random = new Random();
  private List<Node> nodes = new LinkedList<>();
  private List<Node> processedNodes = new LinkedList<>();
  private List<Triangle> triangles = new LinkedList<>();
  private int maxConnections;
  private int minConnections;
  private ExecutorService executors = Executors.newFixedThreadPool(5);
  private ScheduledExecutorService es = Executors.newScheduledThreadPool(1);


  private State currentState;
  private boolean running;
  private int index;




  public void start() {
    es.submit(() -> bowyerWatson(nodes.get(index++)));
  }

  public void bowyerWatson(Node node) {
    processedNodes.add(node);

    List<Triangle> badTriangles = new LinkedList<>();
    badTriangles.clear();
    for (Triangle triangle : triangles) {
      boolean contains = triangle.containsNode(node);
      System.out.println(contains);
      if (contains) {
        badTriangles.add(triangle);
      }
    }
    System.out.println("\n\n\n");
    HashSet<Edge> edgeList = new HashSet<>();
    Triangle curr;

    for (int i = 0; i < badTriangles.size(); i++) {
      curr = badTriangles.remove(i);
      for (Edge edge : curr.getEdges()) {
        edgeList.add(edge);
      }
      badTriangles.add(i, curr);
    }
    triangles.removeAll(badTriangles);
    triangles.addAll(Triangle.fromPolygon(edgeList, node));

    State state = new State();
    state.addNodes(processedNodes);
    state.addTriangles(triangles);
    currentState = state;
  }

  public void link() {
    running = true;
    List<Callable<Void>> tasks = new LinkedList<>();
    for (Node node : nodes) {
      node.reset();
      node.update();
    }

    for (int i = 0; i < nodes.size(); i++) {
      Node curr = nodes.remove(i);
      curr.getClosestNeighbors().addAll(nodes);
      nodes.add(i, curr);
    }

    State state = new State();
    for (Node node : nodes) {
      tasks.add(() -> {
        node.getClosestNeighbors().sort(Comparator.comparingDouble(node::getNorm));
        for (int p = 0; p < Props.get().getConnections(); p++) {
          Node n = node.getClosestNeighbors().get(p);
          n.getConnections().add(node);
          node.getConnections().add(n);
        }
        return null;
      });
    }
    try {
      List<Future<Void>> edges = executors.invokeAll(tasks);
    } catch (InterruptedException ignored) {
    }

    for (Node node : nodes) {
      for (Node connection : node.getConnections()) {
        Line LIne = new Line(node, connection);
        state.getLines().add(LIne);
      }
      state.getVertices().add(new Vertex(node));
    }
    maxConnections = nodes.stream().max(Comparator.comparingInt(o -> o.getConnections().size()))
        .get().getConnections().size();

    minConnections = nodes.stream().min(Comparator.comparingInt(o -> o.getConnections().size()))
        .get().getConnections().size();
    state.getLines().sort(Comparator.comparing(Line::getStartConnectionSize));
    currentState = state;
    tasks.clear();
  }



  public static Graph randomNodes(int num) {
    Graph graph = new Graph();
    graph.currentState = new State();
    for (int i = 0; i < num; i++) {
      double x = (random.nextGaussian() * Props.get().getBounds() / 7) + Props.get().getBounds() / 2;
      double y = (random.nextGaussian() * Props.get().getBounds() / 7) + Props.get().getBounds() / 2;
      Node n = new Node(x, y);
      graph.nodes.add(n);
      graph.currentState.getVertices().add(new Vertex(n));
    }
    Triangle boundingTriangle = Triangle.bounding();
    graph.triangles.add(boundingTriangle);
    graph.currentState.getTriangles().add(boundingTriangle);
    return graph;
  }
}
