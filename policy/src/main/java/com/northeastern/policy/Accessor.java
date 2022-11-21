package com.northeastern.policy;

import java.util.Set;

public interface Accessor {
  public Set<ResourceAccess> generateAccesses() throws MyPMException;
}
