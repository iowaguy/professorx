package com.northeastern;

import org.jgrapht.graph.DefaultEdge;

public class Assignment extends DefaultEdge {

  public Assignment() {
  }

  @Override
  public String toString() {
    return String.format("assign(%1$s,%2$s)",
        this.getSource(), this.getTarget());
  }
}
