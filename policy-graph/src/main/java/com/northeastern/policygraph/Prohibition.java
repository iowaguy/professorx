package com.northeastern.policygraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Prohibition extends Relation {

  private Random random = new Random();
  private static List<Relation> allProhibitions = new ArrayList<>();
  protected AccessRight[] accessRights;

  public Prohibition(AccessRight[] accessRights) {
    super(accessRights);
    allProhibitions.add(this);
  }

  @Override
  public String toString() {
    return toStringProlog();
  }

  //TODO Should we represent prohibited attributes by a list or [attribute]？
  @Override
  public String toStringProlog() {
    StringBuilder ars = new StringBuilder();
    ars.append("[").append(String.join(", ", this.getAccessRightString())).append("]");

    return String.format("disjunctiveProhibition(%1$s, [%2$s], %3$s).",
        this.getTarget(), this.getSource(), ars);
  }

  @Override
  public String toStringPML() {
    StringBuilder ars = new StringBuilder();
    ars.append("[\"").append(String.join("\", \"", this.getAccessRightString())).append("\"]");

    return String.format("create prohibition \"%1$s-prohibition-%2$s-%4$s-" + random.nextInt() + "\"\ndeny user \"%1$s\"\naccess rights"
            + " %3$s\non union of [\"%2$s\"]",
        this.getTarget(), this.getSource(), ars , ars.toString().replace("[\"", "").replace("\"]", ""));
  }

  @Override
  public String toStringPML22() {
    StringBuilder ars = new StringBuilder();
    ars.append("[\"").append(String.join("\", \"", this.getAccessRightString())).append("\"]");

    return String.format("create prohibition \"%1$s-prohibition-%2$s-%4$s-" + random.nextInt() + "\"\ndeny user \"%1$s\"\naccess rights"
            + " %3$s\non union of \"%2$s\";",
        this.getTarget(), this.getSource(), ars, ars.toString().replace("\"", "\\"));
  }

  public static List<Relation> getAllProhibitions() {
    return allProhibitions;
  }
}
