package com.northeastern.policygraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ObjectAttribute extends NodeElement {

  protected String objectAttribute;
  private static List<NodeElement> allObjectAttributes = new ArrayList<>();

  public ObjectAttribute(String objectAttribute) {
    this.objectAttribute = objectAttribute;
    allObjectAttributes.add(this);
  }

  public String getObjectAttribute() {
    return objectAttribute;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ObjectAttribute that = (ObjectAttribute) o;
    return Objects.equals(objectAttribute, that.objectAttribute);
  }

  @Override
  public int hashCode() {
    return Objects.hash(objectAttribute);
  }

  @Override
  public String toString() {
    return objectAttribute;
  }

  @Override
  public String toStringProlog() {
    return String.format("oa(%1$s).", this.getObjectAttribute());
  }

  @Override
  public String toStringPML() {
    return String.format("create oa \"%1$s\"", this.getObjectAttribute());
  }

  @Override
  public String toStringPML22() {
    return String.format("create object attribute \'%1$s\'", this.getObjectAttribute());
  }

  public static List<NodeElement> getAllElements() {
    return allObjectAttributes;
  }

  @Override
  String getTypeNameProlog() {
    return "oa";
  }

  @Override
  String getTypeNamePML() {
    return "oa";
  }
}
