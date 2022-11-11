package com.northeastern.khoury;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gov.nist.csd.pm.exceptions.PMException;

public class Runner {
  static Logger logger = LogManager.getLogger(Runner.class);

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.printf("%d arguments provided. Need 1.", args.length);
      System.exit(1);
    }

    Policy policy = new Policy(args[0]);
    PolicyEngine pe = new PolicyEngine(policy);

    Accessor accessor = new ExhaustiveAccessor(policy);

    Set<ResourceAccess> accesses = null;
    try {
      accesses = accessor.generateAccesses();
    } catch (PMException e) {
      logger.fatal(() -> "Issue encountered generating accesses: " + e.getMessage());
      System.exit(1);
    }

    for (ResourceAccess a : accesses) {
      boolean b = pe.getDecision(a);
      System.out.println(a.toString() + ". Allowed? " + b);
    }
  }
}
