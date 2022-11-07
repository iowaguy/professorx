package com.northeastern.khoury;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPAP;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.memory.MemoryPDP;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.function.Supplier;

public class PolicyEngine {

  static Logger logger = Logger.getLogger(PolicyEngine.class.getName());

  public static void main(String[] args) {
    // TODO make this a CLI param with a default of SEVERE
    logger.setLevel(Level.FINE);

    if (args.length != 1) {
      System.err.printf("%d arguments provided. Need 1.", args.length);
      System.exit(1);
    }


    // Read the policy from disk
    Path fileName = Path.of(args[0]);
    String policyString;
    try {
      policyString = Files.readString(fileName);
      logger.fine(() -> "Full policy:\n" + policyString);
    } catch (IOException io) {
      logger.severe(() -> "Problem reading file: " + io.getMessage());
      return;
    }

    PDP pdp;
    try {
      PAP pap = new MemoryPAP();
      List<PALStatement> pals = pap.compilePAL(policyString);
      pdp = new MemoryPDP(pap);
    } catch (PMException pm) {
      logger.severe(() -> "Problem initializing PAP or PDP: " + pm.getMessage());
      return;
    }


    return;
  }

}
