package com.northeastern.khoury;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.decider.Decider;
// import gov.nist.csd.pm.pip.graph.Graph;
// import gov.nist.csd.pm.pip.graph.GraphSerializer;
// import gov.nist.csd.pm.pip.graph.MemGraph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;
import java.util.logging.Level;

public class PolicyEngine {

  static Logger logger = Logger.getLogger(PolicyEngine.class.getName());

  // Graph graph = new MemGraph();
  // private final Logger log = getLogger(getClass());
  // Decider decider;
  // PDP pdp;

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.printf("%d arguments provided. Need 1.", args.length);
      System.exit(1);
    }

    // TODO make this a CLI param with a default of SEVERE
    logger.setLevel(Level.FINE);

    // Read the policy from disk
    Path fileName = Path.of(args[0]);
    String policyString;
    try {
      policyString = Files.readString(fileName);
      System.out.println(policyString);
    } catch (IOException io) {
      logger.info("Problem reading file: " + io.getMessage());
      return;
    }

    // pdp = new PDP(new PAP(graph, prohibitions, obligations), new EPPOptions());
    // List<PALStatement> pals = pdp.compilePAL(input);


    return;
  }

}
