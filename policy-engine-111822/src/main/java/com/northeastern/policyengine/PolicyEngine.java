package com.northeastern.policyengine;

import com.northeastern.policy.ResourceAccess;
import com.northeastern.policy.MyPMException;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPAP;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.memory.MemoryPDP;
import gov.nist.csd.pm.policy.author.pal.PALExecutor;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gov.nist.csd.pm.policy.model.access.UserContext;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_USER;

public class PolicyEngine {
  static Logger logger = LogManager.getLogger(PolicyEngine.class);
  private final PDP pdp;
  private final PAP pap;
  private final PolicyImpl policy;


  public PolicyEngine(PolicyImpl policy) throws MyPMException {
    this.policy = policy;
    try {
      this.pap = new MemoryPAP();
      this.pdp = new MemoryPDP(this.pap);
    } catch (PMException e) {
      throw new MyPMException(e);
    }
  }

  public boolean getDecision(ResourceAccess access) throws MyPMException {
    return getDecision(access.getSubject(), access.getObject(), access.getPermissions());
  }

  public boolean getDecision(String subject, String object, String action) throws MyPMException {
    UserContext superUser = new UserContext(SUPER_USER);
    try {
      this.pdp.runTx(superUser, (p) -> {
        PALExecutor.compileAndExecutePAL(this.pap, superUser, this.policy.getPolicyString());
      });
    } catch (PMException e) {
      throw new MyPMException(e);
    }

    AccessRightSet permissions = null;
    try {
      permissions = this.pdp.policyReviewer().getAccessRights(new UserContext(subject), object);
    } catch (PMException e) {
      throw new MyPMException(e);
    }

    return permissions.contains(action);
  }

  public PAP getPap() {
    return this.pap;
  }
}
