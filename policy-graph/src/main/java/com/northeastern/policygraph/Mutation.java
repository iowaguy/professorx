package com.northeastern.policygraph;

import static com.northeastern.policygraph.PolicyGraph.allPermissions;
import static com.northeastern.policygraph.PolicyGraph.buildPrologString;
import static com.northeastern.policygraph.PolicyGraph.buildPMLString;

import java.util.ArrayList;
import java.util.List;

public class Mutation {

  public Mutation() {
  }

  // TODO public static List<String> mutateAddNode(PolicyGraph initialGraph,
  //  NodeElementType newNodeType, NodeElementType targetNodeType)

  /**
   *
   * @param initialGraph
   * @return String array consisting of the Prolog string and PML string
   */
  public static List<String> mutateAddUA(PolicyGraph initialGraph) {
    List<String> proPMLString = new ArrayList<>();
    // add a new UserAttribute
    UserAttribute addUA = new UserAttribute("uaX");
    PolicyGraph mutateGraph = initialGraph;
    mutateGraph.addVertex(addUA);

    // get the nodeLists and relationLists of initialGraph
    List<List<NodeElement>> mutateNodeLists = mutateGraph.getNodeLists();
    System.out.println(mutateNodeLists.get(2));
    List<List<Relation>> mutateRelationLists = mutateGraph.getRelationLists();

//    mutateNodeLists.get(2).add(addUA);
    // TODO add uaX to a list of existing ua?
    // assign uaX to ua3
    for (NodeElement node: mutateNodeLists.get(2)) {
      if (node.toString().equals("ua3")) {
        mutateGraph.addEdge(node, addUA, new Assignment());
//        mutateRelationLists.get(0).add(Assignment.getAllAssignments().
//            get(Assignment.getAllAssignments().size() - 1));
        break;
      }
    }

    proPMLString.add(buildPrologString(mutateNodeLists, mutateRelationLists));
    proPMLString.add(buildPMLString(mutateGraph, mutateNodeLists));
    System.out.println(proPMLString.get(1));
    return proPMLString;
  }

  public static List<String> mutateAddU(PolicyGraph initialGraph) {
    List<String> proPMLString = new ArrayList<>();
    return proPMLString;
  }

  public static List<String> mutateAddOb(PolicyGraph initialGraph) {
    List<String> proPMLString = new ArrayList<>();
    return proPMLString;
  }
  public static List<String> mutateAddOa(PolicyGraph initialGraph) {
    List<String> proPMLString = new ArrayList<>();
    return proPMLString;
  }

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
