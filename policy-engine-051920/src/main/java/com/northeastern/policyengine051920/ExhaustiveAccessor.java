package com.northeastern.policyengine051920;

import java.util.*;

import com.northeastern.policy.Accessor;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;

import com.northeastern.policy.Policy;
import com.northeastern.policy.ResourceAccess;
import com.northeastern.policy.MyPMException;


public class ExhaustiveAccessor implements Accessor {
 private List<PolicyImpl> policies;

  public ExhaustiveAccessor(PolicyImpl... policies) {
    this.policies = new ArrayList<>();
    Collections.addAll(this.policies, policies);
  }

  public Set<ResourceAccess> generateAccesses() throws MyPMException {
    // get all users and user attributes, U
    Set<Node> allU = new HashSet<>();
    for (PolicyImpl p : this.policies) {
      Set<Node> u = p.getGraph().search(NodeType.U, null);
      allU.addAll(u);

      Set<Node> ua = p.getGraph().search(NodeType.UA, null);
      allU.addAll(ua);
    }

    // get all objects and object attributes, O
    Set<Node> allOA = new HashSet<>();
    for (PolicyImpl p : this.policies) {
      Set<Node> o = p.getGraph().search(NodeType.O, null);
      allOA.addAll(o);

      Set<Node> oa = p.getGraph().search(NodeType.OA, null);
      allOA.addAll(oa);
    }

    // get all possible permissions, P
    OperationSet possiblePermissions = new OperationSet();
    for (Node u : allU) {
      for (PolicyImpl p : this.policies) {
        Map<String, OperationSet> targetOps = null;
        try {
          targetOps = p.getGraph().getSourceAssociations(u.getName());
        } catch (PMException e) {
          throw new MyPMException(e);
        }
        for (OperationSet opSet : targetOps.values()) {
          possiblePermissions.addAll(opSet);
        }
      }
    }

    // generate all possible triples in UxOxP
    Set<ResourceAccess> crossProduct = generateCrossProduct(allU, allOA, possiblePermissions);

    // generate all possible triples in UxUxP
    return generateCrossProduct(allU, allU, possiblePermissions, crossProduct);
  }

  private Set<ResourceAccess> generateCrossProduct(Set<Node> subjects, Set<Node> objects, OperationSet ops) {
    Set<ResourceAccess> result = new HashSet<>();
    return generateCrossProduct(subjects, objects, ops, result);
  }

  private Set<ResourceAccess> generateCrossProduct(Set<Node> subjects, Set<Node> objects, OperationSet ops, Set<ResourceAccess> result) {
    for (Node s : subjects) {
      for (Node obj : objects) {
        for (String op : ops) {
          if (s.equals(obj)) continue;

          result.add(new ResourceAccess(s.getName(), obj.getName(), op));
        }
      }
    }

    return result;
  }
}
