package com.northeastern.policygraph;

import static com.northeastern.policygraph.PolicyGraph.buildPMLPolicy;
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
    String pmlString = buildPMLPolicy(
        initialGraph, initialGraph.getNodeLists()).getPolicyString();
    System.out.println("Initial Node List: " + initialGraph.getNodeLists());
    String prologPath = "policy-graph/src/main/resources/translatePolicy.pl";
    createFile(proString, prologPath);
    String pmlPath = "policy-graph/src/main/resources/translatePolicy.pal";
    createFile(pmlString, pmlPath);

//    PolicyGraph mutatedGraph = Mutation.mutateAddNode(initialGraph, NodeElementType.USER_ATTRIBUTE,
//        NodeElementType.OBJECT);
//    List<String> proPMLString = new ArrayList<>();
//    proPMLString.add(buildPrologString(mutatedGraph.getNodeLists(), mutatedGraph.getRelationLists()));
//    proPMLString.add(buildPMLString(mutatedGraph, mutatedGraph.getNodeLists()));
//    createFile(proPMLString.get(0), prologPath);
//    createFile(proPMLString.get(1), pmlPath);
  }


  public static void createFile(String proString, String filePath) {
    BufferedWriter writerProlog = null;
    try {
      writerProlog = Files.newBufferedWriter(Paths.get(filePath));
    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.exit(1);
    }
    try {
      writerProlog.append(proString);
      writerProlog.flush();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.exit(1);
    }
  }
}
