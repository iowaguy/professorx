package com.northeastern.khoury;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;

public class ExhaustiveAccessor extends Accessor {
  public ExhaustiveAccessor(Policy... policies) {
    super(policies);
  }

  public Set<ResourceAccess> generateAccesses() throws PMException {
    // get all users and user attributes, U
    Set<Node> allU = new HashSet<>();
    for (Policy p : this.policies) {
      Set<Node> u = p.search(NodeType.U, null);
      allU.addAll(u);

      Set<Node> ua = p.search(NodeType.UA, null);
      allU.addAll(ua);
    }

    // get all objects and object attributes, O
    Set<Node> allOA = new HashSet<>();
    for (Policy p : this.policies) {
      Set<Node> o = p.search(NodeType.O, null);
      allOA.addAll(o);

      Set<Node> oa = p.search(NodeType.OA, null);
      allOA.addAll(oa);
    }

    // get all possible permissions, P
    OperationSet possiblePermissions = new OperationSet();
    for (Node u : allU) {
      for (Policy p : this.policies) {
        Map<String, OperationSet> targetOps = p.getSourceAssociations(u.getName());

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
