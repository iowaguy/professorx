package com.northeastern.policygraph;

import static com.northeastern.policygraph.PolicyGraph.allPermissions;
import static com.northeastern.policygraph.PolicyGraph.buildPrologString;
import static com.northeastern.policygraph.PolicyGraph.buildPMLString;
import static com.northeastern.policygraph.PolicyGraph.isAncestor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Mutation {

  static final int NODE_TYPE_NUMBER = 5;
  private static Random random;
  private static int newNodeIndex;
  private static int targetNodeIndex;

  public Mutation(Long randomSeed) {
    random = new Random(randomSeed);
  }

  public static NodeElement createNewNode(NodeElementType newNodeType) {
    NodeElement newNode = NodeElementType.createNewNode(newNodeType);
    System.out.println("Added new node: " + newNode);
    return newNode;
  }

  private static List<List<NodeElement>> deepCopyNodes(List<List<NodeElement>> nodeLists) {
    List<List<NodeElement>> newNodeList = new ArrayList<>();
    for (List<NodeElement> nodeList : nodeLists) {
      List<NodeElement> copiedNodeList = new ArrayList<>(nodeList);
      newNodeList.add(copiedNodeList);
    }
    return newNodeList;
  }
  
  private static void legalAssiTargets(NodeElementType sourceNodeType) {
    ArrayList<Integer> targetNodesList = NodeElementType.getIndexRange(sourceNodeType);
    targetNodeIndex = targetNodesList.get(random.nextInt(targetNodesList.size()));
  }
  
  public static PolicyGraph mutateAddNode(PolicyGraph initialGraph,
      NodeElementType newNodeType, NodeElementType targetNodeType) {
    PolicyGraph mutatedGraph = (PolicyGraph) initialGraph.clone();
    List<List<NodeElement>> initialNodes = deepCopyNodes(initialGraph.getNodeLists());
    NodeElement newNode = createNewNode(newNodeType);
    mutatedGraph.addVertex(newNode);
    // Get the nodes list of assignedNodeType (before adding the new node)
    // and randomly select one node to be assigned
   List<NodeElement> assignedNodeList = NodeElementType.getNodeList(
        targetNodeType, initialNodes);
    NodeElement assignedNode = null;
    if (!assignedNodeList.isEmpty()) {
      assignedNode = assignedNodeList.get(random.nextInt(assignedNodeList.size()));
    }
    mutatedGraph.addEdge(assignedNode, newNode, new Assignment());
    System.out.println(String.format("Mutate - Add Node!\n"
        + "Added Node: %s\nAdded assignment: (%s, %s)", newNode, newNode, assignedNode));
//    // get the nodeLists and relationLists of mutatedGraph
//    List<List<NodeElement>> mutatedNodeLists = mutatedGraph.getNodeLists();
//    System.out.println("Node list after adding a vertex: " + mutatedNodeLists);
    return mutatedGraph;
  }

  public static PolicyGraph mutateAddNode(PolicyGraph initialGraph,
      NodeElementType newNodeType) {
    legalAssiTargets(newNodeType);
    return mutateAddNode(initialGraph, newNodeType, NodeElementType.getNodeType(targetNodeIndex));
  }

  public static PolicyGraph mutateAddNode(PolicyGraph initialGraph) {
    newNodeIndex = random.nextInt(NODE_TYPE_NUMBER);
    // when create a new policy class
    if (newNodeIndex == 0) {
      PolicyGraph mutatedGraph = (PolicyGraph) initialGraph.clone();
      NodeElement newNode = createNewNode(NodeElementType.getNodeType(newNodeIndex));
      mutatedGraph.addVertex(newNode);
      System.out.println(String.format("Mutate - Add Node!\nAdded policy class: %s", newNode));
      return mutatedGraph;
    } else {
      return mutateAddNode(initialGraph, NodeElementType.getNodeType(newNodeIndex));
    }
  }

  public static PolicyGraph mutateAddAssignment(PolicyGraph initialGraph) {
    Integer sourceNodeIndex = random.nextInt(NODE_TYPE_NUMBER);
    if (sourceNodeIndex == 0) {
      return initialGraph;
    } else {
      legalAssiTargets(NodeElementType.getNodeType(sourceNodeIndex));
      List<NodeElement> sourceList = NodeElementType.getNodeList(
          NodeElementType.getNodeType(sourceNodeIndex), initialGraph);
      List<NodeElement> targetList = NodeElementType.getNodeList(
          NodeElementType.getNodeType(targetNodeIndex), initialGraph);
      NodeElement sourceNode = null;
      NodeElement targetNode = null;
      if (!sourceList.isEmpty() && !targetList.isEmpty()) {
        sourceNode = sourceList.get(random.nextInt(sourceList.size()));
        targetNode = targetList.get(random.nextInt(targetList.size()));
      }
      if (sourceNode == targetNode) {
        System.out.println(String.format("Mutate - Add Assignment!\n"
            + "Source node and target node are the same. Skip"));
      } else if (isAncestor(initialGraph, sourceNode, targetNode)) {
        System.out.println(String.format("Mutate - Add Assignment!\n"
            + "Assignment (%s, %s) exists. Skip", sourceNode, targetNode));
        mutateAddAssignment(initialGraph);
      } else {
        initialGraph.addEdge(targetNode, sourceNode, new Assignment());
        System.out.println(String.format("Mutate - Add Assignment!\n"
            + "Added assignment: (%s, %s)", sourceNode, targetNode));
      }
    }
    return initialGraph;
  }

  public static PolicyGraph mutateAddProhibition(PolicyGraph initialGraph) {
    ArrayList<Integer> sourceOptions = new ArrayList<>();
    sourceOptions.add(1);
    sourceOptions.add(2);
    Integer sourceNodeIndex = sourceOptions.get(random.nextInt(sourceOptions.size()));
    List<NodeElement> sourceList = NodeElementType.getNodeList(
        NodeElementType.getNodeType(sourceNodeIndex), initialGraph);
    ArrayList<Integer> targetOptions = new ArrayList<>();
    targetOptions.add(2);
    targetOptions.add(3);
    targetOptions.add(4);
    Integer targetNodeIndex = targetOptions.get(random.nextInt(targetOptions.size()));
    List<NodeElement> targetList = NodeElementType.getNodeList(
        NodeElementType.getNodeType(targetNodeIndex), initialGraph);
    NodeElement sourceNode = null;
    NodeElement targetNode = null;
    sourceNode = sourceList.get(random.nextInt(sourceList.size()));
    targetNode = targetList.get(random.nextInt(targetList.size()));
    if (sourceNode == targetNode) {
      System.out.println(String.format("Mutate - Add Prohibition!\n"
          + "Source node and target node are the same. Skip"));
    } else {
      AccessRight[] ar = new AccessRight[]{
          allPermissions.get(random.nextInt(allPermissions.size()))};
      initialGraph.addEdge(targetNode, sourceNode, new Prohibition(ar));
      System.out.println(String.format("Mutate - Add Prohibition!\n"
          + "Added prohibition: (%s, %s, %s)", sourceNode, targetNode, Arrays.toString(ar)));
    }
    return initialGraph;
  }

  // TODO Only delete all of the assignments emanating from the node?
  // The policy element in question must not be involved in any defined relation.
  // C.7 Relation Rescindment Commands:
  // the preconditions must also maintain certain model properties
  public static List<String> mutateDeleteUA(PolicyGraph initialGraph) {
    List<String> proPMLString = new ArrayList<>();
    return proPMLString;
  }
}
