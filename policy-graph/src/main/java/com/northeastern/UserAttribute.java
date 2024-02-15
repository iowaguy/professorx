package com.northeastern;

import java.util.Objects;

public class UserAttribute extends Attribute{

  protected String userAttribute;

  public UserAttribute(String userAttribute) {
    this.userAttribute = userAttribute;
  }

  public String getUserAttribute() {
    return userAttribute;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserAttribute that = (UserAttribute) o;
    return Objects.equals(userAttribute, that.userAttribute);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userAttribute);
  }

  @Override
  public String toString() {
    return userAttribute;
  }

  @Override
  public String toStringProlog() {
    return String.format("ua(%1$s).", this.getUserAttribute());
  }

  @Override
  public String toStringPML() {
    return String.format("create user attribute \'%1$s\';", this.getUserAttribute());
  }
}
