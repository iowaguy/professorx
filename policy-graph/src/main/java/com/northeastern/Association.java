package com.northeastern;
import org.jgrapht.graph.DefaultEdge;

public class Association extends DefaultEdge {

  protected AccessRight[] accessRights;

  public Association(AccessRight[] accessRights) {
    this.accessRights = accessRights;
  }

  public AccessRight[] getAccessRights() {
    return accessRights;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    for (AccessRight str : this.getAccessRights()) {
      stringBuilder.append(str + " ");
    }
    String ars = stringBuilder.toString();

    return String.format("association(%1$s,%2$s,%3$s)",
        this.getSource(), this.getTarget(), ars.strip());
  }
}
