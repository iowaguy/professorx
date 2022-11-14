package com.northeastern.khoury;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.NotImplementedException;

import gov.nist.csd.pm.exceptions.PMException;

public abstract class Accessor {
  protected List<Policy> policies;

  public Accessor(Policy... policies) {
    this.policies = new ArrayList<>();
    for (Policy p : policies) {
      this.policies.add(p);
    }
  }

  public Set<ResourceAccess> generateAccesses() throws PMException {
    throw new NotImplementedException();
  }
}
