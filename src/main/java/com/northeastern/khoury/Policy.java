package com.northeastern.khoury;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.GraphSerializer;
import gov.nist.csd.pm.pip.graph.MemGraph;

public class Policy extends MemGraph {
  static Logger logger = LogManager.getLogger(Policy.class);

  public Policy(String policyPath) {
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
      GraphSerializer.fromJson(this, policyString);
    } catch (PMException pm) {
      logger.fatal("Problem parsing policy string: {}", pm.getMessage());
      return;
    }
  }
}
