package com.northeastern.khoury;

public class ResourceAccess {
  private String subject;
  private String object;
  private String permissions;

  public ResourceAccess(String subject, String object, String permissions) {
    this.subject = subject;
    this.object = object;
    this.permissions = permissions;
  }

  public String getSubject() {
    return this.subject;
  }

  public String getObject() {
    return this.object;
  }

  public String getPermissions() {
    return this.permissions;
  }
}
