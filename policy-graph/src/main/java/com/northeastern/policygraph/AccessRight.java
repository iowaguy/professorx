package com.northeastern.policygraph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AccessRight {

  protected String permission;
  private static List<AccessRight> permissions = new ArrayList<>();
  private static String prologPath = "policy-graph/src/main/resources/translatePolicy.pl";
  private static String pmlPath = "policy-graph/src/main/resources/translatePolicy.pal";
  public AccessRight(String permission) {
    this.permission = permission;
    permissions.add(this);
  }

  public static List<String> buildAccessRights(List<AccessRight> allPermissions) {
    String buildPML;
    StringBuilder buildProlog = new StringBuilder();
    Iterator<AccessRight> accessRightIterator = allPermissions.iterator();
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

  public static List<String> buildAccessRights22(List<AccessRight> allPermissions) {
    String buildPML;
    StringBuilder buildProlog = new StringBuilder();
    Iterator<AccessRight> accessRightIterator = allPermissions.iterator();
    List<String> arPML = new ArrayList<>();
    while (accessRightIterator.hasNext()) {
      String arToString = accessRightIterator.next().toString().toLowerCase();
      buildProlog.append(String.format("ar(%1$s).", arToString) + System.lineSeparator());
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

  public static List<AccessRight> getAllPermissions() {
    return permissions;
  }

  public static void writeAccessRights(List<AccessRight> allPermissions) {
    // create a writer
    BufferedWriter writerPML = null;
    // append after the nodes
    FileWriter writerProlog = null;
    try {
      writerProlog = new FileWriter(prologPath, true);
    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.exit(1);
    }
    Iterator<AccessRight> accessRightIterator = allPermissions.iterator();
    List<String> arPML = new ArrayList<>();
    while (accessRightIterator.hasNext()) {
      try {
        String arToString = accessRightIterator.next().toString().toLowerCase();
        writerProlog.append(String.format("ar(%1$s).", arToString) + System.lineSeparator());
        arPML.add('\'' + arToString + '\'');
        writerProlog.flush();
      } catch (IOException e) {
        System.out.println(e.getMessage());
        System.exit(1);
      }
    }
    try {
      writerPML = Files.newBufferedWriter(Paths.get(pmlPath));
      String arsPML = String.join(", ", arPML);
      arsPML += ";";
      writerPML.write(String.format("set resource access rights %1$s", arsPML) + System.lineSeparator());
      writerPML.flush();
    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.exit(1);
    }
  }

  @Override
  public String toString() {
    return permission;
  }
}
