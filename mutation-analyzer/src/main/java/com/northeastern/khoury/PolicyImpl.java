package com.northeastern.khoury;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.GraphSerializer;
import gov.nist.csd.pm.pip.graph.MemGraph;

public class PolicyImpl extends MemGraph implements Policy {
  static Logger logger = LogManager.getLogger(PolicyImpl.class);

  public PolicyImpl(String policyPath) {
    // Read the policy from disk
    Path fileName = Path.of(policyPath);
    String policyString;

    try {
      policyString = Files.readString(fileName);
      logger.info("Full policy:\n {}", policyString);
    } catch (IOException io) {
      logger.fatal("Problem reading file: {}", io.getMessage());
      return;
    }

    try {
      GraphSerializer.fromJson(this, policyString);
    } catch (PMException pm) {
      logger.fatal("Problem parsing policy string: {}", pm.getMessage());
      return;
    }
  }

  private Policy attributeExchangeExplicit(String source, String dest, String newSource,
                                               String newDest) throws PMException {
    Map<String, OperationSet> associations = this.getSourceAssociations(source);
    OperationSet ops = associations.get(dest);
    this.dissociate(source, dest);

    if (ops != null) {
      this.associate(newSource, newDest, ops);
    }
    return this;
  }

  public PolicyImpl attributeExchangeSource(String source, String dest, String newSource)
    throws PMException {
    attributeExchangeExplicit(source, dest, newSource, dest);
    return this;
  }


  public PolicyImpl attributeExchangeDest(String source, String dest, String newDest) throws PMException {
    attributeExchangeExplicit(source, dest, source, newDest);
    return this;
  }
}
