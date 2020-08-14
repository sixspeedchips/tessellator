package io.libsoft.tessel.controller;


import io.libsoft.tessel.model.Graph;
import io.libsoft.tessel.view.ModelViewer;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;

public class DrawController {

  @FXML
  public ModelViewer modelViewer;


  private boolean running;
  private GFXUpdater updater;

  @FXML
  private void initialize() {

    Graph graph = Graph.randomNodes(1000);

    graph.link();

    updater = new GFXUpdater();


    modelViewer.setGraph(graph);
    updater.start();

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
