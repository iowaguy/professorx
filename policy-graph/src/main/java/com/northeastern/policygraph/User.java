package com.northeastern.policygraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends NodeElement {

  protected String user;
  private static List<NodeElement> allUsers = new ArrayList<>();

  public User(String user){
    this.user = user;
    allUsers.add(this);
  }

  public String getUser() {
    return user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user1 = (User) o;
    return Objects.equals(user, user1.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user);
  }

  @Override
  public String toString() {
    return user;
  }

  @Override
  public String toStringProlog() {
    return String.format("u(%1$s).", this.getUser());
  }

  @Override
  public String toStringPML() {
    return String.format("create u \"%1$s\"", this.getUser());
  }

  public static List<NodeElement> getAllElements() {
    return allUsers;
  }

  @Override
  String getTypeNameProlog() {
    return "u";
  }

  @Override
  String getTypeNamePML() {
    return "u";
  }
}
