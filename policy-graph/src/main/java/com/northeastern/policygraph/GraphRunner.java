package com.northeastern.policygraph;

import static com.northeastern.policygraph.PolicyGraph.buildPMLPolicy;
import static com.northeastern.policygraph.PolicyGraph.buildPMLPolicy22;
import static com.northeastern.policygraph.PolicyGraph.buildPrologPolicy;

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
//    allPermissions = AccessRight.getAllPermissions();
    String proString = buildPrologPolicy(
        initialGraph.getNodeLists(), initialGraph.getRelationLists()).getPolicyString();
    String pmlString = buildPMLPolicy(initialGraph, initialGraph.getNodeLists()).getPolicyString();
//    String pmlString = buildPMLPolicy22(initialGraph, initialGraph.getNodeLists()).getPolicyString();
    System.out.println("Initial Node List: " + initialGraph.getNodeLists());
    String prologPath = "policy-graph/src/main/resources/translatePolicy.pl";
    createFile(proString, prologPath);
    String pmlPath = "policy-graph/src/main/resources/translatePolicy.pal";
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
