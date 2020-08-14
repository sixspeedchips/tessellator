package io.libsoft.tessel.view;

import io.libsoft.tessel.model.Graph;
import io.libsoft.tessel.model.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ModelViewer extends Canvas {

  private final GraphicsContext gc;
  private Graph graph;

  public ModelViewer() {
    gc = getGraphicsContext2D();
    setWidth(600);
    setHeight(600);

    gc.setFill(Color.BLACK);
    gc.fillRect(0, 0, getWidth(), getHeight());
  }

  public void setGraph(Graph graph) {
    this.graph = graph;

    int range = graph.getMaxConnections() - graph.getMinConnections();
    double step = 1d / range;

    for (Node node : graph.getNodes()) {
      Color color = Color
          .color((node.getConnections().size() - graph.getMinConnections()) * step, 0, 0);
      gc.setStroke(color);
      for (Node connection : node.getConnections()) {
        gc.strokeLine(connection.getX(), connection.getY(), node.getX(), node.getY());
      }
    }

  }

  public void draw() {
  }


}
