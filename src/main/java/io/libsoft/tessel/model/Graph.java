package io.libsoft.tessel.model;

import io.libsoft.tessel.util.Vars;
import java.util.Comparator;
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

  private static final double BOUNDS = 600;
  private static final Random random = new Random();
  private List<Node> nodes = new LinkedList<>();
  private int maxConnections;
  private int minConnections;
  private ExecutorService executors = Executors.newFixedThreadPool(5);
  private ScheduledExecutorService es = Executors.newScheduledThreadPool(1);

  private State currentState;
  private boolean running;

  public static Graph randomNodes(int num) {
    Graph graph = new Graph();
    for (int i = 0; i < num; i++) {
      double x = (random.nextGaussian() * Vars.instance().getBounds() / 7) + Vars.instance().getBounds() / 2;
      double y = (random.nextGaussian() * Vars.instance().getBounds() / 7) + Vars.instance().getBounds() / 2;
      graph.getNodes().add(new Node(x, y));
    }

    return graph;
  }

  public void start() {
//    es.scheduleAtFixedRate(this::update, 0, 16, TimeUnit.MILLISECONDS);
    es.submit(this::update);
  }

  public void update() {
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
        for (int p = 0; p < Vars.instance().getConnections(); p++) {
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
        Edge edge = new Edge(node, connection);
        state.getEdges().add(edge);
      }
      state.getVertices().add(new Vertex(node));
    }
    maxConnections = nodes.stream().max(Comparator.comparingInt(o -> o.getConnections().size()))
        .get().getConnections().size();

    minConnections = nodes.stream().min(Comparator.comparingInt(o -> o.getConnections().size()))
        .get().getConnections().size();
//      nodes.sort(Comparator.comparingInt(value -> value.getConnections().size()));
    state.getEdges().sort(Comparator.comparing(Edge::getStartConnectionSize));
    currentState = state;
    tasks.clear();
  }
}
