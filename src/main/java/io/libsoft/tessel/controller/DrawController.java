package io.libsoft.tessel.controller;


import io.libsoft.tessel.model.Graph;
import io.libsoft.tessel.util.Props;
import io.libsoft.tessel.view.NodeViewer;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class DrawController {

  @FXML
  public NodeViewer nodeViewer;
  @FXML
  public Button increment;


  private GFXUpdater updater;
  private boolean running;

  @FXML
  private void initialize() {


    Graph graph = Graph.randomNodes(Props.get().getNodes());
    updater = new GFXUpdater();
    nodeViewer.setGraph(graph);
    updater.start();
    increment.setDefaultButton(true);
    increment.setOnAction(event -> {
      graph.start();
    });

  }

  public void start() {

  }




  public void stop() {
  }

  private void updateView() {
    nodeViewer.showTriangle();
  }


  private class GFXUpdater extends AnimationTimer {

    @Override
    public void handle(long now) {
      updateView();
    }

  }

}
