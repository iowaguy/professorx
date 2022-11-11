package com.northeastern.khoury;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.decider.Decider;
import gov.nist.csd.pm.pdp.decider.PReviewDecider;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.GraphSerializer;
import gov.nist.csd.pm.pip.graph.MemGraph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PolicyEngine {
  static Logger logger = LogManager.getLogger(PolicyEngine.class);
  private Decider decider;
  private Graph graph = new MemGraph();

  public PolicyEngine(String policyPath) {
    // Read the policy from disk
    Path fileName = Path.of(policyPath);
    String policyString;

    try {
      policyString = Files.readString(fileName);
      logger.info("Full policy:\n {}", policyString);
    } catch (IOException io) {
      logger.fatal("Problem reading file: {}", io.getMessage());
      return;
    }

    try {
      GraphSerializer.fromJson(graph, policyString);
    } catch (PMException pm) {
      logger.fatal("Problem parsing policy string: {}", pm.getMessage());
      return;
    }

    decider = new PReviewDecider(graph, null /* prohibitions */);
  }

  public boolean getPermission(String subject, String object, String action) {
    try {
      Set<String> permissions = decider.list(subject, "0" , object);
      return permissions.contains(action);
    } catch (PMException p) {
      return false;
    }
  }
}
