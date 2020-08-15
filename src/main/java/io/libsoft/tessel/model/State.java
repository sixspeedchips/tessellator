package io.libsoft.tessel.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Data;

@Data
public class State {

  private final List<Vertex> vertices = Collections.synchronizedList(new ArrayList<>());
  private final List<Edge> edges = Collections.synchronizedList(new ArrayList<>());


  public List<Vertex> getVertices() {
    return vertices;
  }

  public List<Edge> getEdges() {
    return edges;
  }
}
