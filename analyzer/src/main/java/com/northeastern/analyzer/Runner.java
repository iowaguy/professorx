package com.northeastern.analyzer;

import java.util.Set;

import com.northeastern.policy.Accessor;
import com.northeastern.policy.MyPMException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.northeastern.policy.Policy;
import com.northeastern.policy.ResourceAccess;

import com.northeastern.policyengine051920.PolicyImpl;

import com.northeastern.policyengine051920.PolicyEngine;
import com.northeastern.policyengine051920.ExhaustiveAccessor;

public class Runner {
  static Logger logger = LogManager.getLogger(Runner.class);

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.printf("%d arguments provided. Need 1.", args.length);
      System.exit(1);
    }

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

    for (ResourceAccess a : accesses) {
      boolean b = policyEngine.getDecision(a);
      System.out.println(a.toString() + ". Allowed? " + b);
    }

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

    for (ResourceAccess a : accessesAfterMutation) {
      boolean b = newPolicyEngine.getDecision(a);
      System.out.println(a.toString() + ". Allowed after mutation? " + b);
    }


    System.out.println("-----------Explicit access test------------");
    ResourceAccess testAccess1 = new ResourceAccess("UA4", "OA2", "permission4");
    boolean b = policyEngine.getDecision(testAccess1);
    System.out.println("Explicit access: " + testAccess1.toString() + ". Allowed before mutation? " + b);

    b = newPolicyEngine.getDecision(testAccess1);
    System.out.println("Explicit access: " + testAccess1.toString() + ". Allowed after mutation? " + b);


    ResourceAccess testAccess2 = new ResourceAccess("UA1", "OA2", "permission4");
    b = policyEngine.getDecision(testAccess2);
    System.out.println("New explicit access: " + testAccess2.toString() + ". Allowed before mutation? " + b);

    b = newPolicyEngine.getDecision(testAccess2);
    System.out.println("New explicit access: " + testAccess2.toString() + ". Allowed after mutation? " + b);
  }
}
