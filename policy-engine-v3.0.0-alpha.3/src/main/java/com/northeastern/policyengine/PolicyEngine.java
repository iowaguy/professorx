package com.northeastern.policyengine;

import com.northeastern.policy.ResourceAccess;
import com.northeastern.policy.MyPMException;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pdp.Decision;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pdp.ResourceAdjudicationResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gov.nist.csd.pm.pap.query.UserContext;

public class PolicyEngine {
  static Logger logger = LogManager.getLogger(PolicyEngine.class);
  private final PDP pdp;
  private final PAP pap;
  private final PolicyImpl policy;


  public PolicyEngine(PolicyImpl policy) throws MyPMException {
    this.policy = policy;
    try {
      this.pap = new MemoryPAP();
      this.pap.executePML(new UserContext("super"), policy.getPolicyString());
      this.pdp = new PDP(this.pap);
    } catch (PMException e) {
      throw new MyPMException(e);
    }
  }

  public boolean getDecision(ResourceAccess access) throws MyPMException {
    return getDecision(access.getSubject(), access.getObject(), access.getPermissions());
  }

  public boolean getDecision(String subject, String object, String action) throws MyPMException {
    UserContext superUser = new UserContext("super");
    ResourceAdjudicationResponse permissions = null;
    try {
      permissions = this.pdp.adjudicateResourceOperation(new UserContext(subject), object, action);
              //policyReviewer().getAccessRights(new UserContext(subject), object);
//      this.pdp.runTx(superUser, (policy -> {
//        policy.graph().createObjectAttribute("newOA", "oa1");
//      }));
    } catch (PMException e) {
      throw new MyPMException(e);
    }
    return Decision.GRANT == permissions.getDecision();
  }

  public PAP getPap() {
    return this.pap;
  }
}
