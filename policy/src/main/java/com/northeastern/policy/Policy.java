package com.northeastern.policy;

public interface Policy {
  public Policy attributeExchangeSource(String source, String dest, String newSource) throws MyPMException;
  public Policy attributeExchangeDest(String source, String dest, String newDest) throws MyPMException;
}
