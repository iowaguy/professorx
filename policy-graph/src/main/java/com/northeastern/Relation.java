package com.northeastern;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.graph.DefaultEdge;

public abstract class Relation extends DefaultEdge implements Element {

  protected AccessRight[] accessRights;

  public Relation(AccessRight[] accessRights) {
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

//  String toStringProlog() {
//    return "to be defined in subclass";
//  }
//
//  String toStringPML() {
//    return "to be defined in subclass";
//  }
}
