package com.northeastern.policyengine;

import com.northeastern.policy.Accessor;
import com.northeastern.policy.MyPMException;
import com.northeastern.policy.ResourceAccess;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.graph.node.NodeType;

import java.util.*;


public class ExhaustiveAccessor implements Accessor {
 private final List<PolicyImpl> policies;
 private final List<PAP> paps;

  public ExhaustiveAccessor(PolicyImpl... policies) throws MyPMException {
    this.policies = new ArrayList<>();
    Collections.addAll(this.policies, policies);

    this.paps = new ArrayList<>();
    for (PolicyImpl p : policies) {
      this.paps.add(new PolicyEngine(p).getPap());
    }
  }

  public Set<ResourceAccess> generateAccesses() throws MyPMException {
    Set<String> removeMe = new HashSet<>();
    removeMe.add("super");
    removeMe.add("super_ua");
    removeMe.add("super_oa");
    removeMe.add("super_ua1");
    removeMe.add("super_policy_pc_rep");

    // get all users and user attributes, U
    Set<String> allU = new HashSet<>();
    for (int i = 0; i < this.policies.size(); i++) {
      try {
        Collection<String> u = this.paps.get(i).query().graph().search(NodeType.U, new HashMap<>());
        allU.addAll(u);
        Collection<String> ua = this.paps.get(i).query().graph().search(NodeType.UA, new HashMap<>());
        allU.addAll(ua);
        allU.removeAll(removeMe);
      } catch (PMException e) {
        throw new MyPMException(e);
      }
    }

    // get all objects and object attributes, O
    Set<String> allOA = new HashSet<>();
    for (int i = 0; i < this.policies.size(); i++) {
      try {
        Collection<String> o = this.paps.get(i).query().graph().search(NodeType.O, new HashMap<>());
        allOA.addAll(o);

        Collection<String> oa = this.paps.get(i).query().graph().search(NodeType.OA, new HashMap<>());
        allOA.addAll(oa);
        allOA.removeAll(removeMe);
      } catch (PMException e) {
        throw new MyPMException(e);
      }
    }

    // get all possible permissions, P
    AccessRightSet possiblePermissions = new AccessRightSet();
    for (int i = 0; i < this.policies.size(); i++) {
      AccessRightSet targetOps = null;
      try {
        targetOps = this.paps.get(i).policyStore().operations().getResourceOperations();
        possiblePermissions.addAll(targetOps);
      } catch (PMException e) {
        throw new MyPMException(e);
      }
    }

    // generate all possible triples in UxOxP
    Set<ResourceAccess> crossProduct = generateCrossProduct(allU, allOA, possiblePermissions);

    // generate all possible triples in UxUxP
    return generateCrossProduct(allU, allU, possiblePermissions, crossProduct);
  }

  private Set<ResourceAccess> generateCrossProduct(Set<String> subjects, Set<String> objects, AccessRightSet ops) {
    Set<ResourceAccess> result = new HashSet<>();
    return generateCrossProduct(subjects, objects, ops, result);
  }

  private Set<ResourceAccess> generateCrossProduct(Set<String> subjects, Set<String> objects, AccessRightSet ops, Set<ResourceAccess> result) {
    for (String s : subjects) {
      for (String obj : objects) {
        for (String op : ops) {
          if (s.equals(obj)) continue;

          result.add(new ResourceAccess(s, obj, op));
        }
      }
    }

    return result;
  }
}
