package io.libsoft.tessel.model;

import io.libsoft.tessel.view.entity.Line;
import io.libsoft.tessel.view.entity.Vertex;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Data;

@Data
public class State {

  private final List<Vertex> vertices = Collections.synchronizedList(new ArrayList<>());
  private final List<Line> Lines = Collections.synchronizedList(new ArrayList<>());
  private final List<Triangle> triangles = Collections.synchronizedList(new ArrayList<>());

  public void addNodes(List<Node> nodes) {
    this.vertices.addAll(Vertex.from(nodes));
  }


  public void addTriangles(List<Triangle> triangles) {
    this.triangles.addAll(triangles);
  }
}
