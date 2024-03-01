package com.northeastern;

import org.jgrapht.graph.DefaultEdge;

public class Prohibition extends Relation {

  protected AccessRight[] accessRights;

  public Prohibition(AccessRight[] accessRights) {
    super(accessRights);
  }

  @Override
  public String toString() {
    return String.format("association(%1$s,%2$s,[%3$s]).",
        this.getTarget(), this.getSource(), this.getAccessRightString());
  }

  //TODO Should we represent prohibited attributes by a list or [attribute]？
  @Override
  public String toStringProlog() {
    StringBuilder ars = new StringBuilder();
    ars.append("[").append(String.join(", ", this.getAccessRightString())).append("]");

    return String.format("disjunctiveProhibition(%1$s, [%2$s], %3$s).",
        this.getTarget(), this.getSource(), ars);
  }

  @Override
  public String toStringPML() {
    StringBuilder ars = new StringBuilder();
    ars.append("[\"").append(String.join("\", \"", this.getAccessRightString())).append("\"]");

    return String.format("create prohibition \"%1$s-prohibition-%2$s-%4$s\"\ndeny user \"%1$s\"\naccess rights"
            + " %3$s\non union of \"%2$s\";",
        this.getTarget(), this.getSource(), ars, ars.toString().replace("\"", "\\"));
  }
}
