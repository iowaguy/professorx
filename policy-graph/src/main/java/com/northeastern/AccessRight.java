package com.northeastern;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.DepthFirstIterator;

public enum AccessRight {

  PERMISSION0,
  PERMISSION1,
  PERMISSION2,
  PERMISSION3,
  PERMISSION4;

  public static void writeAccessRights() {
    // create a writer
    BufferedWriter writerProlog = null;
    BufferedWriter writerPML = null;
    try {
      writerProlog = Files.newBufferedWriter(
          Paths.get("policy-graph/src/main/resources/translatePolicy.pl"));
      writerPML = Files.newBufferedWriter(
          Paths.get("policy-graph/src/main/resources/translatePolicy.pal"));
    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.exit(1);
    }
    Iterator<AccessRight> accessRightIterator = Arrays.stream(values()).iterator();
    List<String> arPML = new ArrayList<>();
    while (accessRightIterator.hasNext()) {
      try {
        String arToString = accessRightIterator.next().toString().toLowerCase();
        writerProlog.write(String.format("ar(%1$s).", arToString) + System.lineSeparator());
        arPML.add('\'' + arToString + '\'');
      } catch (IOException e) {
        System.out.println(e.getMessage());
        System.exit(1);
      }
    }
    try {
      String arsPML = String.join(", ", arPML);
      arsPML += ";";
      writerPML.write(String.format("set resource access rights %1$s", arsPML) + System.lineSeparator());
    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.exit(1);
    }
    try {
      writerProlog.flush();
      writerPML.flush();
    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.exit(1);
    }
  }
}
