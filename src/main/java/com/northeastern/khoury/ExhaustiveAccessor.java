package com.northeastern.khoury;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;

public class ExhaustiveAccessor extends Accessor {
  public ExhaustiveAccessor(Policy policy) {
    super(policy);
  }

  public Set<ResourceAccess> generateAccesses() throws PMException {
    // get all users and user attributes, U
    Set<Node> u = this.policy.search(NodeType.U, null);
    Set<Node> ua = this.policy.search(NodeType.UA, null);
    u.addAll(ua);

    // get all objects and object attributes, O
    Set<Node> o = this.policy.search(NodeType.O, null);
    Set<Node> oa = this.policy.search(NodeType.OA, null);
    oa.addAll(o);

    // get all possible permissions, P
    OperationSet possiblePermissions = new OperationSet();
    for (Node n : u) {
      Map<String, OperationSet> targetOps = this.policy.getSourceAssociations(n.getName());

      for (OperationSet opSet : targetOps.values()) {
        possiblePermissions.addAll(opSet);
      }
    }

    // generate all possible triples in UxOxP
    Set<ResourceAccess> crossProduct = generateCrossProduct(u, oa, possiblePermissions);

    // generate all possible triples in UxUxP
    return generateCrossProduct(u, u, possiblePermissions, crossProduct);
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
