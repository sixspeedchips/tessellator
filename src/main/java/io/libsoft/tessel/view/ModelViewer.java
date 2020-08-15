package io.libsoft.tessel.view;

import io.libsoft.tessel.model.Edge;
import io.libsoft.tessel.model.Graph;
import io.libsoft.tessel.model.State;
import io.libsoft.tessel.model.Vertex;
import io.libsoft.tessel.util.Vars;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;

public class ModelViewer extends Pane {

  private final GraphicsContext gc;
  private Graph graph;


  private final Canvas canvas;
  private DoubleProperty scale = new SimpleDoubleProperty(1.0);
  private DoubleProperty mouseX = new SimpleDoubleProperty(0);
  private DoubleProperty mouseY = new SimpleDoubleProperty(0);


  public ModelViewer() {
    canvas = new Canvas();
    gc = canvas.getGraphicsContext2D();
    getChildren().add(canvas);
    canvas.setWidth(Vars.instance().getBounds());
    canvas.setHeight(Vars.instance().getBounds());

    gc.setFill(Color.BLACK);
    gc.fillRect(0, 0, getWidth(), getHeight());
    setOnMouseMoved(event -> {
      mouseX.setValue(event.getX());
      mouseY.setValue(event.getY());
    });
    setOnScroll(event -> {
      scale.setValue(Math.min(Math.max(scale.getValue() + (int) Math.signum(event.getDeltaY()), 1), 10));

      zoomIn();
    });
  }

  private void zoomIn() {
    Scale newScale = new Scale();
    newScale.setX(scale.getValue());
    newScale.setY(scale.getValue());
    newScale.setPivotX(mouseX.getValue());
    newScale.setPivotY(mouseY.getValue());

    Transform start = getTransforms().get(getTransforms().size() - 1);


  }

  public void setGraph(Graph graph) {
    this.graph = graph;
  }

  public void draw() {
    gc.clearRect(0, 0, getWidth(), getHeight());
    gc.setFill(Color.BLACK);
    gc.fillRect(0, 0, getWidth(), getHeight());
    State state = graph.getCurrentState();
    if (state == null) {
      return;
    }
    gc.setFill(Color.WHITE);
    gc.setStroke(Color.RED);
    int range = graph.getMaxConnections() - graph.getMinConnections();
    double step = 1d / range;

    for (Edge edge : state.getEdges()) {
      Color color = Color
          .color((edge.getStartConnectionSize() - graph.getMinConnections()) * step, 0, 0);
      gc.setStroke(color);
      gc.strokeLine(edge.getPoints()[0], edge.getPoints()[1], edge.getPoints()[2], edge.getPoints()[3]);

    }
    gc.setFill(Color.WHITE);
    gc.setStroke(Color.WHITE);

    if (Vars.instance().isShowNodes()) {
      for (Vertex vertex : state.getVertices()) {
        double width = vertex.getConnections() * Vars.instance().getNodeSize();
        gc.fillOval(vertex.getX() - width / 2d, vertex.getY() - width / 2d, width, width);
      }

    }

  }

}
