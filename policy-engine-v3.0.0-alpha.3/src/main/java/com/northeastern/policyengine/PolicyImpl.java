package com.northeastern.policyengine;

import com.northeastern.policy.MyPMException;
import com.northeastern.policy.Policy;
import java.nio.file.Paths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PolicyImpl implements Policy {
  private String policyString;
  private Path policyPath;
  static Logger logger = LogManager.getLogger(PolicyImpl.class);

//  public PolicyImpl(Path policyPath) {
//    // Read the policy from disk
//    Path fileName = policyPath;
//
//    try {
//      this.policyString = Files.readString(fileName);
//      logger.info("Full policy:\n {}", this.policyString);
//    } catch (IOException io) {
//      logger.fatal("Problem reading file: {}", io.getMessage());
//    }

//    UserContext superUser = new UserContext(SUPER_USER);
//    try {
//      PALCompiler.compilePAL((PolicyAuthor) superUser, policyString);
//    } catch (PMException e) {
//      logger.fatal("Problem compile policy string: {}", e.getMessage());
//    }
//  }

  public PolicyImpl(String policyString, Path policyPath) {
    this.policyPath = policyPath;
    // Read the policy from String
    this.policyString = policyString;
//    logger.info("Full policy:\n {}", this.policyString);
  }


  public PolicyImpl(Path policyPath) {
    // Read the policy from String
    this.policyPath = policyPath;

    try {
        policyString = Files.readString(policyPath);
    } catch (IOException e) {
        logger.fatal(() -> "Issue encountered reading file: " + policyPath);
        System.exit(1);
    }
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

  public PolicyImpl attributeExchangeSource(String source, String dest, String newSource)
    throws MyPMException {
    attributeExchangeExplicit(source, dest, newSource, dest);
    return this;
  }

  public PolicyImpl attributeExchangeDest(String source, String dest, String newDest) throws MyPMException {
    attributeExchangeExplicit(source, dest, source, newDest);
    return this;
  }

  public String getPolicyString() {
    return this.policyString;
  }

  public Path getPolicyPath() {
    return policyPath;
  }
}
