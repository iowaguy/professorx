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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PolicyEngine {
  static Logger logger = LogManager.getLogger(PolicyEngine.class);

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.printf("%d arguments provided. Need 1.", args.length);
      System.exit(1);
    }

    // Read the policy from disk
    Path fileName = Path.of(args[0]);
    String policyString;
    try {
      policyString = Files.readString(fileName);
      logger.info("Full policy:\n {}", policyString);
    } catch (IOException io) {
      logger.fatal("Problem reading file: {}", io.getMessage());
      return;
    }

    Graph graph = new MemGraph();

    try {
      GraphSerializer.fromJson(graph, policyString);
    } catch (PMException pm) {
      logger.fatal("Problem parsing policy string: {}", pm.getMessage());
      return;
    }

    // decider = new PReviewDecider(graph, prohibitions);
    // PDP pdp;
    // Decider decider;
    // try {
    //   PAP pap = new MemoryPAP();
    //   // List<PALStatement> pals = pap.compilePAL(policyString);
    //   // for (PALStatement p : pals) {
    //   //   System.out.println(p);
    //   // }
    //   pdp = new MemoryPDP(pap);
    // } catch (PMException pm) {
    //   logger.fatal("Problem initializing PAP or PDP: {}", pm.getMessage());
    //   return;
    // }

    return;
  }

}
