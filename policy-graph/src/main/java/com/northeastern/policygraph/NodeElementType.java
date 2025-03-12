package com.northeastern.policygraph;

import java.util.ArrayList;
import java.util.List;

public enum NodeElementType {
  POLICY_CLASS,
  USER,
  USER_ATTRIBUTE,
  OBJECT,
  OBJECT_ATTRIBUTE;

  // the maximum number of node elements, e.g. ua7
  private static int number = PolicyGraph.maxNodeNumber;

  public static void resetNumber() {
    NodeElementType.number = PolicyGraph.maxNodeNumber;
  }

  public static NodeElement createNewNode(NodeElementType newNodeType) {
    number++;
    switch (newNodeType) {
      case POLICY_CLASS -> {
        return new PolicyClass("pc" + number);
      }
      case USER -> {
        return new User("u" + number);
      }
      case USER_ATTRIBUTE -> {
        return new UserAttribute("ua" + number);
      }
      case OBJECT -> {
        return new Ob("o" + number);
      }
      case OBJECT_ATTRIBUTE -> {
        return new ObjectAttribute("oa" + number);
      }
      default -> {
        return null;
      }
    }
  }

  public static List<NodeElement> getNodeList(NodeElementType targetNodeType,
      List<List<NodeElement>> initialNodes) {
    switch (targetNodeType) {
      case POLICY_CLASS -> {
        return initialNodes.get(0);
      }
      case USER -> {
        return initialNodes.get(1);
      }
      case USER_ATTRIBUTE -> {
        return initialNodes.get(2);
      }
      case OBJECT -> {
        return initialNodes.get(3);
      }
      case OBJECT_ATTRIBUTE -> {
        return initialNodes.get(4);
      }
      default -> {
        return null;
      }
    }
  }

  public static List<NodeElement> getNodeList(NodeElementType targetNodeType,
      PolicyGraph policyGraph) {
    List<List<NodeElement>> initialNodes = policyGraph.getNodeLists();
    return getNodeList(targetNodeType, initialNodes);
  }

  public static NodeElementType getNodeType(Integer randomIndex) {
    switch (randomIndex) {
      case 0 -> {
        return POLICY_CLASS;
      }
      case 1 -> {
        return USER;
      }
      case 2 -> {
        return USER_ATTRIBUTE;
      }
      case 3 -> {
        return OBJECT;
      }
      case 4 -> {
        return OBJECT_ATTRIBUTE;
      }
      default -> {
        return null;
      }
    }
  }

  public static ArrayList<Integer> getIndexRange(NodeElementType newNodeType) {
    ArrayList<Integer> indexRange = new ArrayList<>();
    switch (newNodeType) {
      case USER -> {
        indexRange.add(2);
      }
      case USER_ATTRIBUTE -> {
        indexRange.add(0);
        indexRange.add(2);
      }
      case OBJECT -> {
        indexRange.add(4);
      }
      case OBJECT_ATTRIBUTE -> {
        indexRange.add(0);
        indexRange.add(4);
      }
      default -> {}
    }
    return indexRange;
  }

  public String mutateForProlog() {
    switch (this) {
      case POLICY_CLASS -> {
        return "pc";
      }
      case USER -> {
        return "u";
      }
      case USER_ATTRIBUTE -> {
        return "ua";
      }
      case OBJECT -> {
        return "o";
      }
      case OBJECT_ATTRIBUTE -> {
        return "oa";
      }
      default -> {
        return null;
      }
    }
  }

  public String mutateForPML() {
    switch (this) {
      case POLICY_CLASS -> {
        return "policy class";
      }
      case USER -> {
        return "user";
      }
      case USER_ATTRIBUTE -> {
        return "user attribute";
      }
      case OBJECT -> {
        return "object";
      }
      case OBJECT_ATTRIBUTE -> {
        return "object attribute";
      }
      default -> {
        return null;
      }
    }
  }
}
