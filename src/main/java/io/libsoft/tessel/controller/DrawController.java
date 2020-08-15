package io.libsoft.tessel.controller;


import io.libsoft.tessel.model.Graph;
import io.libsoft.tessel.util.Vars;
import io.libsoft.tessel.view.CannyViewer;
import io.libsoft.tessel.view.ModelViewer;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;

public class DrawController {

  @FXML
  public ModelViewer modelViewer;

  @FXML
  public CannyViewer cannyViewer;

  private GFXUpdater updater;
  private boolean running;

  @FXML
  private void initialize() {

    Graph graph = Graph.randomNodes(Vars.instance().getNodes());
    updater = new GFXUpdater();
    modelViewer.setGraph(graph);
    updater.start();
    graph.start();

  }

  public void start() {

  }




  public void stop() {
  }

  private void updateView() {
    modelViewer.draw();
  }


  private class GFXUpdater extends AnimationTimer {

    @Override
    public void handle(long now) {
      updateView();
    }

  }

}
