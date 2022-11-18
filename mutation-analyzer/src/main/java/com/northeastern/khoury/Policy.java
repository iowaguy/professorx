package com.northeastern.khoury;

import gov.nist.csd.pm.exceptions.PMException;

public interface Policy {
  public Policy attributeExchangeSource(String source, String dest, String newSource);
  public Policy attributeExchangeDest(String source, String dest, String newDest) throws PMException;
}
