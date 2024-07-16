package com.northeastern.policygraph;

import static com.northeastern.policygraph.PolicyGraph.buildPMLString;
import static com.northeastern.policygraph.PolicyGraph.buildPrologString;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GraphRunner {

  private static String prologPath = "policy-graph/src/main/resources/translatePolicy.pl";
  private static String pmlPath = "policy-graph/src/main/resources/translatePolicy.pal";
  protected static List<AccessRight> allPermissions;
  protected static PolicyGraph initialGraph;

  public GraphRunner() {
  }

//  public void mutateNodeType(NodeElement oldNode, NodeElementType targetType) {
//    String sourceStringProlog = oldNode.toStringProlog();
//    String sourceStringPML = oldNode.toStringPML();
//    BufferedWriter writerProlog = null;
//    BufferedWriter writerPML = null;

  // for Prolog
//    try {
//      FileReader fileReaderProlog = new FileReader(prologPath);
//      BufferedReader bufferedReaderProlog = new BufferedReader(fileReaderProlog);
//
//      writerProlog = Files.newBufferedWriter(Paths.get(newPrologPath));
//
//      String line;
//      while ((line = bufferedReaderProlog.readLine()) != null) {
//        // Replace the old with target
//        if (line.equals(sourceStringProlog)) {
//          line = line.replace(oldNode.getTypeNameProlog() + "(", targetType.mutateForProlog() + "(");
//        }
//        writerProlog.write(line);
//      }
//      writerProlog.flush();
//    } catch (IOException e) {
//      throw new RuntimeException(e);
//    }
//  }

//  /**
//   * Mutate relation type between association and prohibition.
//   * Be called after Bookmark #2.
//   * @param source the source vertex of the edge
//   * @param target the target vertex of the edge
//   * @param g the graph before mutation
//   */
//  public void mutateEdgeType(NodeElement source, NodeElement target,
//      Graph<Element, Relation> g) {
//    Graph<Element, Relation> newGraph = g;
//    Element startP = newGraph.
//        vertexSet().stream().filter(pc -> pc instanceof PolicyClass).findAny()
//        .get();
//    Relation oldRelation = g.getEdge(source, target);
//    AccessRight[] ars = oldRelation.getAccessRights();
//    if (oldRelation instanceof Association) {
//      newGraph.removeEdge(oldRelation);
//      newGraph.addEdge(source, target, new Prohibition(ars));
//    }
//    else if (oldRelation instanceof Prohibition) {
//      newGraph.removeEdge(oldRelation);
//      newGraph.addEdge(source, target, new Association(ars));
//    }
//    else {
//    }
//
//    // for Prolog
//    try (
//        FileInputStream fis = new FileInputStream(prologPath);
//        FileOutputStream fos = new FileOutputStream(newPrologPath);
//    ) {
//      byte[] buffer = new byte[1024];
//      int length;
//
//      while ((length = fis.read(buffer)) > 0) {
//        fos.write(buffer, 0, length);
//      }
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//
//    depthTraverseAssignGraph(newGraph, startP, newPrologPath, newPMLPath);
//  }
//
//  /**
//   * Mutate the number of nodes by adding one node.
//   * Be called after Bookmark #2.
//   * @param newNodeType the type of the new node
//   * @param g the graph before mutation
//   */
//  public void mutateByAddNode(NodeElementType newNodeType,
//      Graph<Element, Relation> g) {
//    Graph<Element, Relation> newGraph = g;
//    Element startP = newGraph.
//        vertexSet().stream().filter(pc -> pc instanceof PolicyClass).findAny()
//        .get();
//    // Create a new Node
//    NodeElement newNode = NodeElementType.createNewNode(newNodeType);
//    newGraph.addVertex(newNode);
//    newGraph.addEdge(startP, newNode, new Assignment());
//
//    // for Prolog
//    try (
//        FileInputStream fis = new FileInputStream(prologPath);
//        FileOutputStream fos = new FileOutputStream(newPrologPath);
//    ) {
//      byte[] buffer = new byte[1024];
//      int length;
//
//      while ((length = fis.read(buffer)) > 0) {
//        fos.write(buffer, 0, length);
//      }
//      // Write new node declaration in prolog.
//      fos.write(newNode.toStringProlog().getBytes());
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//
//    depthTraverseAssignGraph(newGraph, startP, newPrologPath, newPMLPath);
//  }
//
//  public void mutateByRemoveNode(NodeElement removeNode,
//      Graph<Element, Relation> g) {
//    Graph<Element, Relation> newGraph = g;
//    newGraph.removeVertex(removeNode);
//    Element startP = newGraph.
//        vertexSet().stream().filter(pc -> pc instanceof PolicyClass).findAny()
//        .get();
//
//    // for Prolog
//    try (
//        FileInputStream fis = new FileInputStream(prologPath);
//        FileOutputStream fos = new FileOutputStream(newPrologPath);
//    ) {
//      byte[] buffer = new byte[1024];
//      int length;
//
//      while ((length = fis.read(buffer)) > 0) {
//        fos.write(buffer, 0, length);
//      }
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//
//    depthTraverseAssignGraph(newGraph, startP, newPrologPath, newPMLPath);
//
//  }
  public static void main(String[] args) {
    initialGraph = new PolicyGraph();
//    allPermissions = AccessRight.getAllPermissions();
    String proString = initialGraph.buildPrologString(
        initialGraph.getNodeLists(), initialGraph.getRelationLists());
    String pmlString = initialGraph.buildPMLString(
        initialGraph, initialGraph.getNodeLists());
    System.out.println("Initial Node List before creating a new node: " + initialGraph.getNodeLists());
    createFile(proString, prologPath);
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
