package com.northeastern;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Association extends Relation {

  public Association(AccessRight[] accessRights) {
    super(accessRights);
  }

  @Override
  public String toString() {
    return String.format("association(%1$s,%2$s,[%3$s]).",
        this.getTarget(), this.getSource(), this.getAccessRightString());
  }

  @Override
  public String toStringProlog() {
    StringBuilder ars = new StringBuilder();
    ars.append("[").append(String.join(", ", this.getAccessRightString())).append("]");

    return String.format("association(%1$s,%2$s,%3$s).",
        this.getTarget(), this.getSource(), ars);
  }

  @Override
  public String toStringPML() {
    StringBuilder ars = new StringBuilder();
    ars.append("[\'").append(String.join("\', \'", this.getAccessRightString())).append("\']");

    return String.format("associate \'%1$s\' and \'%2$s\' with %3$s;",
        this.getTarget(), this.getSource(), ars);
  }
}
