package io.libsoft.tessel.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Edge {

  private final Set<Node> terminals = new HashSet<>();

  public Edge(Node nodeOne, Node nodeTwo) {
    terminals.add(nodeOne);
    terminals.add(nodeTwo);
  }

  public Set<Node> getTerminals() {
    return terminals;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Edge edge = (Edge) o;

    return terminals != null ? terminals.equals(edge.terminals) : edge.terminals == null;
  }

  @Override
  public int hashCode() {
    return terminals != null ? terminals.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "Edge{" +
        "terminals=" + terminals +
        '}';
  }

  public List<Node> getTerminalsAsList() {
    return new LinkedList<>(terminals);
  }
}
