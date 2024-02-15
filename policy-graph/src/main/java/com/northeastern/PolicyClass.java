package com.northeastern;

import java.util.Objects;

public class PolicyClass implements NodeElement{

  protected String policyClass;

  public PolicyClass(String policyClass) {
    this.policyClass = policyClass;
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
    return String.format("create policy class \'%1$s\';", this.getPolicyClass());
  }
}
