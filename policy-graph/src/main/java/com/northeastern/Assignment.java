package com.northeastern;

import org.jgrapht.graph.DefaultEdge;

public class Assignment extends Relation {

  public Assignment() {
    super(null);
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
    return String.format("assign \'%1$s\' to \'%2$s\';",
        this.getTarget(), this.getSource());
  }

  protected NodeElement getTarget() {
    return (NodeElement) super.getTarget();
  }
}
