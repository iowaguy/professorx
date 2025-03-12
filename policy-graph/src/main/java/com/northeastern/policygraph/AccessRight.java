package com.northeastern.policygraph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AccessRight {

  protected String permission;
//  private static List<AccessRight> permissions = new ArrayList<>();
  private static Set<String> permissions = new HashSet<>();
  private static String prologPath = "policy-graph/src/main/resources/translatePolicy.pl";
  private static String pmlPath = "policy-graph/src/main/resources/translatePolicy.pal";
  public AccessRight(String permission) {
    this.permission = permission;
    permissions.add(this.toString());
  }

  public static List<String> buildAccessRights(List<String> allPermissions) {
    String buildPML;
    StringBuilder buildProlog = new StringBuilder();
    Iterator<String> accessRightIterator = allPermissions.iterator();
    List<String> arPML = new ArrayList<>();
    while (accessRightIterator.hasNext()) {
      String arToString = accessRightIterator.next().toString().toLowerCase();
      buildProlog.append(String.format("ar(%1$s).", arToString)).append(System.lineSeparator());
      arPML.add("\"" + arToString + "\"");
    }
    String arsPML = String.join(", ", arPML);
    buildPML = String.format("set resource operations [%1$s]", arsPML) + System.lineSeparator();
    return List.of(buildProlog.toString(), buildPML);
  }

  public static List<String> buildAccessRights22(List<String> allPermissions) {
    String buildPML;
    StringBuilder buildProlog = new StringBuilder();
    Iterator<String> accessRightIterator = allPermissions.iterator();
    List<String> arPML = new ArrayList<>();
    while (accessRightIterator.hasNext()) {
      String arToString = accessRightIterator.next().toString().toLowerCase();
      buildProlog.append(String.format("ar(%1$s).", arToString)).append(System.lineSeparator());
      arPML.add("'" + arToString + "'");
    }
    String arsPML = String.join(", ", arPML);
    arsPML += ";";
    buildPML = String.format("set resource access rights %1$s", arsPML) + System.lineSeparator();
    return List.of(buildProlog.toString(), buildPML);
  }

  public String getPermission() {
    return permission;
  }

  public static Set<String> getAllPermissions() {
    return permissions;
  }

  @Override
  public String toString() {
    return permission;
  }
}
