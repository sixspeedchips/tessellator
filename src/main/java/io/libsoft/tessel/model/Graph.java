package io.libsoft.tessel.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import lombok.Data;

@Data
public class Graph {

  private static final double BOUNDS = 600;
  private static final Random random = new Random();
  private List<Node> nodes = new LinkedList<>();
  private int maxConnections;
  private int minConnections;


  public static Graph randomNodes(int num) {
    Graph graph = new Graph();
    for (int i = 0; i < num; i++) {
      double x = (random.nextGaussian() * 120) + 300;
      double y = (random.nextGaussian() * 120) + 300;
//      double x = random.nextDouble() * BOUNDS;
//      double y = random.nextDouble() * BOUNDS;
      graph.getNodes().add(new Node(x, y));
    }

    return graph;
  }

  public void link() {
    maxConnections = 0;
    for (int k = 0; k < 10; k++) {

      for (int i = 0; i < nodes.size(); i++) {

        Node closestNeighbor = null;

        double shortestDistance = Double.POSITIVE_INFINITY;
        Node curr = nodes.remove(i);

//      for (int j = i + 1; j < nodes.size(); j++) {
//        Node node = nodes.get(j);
        for (Node node : nodes) {

          double dist = curr.getNorm(node);

          if (dist < shortestDistance && !curr.getConnections().contains(node)) {
            closestNeighbor = node;
            shortestDistance = dist;
          }

        }
        nodes.add(i, curr);
        curr.getConnections().add(closestNeighbor);
        closestNeighbor.getConnections().add(curr);

      }
      maxConnections = nodes.stream().max(Comparator.comparingInt(o -> o.getConnections().size()))
          .get().getConnections().size();

      minConnections = nodes.stream().min(Comparator.comparingInt(o -> o.getConnections().size()))
          .get().getConnections().size();


      nodes.sort(Comparator.comparingInt(value -> value.getConnections().size()));
//      Collections.reverse(nodes);

    }


  }
}
