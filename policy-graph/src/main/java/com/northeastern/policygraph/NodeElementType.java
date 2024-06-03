package com.northeastern.policygraph;

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
