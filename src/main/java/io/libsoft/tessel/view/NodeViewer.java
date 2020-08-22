package io.libsoft.tessel.view;

import io.libsoft.tessel.model.Edge;
import io.libsoft.tessel.model.Graph;
import io.libsoft.tessel.model.Node;
import io.libsoft.tessel.model.State;
import io.libsoft.tessel.model.Triangle;
import io.libsoft.tessel.util.Props;
import io.libsoft.tessel.view.entity.Line;
import io.libsoft.tessel.view.entity.Vertex;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

public class NodeViewer extends Pane {

  private final GraphicsContext gc;
  private final Canvas canvas;
  private final DoubleProperty scale = new SimpleDoubleProperty(1.0);
  private final DoubleProperty mouseX = new SimpleDoubleProperty(0);
  private final DoubleProperty mouseY = new SimpleDoubleProperty(0);
  private Graph graph;


  public NodeViewer() {
    canvas = new Canvas();
    gc = canvas.getGraphicsContext2D();
    getChildren().add(canvas);
    canvas.setWidth(Props.get().getBounds() * 2);
    canvas.setHeight(Props.get().getBounds() * 2);

    gc.setFill(Color.BLACK);
    gc.fillRect(0, 0, getWidth(), getHeight());
    setOnMouseMoved(event -> {
      mouseX.setValue(event.getX());
      mouseY.setValue(event.getY());
    });
    setOnScroll(event -> {
      scale.setValue(Math.min(Math.max(scale.getValue() + Math.signum(event.getDeltaY()) * .01, 1), 10));

      zoomIn();
    });
  }

  private void zoomIn() {
    Scale newScale = new Scale();
    newScale.setX(scale.getValue());
    newScale.setY(scale.getValue());
    newScale.setPivotX(mouseX.getValue());
    newScale.setPivotY(mouseY.getValue());

    canvas.getTransforms().setAll(newScale);

  }

  public void setGraph(Graph graph) {
    this.graph = graph;
  }

  private void clear() {
    gc.setFill(Color.BLACK);
    gc.fillRect(0, 0, getWidth(), getHeight());
  }

  public void draw() {
    clear();
    State state = graph.getCurrentState();
    if (state == null) {
      return;
    }
    gc.setFill(Color.WHITE);
    gc.setStroke(Color.RED);
    int range = graph.getMaxConnections() - graph.getMinConnections();
    double step = 1d / range;

    for (Line LIne : state.getLines()) {
//      (edge.getStartConnectionSize() - graph.getMinConnections()) * step
      Color color = Color
          .color(1, 0, 0);
      gc.setStroke(color);
      gc.strokeLine(LIne.getPoints()[0], LIne.getPoints()[1], LIne.getPoints()[2], LIne.getPoints()[3]);

    }
    gc.setFill(Color.WHITE);
    gc.setStroke(Color.WHITE);

    if (Props.get().isShowNodes()) {
      for (Vertex vertex : state.getVertices()) {
        double width = vertex.getConnections() * Props.get().getNodeSize() + 5;
        gc.fillOval(vertex.getX() - width / 2d, vertex.getY() - width / 2d, width, width);
      }

    }

  }

  public void showTriangle() {
    clear();
    gc.setFill(Color.WHITE);
    State state = graph.getCurrentState();
    for (Triangle triangle : state.getTriangles()) {
      if (Props.get().isShowCircumCircles()) {
        gc.setStroke(Color.WHITE);
        double diameter = triangle.getRadius() * 2;
        gc.strokeOval(triangle.getCircumCenter().getX() - diameter / 2,
            triangle.getCircumCenter().getY() - diameter / 2, diameter,
            diameter);

      }

      gc.setStroke(Color.GREY);
      for (Edge edge : triangle.getEdges()) {
        Node n1 = edge.getTerminalsAsList().get(0);
        Node n2 = edge.getTerminalsAsList().get(1);
        gc.strokeLine(n1.getX(), n1.getY(), n2.getX(), n2.getY());
      }

    }
    gc.setFill(Color.RED);
    for (Vertex vertex : state.getVertices()) {
      gc.fillOval(vertex.getX() - Props.get().getNodeSize() / 2, vertex.getY() - Props.get().getNodeSize() / 2,
          Props.get().getNodeSize(), Props.get().getNodeSize());
    }
  }
}
