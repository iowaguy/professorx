package com.northeastern.khoury;

import java.util.Set;

import org.apache.commons.lang3.NotImplementedException;

import gov.nist.csd.pm.exceptions.PMException;

public abstract class Accessor {
  protected Policy policy;

  public Accessor(Policy policy) {
    this.policy = policy;
  }

  public Set<ResourceAccess> generateAccesses() throws PMException {
    throw new NotImplementedException();
  }
}
