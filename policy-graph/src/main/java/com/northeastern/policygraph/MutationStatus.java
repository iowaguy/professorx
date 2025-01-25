package com.northeastern.policygraph;

public class MutationStatus {
  public PolicyGraph policyGraph;
  public boolean success;

  public MutationStatus(PolicyGraph policyGraph, boolean success) {
    this.policyGraph = policyGraph;
    this.success = success;
  }

  public PolicyGraph getPolicyGraph() {
    return policyGraph;
  }

  public boolean isSuccess() {
    return success;
  }
}
