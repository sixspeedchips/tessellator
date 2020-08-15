package io.libsoft.tessel.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Utils {

  private static final Gson printer;

  static {
    printer = new GsonBuilder().setPrettyPrinting().create();
  }


  public static Gson getPrinter() {
    return printer;
  }
}
