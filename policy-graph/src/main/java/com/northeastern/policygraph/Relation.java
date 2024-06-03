package com.northeastern.policygraph;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.graph.DefaultEdge;

public abstract class Relation extends DefaultEdge implements Element {
  private NodeElement policySource;
  private NodeElement policyTarget;
  protected AccessRight[] accessRights;

  public Relation(AccessRight[] accessRights) {
    this.accessRights = accessRights;
  }
  public Relation(NodeElement policySource, NodeElement policyTarget, AccessRight[] accessRights) {
    this.policySource = policySource;
    this.policyTarget = policyTarget;
    this.accessRights = accessRights;
  }
  public AccessRight[] getAccessRights() {
    return accessRights;
  }
  public List<String> getAccessRightString() {
    List<String> arList = new ArrayList<>();
    for (AccessRight ar : this.getAccessRights()) {
      arList.add(ar.toString().toLowerCase());
    }
    return arList;
  }
}
