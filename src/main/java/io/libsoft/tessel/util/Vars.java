package io.libsoft.tessel.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import lombok.Data;

@Data
public class Vars {


  private int nodes = 500;
  private int connections = 10;
  private double nodeSize = .001;
  private double bounds = 600;
  private boolean showNodes = true;

  private Vars() {

  }

  public static Vars instance() {
    return InstanceHolder.INSTANCE;
  }

  private static class InstanceHolder {

    private final static Vars INSTANCE;

    static {
      INSTANCE = new Vars();
      Properties prop = new Properties();
      try (InputStream inputStream = Vars.class.getClassLoader().getResourceAsStream("config.properties")) {
        prop.load(inputStream);
        System.out.println(prop);
        INSTANCE.nodes = Integer.parseInt(prop.getProperty("nodes", String.valueOf(INSTANCE.nodes)));
        INSTANCE.connections = Integer.parseInt(prop.getProperty("connections", String.valueOf(INSTANCE.connections)));
        INSTANCE.nodeSize = Double.parseDouble(prop.getProperty("node_size", String.valueOf(INSTANCE.nodeSize)));
        INSTANCE.bounds = Double.parseDouble(prop.getProperty("canvas_size", String.valueOf(INSTANCE.bounds)));
        INSTANCE.showNodes = Boolean.parseBoolean(prop.getProperty("show_nodes", String.valueOf(INSTANCE.showNodes)));

        System.out.println("Set properties from file.");
        System.out.println(Utils.getPrinter().toJson(INSTANCE));

      } catch (IOException | NullPointerException ignored) {
        ignored.printStackTrace();
        System.out.println("Failed to load properties.");
      }
    }

  }


}
