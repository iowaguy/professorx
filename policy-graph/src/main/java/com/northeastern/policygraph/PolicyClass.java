package com.northeastern.policygraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PolicyClass extends NodeElement{

  protected String policyClass;
  private static List<NodeElement> allPolicies = new ArrayList<>();

  public PolicyClass(String policyClass) {
    this.policyClass = policyClass;
    allPolicies.add(this);
  }

  public String getPolicyClass() {
    return policyClass;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PolicyClass that = (PolicyClass) o;
    return Objects.equals(policyClass, that.policyClass);
  }

  @Override
  public int hashCode() {
    return Objects.hash(policyClass);
  }

  @Override
  public String toString() {
    return policyClass;
  }

  @Override
  public String toStringProlog() {
    return String.format("pc(%1$s).", this.getPolicyClass());
  }

  @Override
  public String toStringPML() {
    return String.format("create pc \"%1$s\"", this.getPolicyClass());
  }

  public static List<NodeElement> getAllElements() {
    return allPolicies;
  }

  @Override
  String getTypeNameProlog() {
    return "pc";
  }

  @Override
  String getTypeNamePML() {
    return "pc";
  }
}
