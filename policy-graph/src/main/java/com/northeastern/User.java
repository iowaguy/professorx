package com.northeastern;

import java.util.Objects;

public class User implements NodeElement{

  protected String user;

  public User(String user){
    this.user = user;
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
    return String.format("create user \'%1$s\';", this.getUser());
  }
}
