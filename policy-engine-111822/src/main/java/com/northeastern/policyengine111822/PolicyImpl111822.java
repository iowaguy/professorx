package com.northeastern.policyengine111822;

import com.northeastern.policy.MyPMException;
import com.northeastern.policy.Policy;
import gov.nist.csd.pm.policy.author.PolicyAuthor;
import gov.nist.csd.pm.policy.author.pal.PALCompiler;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_USER;

public class PolicyImpl111822 implements Policy {
  private String policyString;
  static Logger logger = LogManager.getLogger(PolicyImpl111822.class);

  public PolicyImpl111822(String policyPath) {
    // Read the policy from disk
    Path fileName = Path.of(policyPath);
    String policyString;

    try {
      this.policyString = Files.readString(fileName);
      logger.info("Full policy:\n {}", this.policyString);
    } catch (IOException io) {
      logger.fatal("Problem reading file: {}", io.getMessage());
    }

//    UserContext superUser = new UserContext(SUPER_USER);
//    try {
//      PALCompiler.compilePAL((PolicyAuthor) superUser, policyString);
//    } catch (PMException e) {
//      logger.fatal("Problem compile policy string: {}", e.getMessage());
//    }
  }

  private Policy attributeExchangeExplicit(String source, String dest, String newSource,
                                               String newDest) throws MyPMException {
//    Map<String, OperationSet> associations = null;
//    try {
//      associations = this.getSourceAssociations(source);
//    } catch (PMException e) {
//      throw new MyPMException(e);
//    }
//
//    OperationSet ops = associations.get(dest);
//    this.dissociate(source, dest);
//
//    if (ops != null) {
//      try {
//        this.associate(newSource, newDest, ops);
//      } catch (PMException e) {
//        throw new MyPMException(e);
//      }
//    }
    return this;
  }

  public PolicyImpl111822 attributeExchangeSource(String source, String dest, String newSource)
    throws MyPMException {
    attributeExchangeExplicit(source, dest, newSource, dest);
    return this;
  }

  public PolicyImpl111822 attributeExchangeDest(String source, String dest, String newDest) throws MyPMException {
    attributeExchangeExplicit(source, dest, source, newDest);
    return this;
  }

  public String getPolicyString() {
    return this.policyString;
  }
}
