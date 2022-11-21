package com.northeastern.policyengine051920;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import com.northeastern.policy.MyPMException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.GraphSerializer;
import gov.nist.csd.pm.pip.graph.MemGraph;

import com.northeastern.policy.Policy;

public class PolicyImpl implements Policy {
  private MemGraph graph;

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
      GraphSerializer.fromJson(this.graph, policyString);
    } catch (PMException pm) {
      logger.fatal("Problem parsing policy string: {}", pm.getMessage());
      return;
    }
  }

  private Policy attributeExchangeExplicit(String source, String dest, String newSource,
                                               String newDest) throws MyPMException {
    Map<String, OperationSet> associations = null;
    try {
      associations = this.graph.getSourceAssociations(source);
    } catch (PMException e) {
      throw new MyPMException(e);
    }

    OperationSet ops = associations.get(dest);
    this.graph.dissociate(source, dest);

    if (ops != null) {
      try {
        this.graph.associate(newSource, newDest, ops);
      } catch (PMException e) {
        throw new MyPMException(e);
      }
    }
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

  public MemGraph getGraph() {
    return this.graph;
  }
}
