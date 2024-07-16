package com.northeastern.policygraph;

import static com.northeastern.policygraph.PolicyGraph.allPermissions;
import static com.northeastern.policygraph.PolicyGraph.buildPrologString;
import static com.northeastern.policygraph.PolicyGraph.buildPMLString;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mutation {

  static final int NODE_TYPE_NUMBER = 5;

  public Mutation() {
  }

//  public static List<String> mutateAddNode(PolicyGraph initialGraph,
//      NodeElementType newNodeType, NodeElementType targetNodeType) {
//    List<String> proPMLString = new ArrayList<>();
//    List<List<NodeElement>> initialNodes = initialGraph.getNodeLists();
//
//    // Create the new node
//    NodeElement newNode = NodeElementType.createNewNode(newNodeType);
//    System.out.println("Initial Node list after creating a new node: " + initialGraph.getNodeLists());
//
//    // Get the nodes list of assignedNodeType
//    // and randomly select one node to be assigned
//    List<NodeElement> assignedNodeList = NodeElementType.getNodeList(
//        targetNodeType, initialNodes);
//    Random random = new Random();
//    NodeElement assignedNode = null;
//    if (!assignedNodeList.isEmpty()) {
//      assignedNode = assignedNodeList.get(random.nextInt(assignedNodeList.size()));
//    }
//    System.out.println("Randomly selected node: " + assignedNode);
//    initialGraph.addVertex(newNode);
//    initialGraph.addEdge(assignedNode, newNode, new Assignment());
//
//    // get the nodeLists and relationLists of mutatedGraph
//    List<List<NodeElement>> mutatedNodeLists = initialGraph.getNodeLists();
//    System.out.println("Node list after adding a vertex: " + mutatedNodeLists);
//    List<List<Relation>> mutatedRelationLists = initialGraph.getRelationLists();
//
////    proPMLString.add(buildPrologString(mutatedNodeLists, mutatedRelationLists));
////    proPMLString.add(buildPMLString(initialGraph, mutatedNodeLists));
////    System.out.println(proPMLString.get(1));
//    System.out.println(proPMLString.get(0));
//    return proPMLString;
//  }
  public static PolicyGraph mutateAddNode(PolicyGraph initialGraph,
      NodeElementType newNodeType, NodeElementType targetNodeType) {
//    List<String> proPMLString = new ArrayList<>();
    List<List<NodeElement>> initialNodes = initialGraph.getNodeLists();

    // Create the new node
    NodeElement newNode = NodeElementType.createNewNode(newNodeType);
    System.out.println("Node list after creating a new node: " + initialGraph.getNodeLists());

    // Get the nodes list of assignedNodeType
    // and randomly select one node to be assigned
    List<NodeElement> assignedNodeList = NodeElementType.getNodeList(
        targetNodeType, initialNodes);
    Random random = new Random();
    NodeElement assignedNode = null;
    if (!assignedNodeList.isEmpty()) {
      assignedNode = assignedNodeList.get(random.nextInt(assignedNodeList.size()));
    }
    System.out.println("Randomly selected node: " + assignedNode);
    initialGraph.addVertex(newNode);
    initialGraph.addEdge(assignedNode, newNode, new Assignment());

    // get the nodeLists and relationLists of mutatedGraph
    List<List<NodeElement>> mutatedNodeLists = initialGraph.getNodeLists();
    System.out.println("Node list after adding a vertex: " + mutatedNodeLists);
    List<List<Relation>> mutatedRelationLists = initialGraph.getRelationLists();

//    proPMLString.add(buildPrologString(mutatedNodeLists, mutatedRelationLists));
//    proPMLString.add(buildPMLString(initialGraph, mutatedNodeLists));
//    System.out.println(proPMLString.get(1));
//    System.out.println(proPMLString.get(0));
    return initialGraph;
  }

  public static PolicyGraph mutateAddNode(PolicyGraph initialGraph,
      NodeElementType newNodeType) {
    Random random = new Random();
    int randomIndex = random.nextInt(NODE_TYPE_NUMBER);
    return mutateAddNode(initialGraph, newNodeType, NodeElementType.getNodeType(randomIndex));
  }
//  public static List<String> mutateAddNode(PolicyGraph initialGraph,
//      NodeElementType newNodeType) {
//    Random random = new Random();
//    int randomIndex = random.nextInt(NODE_TYPE_NUMBER);
//    return mutateAddNode(initialGraph, newNodeType, NodeElementType.getNodeType(randomIndex));
//  }

  // TODO public static List<String> mutateAddAssignment(PolicyGraph initialGraph,
  //  NodeElementType sourceNodeType, NodeElementType targetNodeType)
  public static List<String> mutateAddUaAssignment(PolicyGraph initialGraph) {
    List<String> proPMLString = new ArrayList<>();
    PolicyGraph mutateGraph = initialGraph;
    // get UserAttribute list
    List<NodeElement> userAttributes = mutateGraph.getNodeLists().get(2);
    mutateGraph.addEdge(userAttributes.get(3), userAttributes.get(2),
        new Assignment());
    proPMLString.add(buildPrologString(mutateGraph.getNodeLists(),
        mutateGraph.getRelationLists()));
    proPMLString.add(buildPMLString(mutateGraph, mutateGraph.getNodeLists()));
    return proPMLString;
  }

  public static List<String> mutateAddAssociation(PolicyGraph initialGraph) {
    List<String> proPMLString = new ArrayList<>();
    PolicyGraph mutateGraph = initialGraph;
    // get UserAttribute, ObjectAttribute list
    List<NodeElement> userAttributes = mutateGraph.getNodeLists().get(2);
    List<NodeElement> objectAttributes = mutateGraph.getNodeLists().get(4);
    mutateGraph.addEdge(objectAttributes.get(4), userAttributes.get(2),
        new Association(new AccessRight[]{allPermissions.get(3)}));
    proPMLString.add(buildPrologString(mutateGraph.getNodeLists(),
        mutateGraph.getRelationLists()));
    proPMLString.add(buildPMLString(mutateGraph, mutateGraph.getNodeLists()));
    return proPMLString;
  }

  public static List<String> mutateAddProhibition(PolicyGraph initialGraph) {
    List<String> proPMLString = new ArrayList<>();
    PolicyGraph mutateGraph = initialGraph;
    // get UserAttribute, ObjectAttribute list
    List<NodeElement> users = mutateGraph.getNodeLists().get(1);
    List<NodeElement> objectAttributes = mutateGraph.getNodeLists().get(4);
    mutateGraph.addEdge(objectAttributes.get(6), users.get(0),
        new Prohibition(new AccessRight[]{allPermissions.get(4)}));
    proPMLString.add(buildPrologString(mutateGraph.getNodeLists(),
        mutateGraph.getRelationLists()));
    proPMLString.add(buildPMLString(mutateGraph, mutateGraph.getNodeLists()));
    System.out.println(proPMLString.get(0));
    return proPMLString;
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
