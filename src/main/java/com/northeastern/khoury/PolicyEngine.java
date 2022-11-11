package com.northeastern.khoury;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.decider.Decider;
import gov.nist.csd.pm.pdp.decider.PReviewDecider;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PolicyEngine {
  static Logger logger = LogManager.getLogger(PolicyEngine.class);
  private Decider decider;

  public PolicyEngine(Policy policy) {
    decider = new PReviewDecider(policy, null /* prohibitions */);
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
