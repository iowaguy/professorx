package com.northeastern.analyzer;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.northeastern.policy.Accessor;
import com.northeastern.policy.MyPMException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.northeastern.policy.Policy;
import com.northeastern.policy.ResourceAccess;

import com.northeastern.policyengine.PolicyImpl;
import com.northeastern.policyengine.PolicyEngine;
import com.northeastern.policyengine.ExhaustiveAccessor;

public class Runner {
  static Logger logger = LogManager.getLogger(Runner.class);

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.printf("%d arguments provided. Need 1.", args.length);
      System.exit(1);
    }

    testOldPolicyEngine(args);
  }

  private static Map<String, Boolean> getDecisionMap(PolicyEngine policyEngine, Set<ResourceAccess> accesses) {
    return accesses.stream().collect(Collectors.toMap(ResourceAccess::toString,
                                                      (x) -> policyEngine.getDecision(x)));
  }

  private static void testOldPolicyEngine(String[] args) {

    Policy policy = new PolicyImpl(args[0]);
    PolicyEngine policyEngine = new PolicyEngine((PolicyImpl) policy);

    Accessor accessor = new ExhaustiveAccessor((PolicyImpl) policy);

    Set<ResourceAccess> accesses = null;
    try {
      accesses = accessor.generateAccesses();
    } catch (MyPMException e) {
      logger.fatal(() -> "Issue encountered generating accesses: " + e.getMessage());
      System.exit(1);
    }

    ResourceAccess testAccess1 = new ResourceAccess("U2", "OA2", "permission4");
    ResourceAccess testAccess2 = new ResourceAccess("UA4", "OA2", "permission4");

    Map<String, Boolean> decisionsBeforeMutation = getDecisionMap(policyEngine, accesses);

    Policy newPolicy = null;
    try {
      newPolicy = Mutations.ATTRIBUTE_EXCHANGE_SOURCE_EXPLICIT.applyExplicit(policy, "UA4", "OA2", "UA1", "OA2", null);
    } catch(MyPMException pm) {
      logger.fatal(() -> "Issue encountered mutating policy: " + pm.getMessage());
      System.exit(1);
    }

    PolicyEngine newPolicyEngine = new PolicyEngine((PolicyImpl) newPolicy);
    Accessor accessorAfterMutation = new ExhaustiveAccessor((PolicyImpl) policy, (PolicyImpl) newPolicy);

    Set<ResourceAccess> accessesAfterMutation = null;
    try {
      accessesAfterMutation = accessorAfterMutation.generateAccesses();
    } catch (MyPMException e) {
      logger.fatal(() -> "Issue encountered generating accesses: " + e.getMessage());
      System.exit(1);
    }


    Map<String, Boolean> decisionsAfterMutation = getDecisionMap(newPolicyEngine, accessesAfterMutation);

    for (ResourceAccess ra : accessesAfterMutation) {
      String raString = ra.toString();
      boolean beforeDecision = decisionsBeforeMutation.get(raString);
      boolean afterDecision = decisionsAfterMutation.get(raString);
      if (beforeDecision != afterDecision) {
        System.out.println("Before mutation " + raString + " was allowed: " + beforeDecision);
        System.out.println("After mutation " + raString + " was allowed: " + afterDecision);
        System.out.println("----------------------------------------------------------------");
      }
    }
  }
}
