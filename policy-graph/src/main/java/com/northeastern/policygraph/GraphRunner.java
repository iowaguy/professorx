package com.northeastern.policygraph;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class GraphRunner {

  protected static List<AccessRight> allPermissions;
  protected static PolicyGraph initialGraph;

  public GraphRunner() {
  }

  public static void main(String[] args) {
    initialGraph = new PolicyGraph();
    String proString = initialGraph.buildPrologPolicy().getPolicyString();
    String prologPath = "policy-graph/src/main/resources/translatePolicy.pl";
    // Build the latest version
    String pmlString = initialGraph.buildPMLPolicy().getPolicyString();
    String pmlPath = "policy-graph/src/main/resources/translatePolicy.pal";
//     Build 111822 version
//    String pmlString = initialGraph.buildPMLPolicy22().getPolicyString();
//    String pmlPath = "policy-graph/src/main/resources/translatePolicy_111822.pal";
//    System.out.println("Initial Node List: " + initialGraph.getNodeLists());
    createFile(proString, prologPath);
    createFile(pmlString, pmlPath);
  }


  public static void createFile(String policyString, String filePath) {
    BufferedWriter writerProlog = null;
    try {
      writerProlog = Files.newBufferedWriter(Paths.get(filePath));
    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.exit(1);
    }
    try {
      writerProlog.append(policyString);
      writerProlog.flush();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.exit(1);
    }
  }
}
