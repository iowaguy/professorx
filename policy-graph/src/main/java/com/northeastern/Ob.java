package com.northeastern;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Ob extends ObjectAttribute{

  protected String ob;
  private static List<NodeElement> allObs = new ArrayList<>();

  public Ob(String objectAttribute) {
    super(objectAttribute);
    this.ob = objectAttribute;
    allObs.add(this);
  }

  public String getOb() {
    return ob;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Ob ob1 = (Ob) o;
    return Objects.equals(ob, ob1.ob);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ob);
  }

  @Override
  public String toString() {
    return ob;
  }

  @Override
  public String toStringProlog() {
    return String.format("o(%1$s).", this.getOb());
  }

  @Override
  public String toStringPML() {
    return String.format("create object \'%1$s\'", this.getOb());
  }

  @Override
  public List<NodeElement> getAllElements() {
    return allObs;
  }
}
