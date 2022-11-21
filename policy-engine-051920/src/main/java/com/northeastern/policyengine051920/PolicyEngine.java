package com.northeastern.policyengine051920;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.decider.Decider;
import gov.nist.csd.pm.pdp.decider.PReviewDecider;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.northeastern.policy.Policy;
import com.northeastern.policy.ResourceAccess;

public class PolicyEngine {
  static Logger logger = LogManager.getLogger(PolicyEngine.class);
  private Decider decider;

  public PolicyEngine(PolicyImpl policy) {
    this.decider = new PReviewDecider(policy, null /* prohibitions */);
  }

  public boolean getDecision(ResourceAccess access) {
    return getDecision(access.getSubject(), access.getObject(), access.getPermissions());
  }

  public boolean getDecision(String subject, String object, String action) {
    try {
      Set<String> permissions = decider.list(subject, "0" , object);
      return permissions.contains(action);
    } catch (PMException p) {
      return false;
    }
  }
}
