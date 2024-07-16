package com.northeastern.policygraph;

import java.util.List;

public enum NodeElementType {
  POLICY_CLASS,
  USER,
  USER_ATTRIBUTE,
  OBJECT,
  OBJECT_ATTRIBUTE;

  public static NodeElement createNewNode(NodeElementType newNodeType) {
    switch (newNodeType) {
      case POLICY_CLASS -> {
        return new PolicyClass("pcNew");
      }
      case USER -> {
        return new User("uNew");
      }
      case USER_ATTRIBUTE -> {
        return new UserAttribute("uaNew");
      }
      case OBJECT -> {
        return new Ob("oNew");
      }
      case OBJECT_ATTRIBUTE -> {
        return new ObjectAttribute("oaNew");
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
