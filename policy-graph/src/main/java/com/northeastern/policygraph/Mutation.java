package com.northeastern.policygraph;

import static com.northeastern.policygraph.PolicyGraph.allPermissions;
import static com.northeastern.policygraph.PolicyGraph.isAncestor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Mutation {

  static final int NODE_TYPE_NUMBER = 5;
  private static Random random;
  private static int newNodeIndex;
  private static int targetNodeIndex;
  public static String curMutation;
  public static int skipNumber = 0;

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
  
  public static MutationStatus mutateAddNode(PolicyGraph initialGraph,
      NodeElementType newNodeType, NodeElementType targetNodeType) {
    List<List<NodeElement>> initialNodes = deepCopyNodes(initialGraph.getNodeLists());
    NodeElement newNode = createNewNode(newNodeType);
    initialGraph.addVertex(newNode, initialGraph);
    // Get the nodes list of assignedNodeType (before adding the new node)
    // and randomly select one node to be assigned
   List<NodeElement> assignedNodeList = NodeElementType.getNodeList(
        targetNodeType, initialNodes);
    NodeElement assignedNode = assignedNodeList.get(random.nextInt(assignedNodeList.size()));

    initialGraph.addEdge(assignedNode, newNode, new Assignment(), initialGraph);
    System.out.println(String.format("Mutate - Add Node!\n"
        + "Added Node: %s\nAdded assignment: (%s, %s)", newNode, newNode, assignedNode));
    curMutation = String.format("Add-Node_"
        + "%s_(%s, %s)", newNode, newNode, assignedNode);
    return new MutationStatus(initialGraph, true);
  }

  public static MutationStatus mutateAddNode(PolicyGraph initialGraph,
      NodeElementType newNodeType) {
    legalAssiTargets(newNodeType);
    return mutateAddNode(initialGraph, newNodeType, NodeElementType.getNodeType(targetNodeIndex));
  }

  public static MutationStatus mutateAddNode(PolicyGraph initialGraph) {
    newNodeIndex = random.nextInt(NODE_TYPE_NUMBER);
    // when create a new policy class
    if (newNodeIndex == 0) {
//      NodeElement newNode = createNewNode(NodeElementType.getNodeType(newNodeIndex));
//      initialGraph.addVertex(newNode, initialGraph);
//      System.out.println(String.format("Mutate - Add Node!\nAdded policy class: %s", newNode));
//      curMutation = String.format("Add-Node_%s", newNode);
//      return new MutationStatus(initialGraph, true);
      return mutateAddNode(initialGraph);
    } else {
      return mutateAddNode(initialGraph, NodeElementType.getNodeType(newNodeIndex));
    }
  }

  public static MutationStatus mutateAddAssignment(PolicyGraph initialGraph) {
    Integer sourceNodeIndex = random.nextInt(NODE_TYPE_NUMBER);
    Integer nodeNumber = initialGraph.getNodeLists().stream()
        .mapToInt(List::size)
        .sum();
    Integer maxSkipNumber = nodeNumber * (nodeNumber - 1);
    if (skipNumber <= maxSkipNumber) {
      if (sourceNodeIndex == 0) { // when add assignment from policy class to others, skip！
        skipNumber++;
        return mutateAddAssignment(initialGraph);
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
          skipNumber++;
          return mutateAddAssignment(initialGraph);
        } else if (isAncestor(initialGraph, sourceNode, targetNode)) {
          skipNumber++;
          return mutateAddAssignment(initialGraph);
        } else if (isAncestor(initialGraph, targetNode, sourceNode)) {
          skipNumber++;
          return mutateAddAssignment(initialGraph);
        } else {
          initialGraph.addEdge(targetNode, sourceNode, new Assignment(), initialGraph);
          System.out.println(String.format("Mutate - Add Assignment!\n"
              + "Added assignment: (%s, %s)", sourceNode, targetNode));
          curMutation = String.format("Add-Assignment_"
              + "(%s, %s)", sourceNode, targetNode);
          return new MutationStatus(initialGraph, true);
        }
      }
    } else {
      System.out.println("When no more assignments!!!!!!!!!!");
      return new MutationStatus(initialGraph, false);
    }
  }

  public static MutationStatus mutateAddProhibition(PolicyGraph initialGraph) {
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
      mutateAddProhibition(initialGraph);
    } else {
      String arString = allPermissions.get(random.nextInt(allPermissions.size()));
      AccessRight[] ar = new AccessRight[]{new AccessRight(arString)};
      initialGraph.addEdge(targetNode, sourceNode, new Prohibition(ar), initialGraph);
      System.out.println(String.format("Mutate - Add Prohibition!\n"
          + "Added prohibition: (%s, %s, %s)", sourceNode, targetNode, Arrays.toString(ar)));
      curMutation = String.format("Add-Prohibition_"
          + "(%s, %s, %s)", sourceNode, targetNode, Arrays.toString(ar));
    }
    return new MutationStatus(initialGraph, true);
  }

  public static MutationStatus mutateAddAssociation(PolicyGraph initialGraph) {
    List<NodeElement> sourceList = NodeElementType.getNodeList(NodeElementType.getNodeType(2), initialGraph);
    ArrayList<Integer> targetOptions = new ArrayList<>();
    targetOptions.add(2);
    targetOptions.add(3);
    targetOptions.add(4);
    Integer targetNodeIndex = targetOptions.get(random.nextInt(targetOptions.size()));
    List<NodeElement> targetList = NodeElementType.getNodeList(NodeElementType.getNodeType(targetNodeIndex), initialGraph);
    NodeElement sourceNode = null;
    NodeElement targetNode = null;
    sourceNode = sourceList.get(random.nextInt(sourceList.size()));
    targetNode = targetList.get(random.nextInt(targetList.size()));
    if (sourceNode == targetNode) {
      mutateAddAssociation(initialGraph);
    }
    else {
      String arString = allPermissions.get(random.nextInt(allPermissions.size()));
      AccessRight[] ar = new AccessRight[]{new AccessRight(arString)};
      initialGraph.addEdge(targetNode, sourceNode, new Association(ar), initialGraph);
      System.out.println(String.format("Mutate - Add Association!\n"
          + "Added Association: (%s, %s, %s)", sourceNode, targetNode, Arrays.toString(ar)));
      curMutation = String.format("Add-Association_"
          + "(%s, %s, %s)", sourceNode, targetNode, Arrays.toString(ar));
    }
    return new MutationStatus(initialGraph, true);
  }

  // TODO Only delete all of the assignments emanating from the node?
  // The policy element in question must not be involved in any defined relation.
  // C.7 Relation Rescindment Commands:
  // the preconditions must also maintain certain model properties
  public static List<String> mutateDeleteUA(PolicyGraph initialGraph) {
    List<String> proPMLString = new ArrayList<>();
    return proPMLString;
  }

  public static String getCurMutation() {
    return curMutation;
  }
}
