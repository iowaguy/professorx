package com.northeastern.policygraph;

import java.util.ArrayList;
import java.util.List;

public class Assignment extends Relation {

  private static List<Relation> allAssignments = new ArrayList<>();

  public Assignment() {
    super(null);
    allAssignments.add(this);
  }

  @Override
  public String toString() {
    return String.format("assign(%1$s,%2$s).",
        this.getTarget(), this.getSource());
  }

  @Override
  public String toStringProlog() {
    return String.format("assign(%1$s,%2$s).",
        this.getTarget(), this.getSource());
  }

  @Override
  public String toStringPML() {
    return String.format("assign \"%1$s\" to [\"%2$s\"]",
        this.getTarget(), this.getSource());
  }

  public static List<Relation> getAllAssignments() {
    return allAssignments;
  }

  protected NodeElement getTarget() {
    return (NodeElement) super.getTarget();
  }

}
