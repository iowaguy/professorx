package com.northeastern.analyzer;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVFormat;

import com.northeastern.prologpolicyengine.PrologPolicyEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.northeastern.policy.Accessor;
import com.northeastern.policy.MyPMException;
import com.northeastern.policy.Policy;
import com.northeastern.policy.ResourceAccess;
import com.northeastern.policyengine.ExhaustiveAccessor;
import com.northeastern.policyengine.PolicyEngine;
import com.northeastern.policyengine.PolicyImpl;


public class Runner {
  static Logger logger = LogManager.getLogger(Runner.class);

  public static void main(String[] args) {
    if (args.length != 3) {
      System.err.printf("%d arguments provided. Need 3.", args.length);
      System.exit(1);
    }

    testNewPolicyEngine(args);
  }

  private static void testNewPolicyEngine(String[] args) {

    Policy policy = new PolicyImpl(args[0]);
    PolicyEngine policyEngine = null;
    try {
      policyEngine = new PolicyEngine((PolicyImpl) policy);
    } catch (MyPMException e) {
      logger.fatal(() -> "Issue encountered creating 111822 policy engine: " + e.getMessage());
      System.exit(1);
    }

    Accessor accessor = null;
    try {
      accessor = new ExhaustiveAccessor((PolicyImpl) policy);
    } catch (MyPMException e) {
      logger.fatal(() -> "Issue encountered creating exhaustive accessor: " + e.getMessage());
      System.exit(1);
    }

    Set<ResourceAccess> accesses = null;
    try {
      accesses = accessor.generateAccesses();
    } catch (MyPMException e) {
      logger.fatal(() -> "Issue encountered generating accesses: " + e.getMessage());
      System.exit(1);
    }

    // TODO do prolog stuff here
    // convert policy into prolog facts
    // load prolog rules
    PrologPolicyEngine prologPolicyEngine = null;
    try {
      prologPolicyEngine = new PrologPolicyEngine(args[1]);
      prologPolicyEngine.loadPolicy(args[2]);
    } catch (MyPMException e) {
      logger.fatal(() -> "Issue encountered loading prolog rules: " + e.getMessage());
      System.exit(1);
    }

    // create a writer
    Writer writerDecisions = null;
    Writer writerDiscrepencies = null;
    try {
      writerDecisions = Files.newBufferedWriter(Paths.get("decisions.csv"));
      writerDiscrepencies = Files.newBufferedWriter(Paths.get("discrepencies.csv"));
    } catch (IOException e) {
      logger.fatal(() -> "Issue encountered loading opening csv for writing: " + e.getMessage());
      System.exit(1);
    }

    String[] HEADERS = { "subject", "object", "permission", "prolog_decision", "nist_decision"};
    // write CSV file
    CSVFormat printer = CSVFormat.DEFAULT.builder()
            .setHeader(HEADERS)
            .setAutoFlush(true)
            .build();

    try {
      printer.printRecord(writerDecisions, HEADERS);
      printer.printRecord(writerDiscrepencies, HEADERS);
    } catch (IOException e) {
      logger.fatal(() -> "Issue encountered printing CSV header: " + e.getMessage());
      System.exit(1);
    }
    for (ResourceAccess a : accesses) {
      boolean nistDecision = false;
      boolean prologDecision = false;
      try {
        nistDecision = policyEngine.getDecision(a);
        prologDecision = prologPolicyEngine.getDecision(a);
      } catch (MyPMException e) {
        logger.fatal(() -> "Issue encountered making policy decision: " + e.getMessage());
        System.exit(1);
      }

      try {
        printer.printRecord(writerDecisions, a.getSubject(), a.getObject(), a.getPermissions(), prologDecision, nistDecision);

        if (prologDecision != nistDecision) {
          System.out.println("NIST decision for " + a.toString() + ". Allowed? " + nistDecision);
          System.out.println("Prolog decision for " + a.toString() + ". Allowed? " + prologDecision);
          System.out.println("------------------");
          printer.printRecord(writerDiscrepencies, a.getSubject(), a.getObject(), a.getPermissions(), prologDecision, nistDecision);
        }
      } catch (IOException e) {
        logger.fatal(() -> "Issue encountered printing CSV record: " + e.getMessage());
        System.exit(1);
      }
    }

    try {
      // close the writer
      writerDecisions.close();
      writerDiscrepencies.close();

    } catch (IOException e) {
      logger.fatal(() -> "Issue encountered closing CSV file: " + e.getMessage());
      System.exit(1);
    }
//    Policy newPolicy = null;
//    try {
//      newPolicy = Mutations.ATTRIBUTE_EXCHANGE_SOURCE_EXPLICIT.applyExplicit(policy, "UA4", "OA2", "UA1", "OA2", null);
//    } catch(MyPMException pm) {
//      logger.fatal(() -> "Issue encountered mutating policy: " + pm.getMessage());
//      System.exit(1);
//    }
//
//    PolicyEngine051920 newPolicyEngine = new PolicyEngine051920((PolicyImpl051920) newPolicy);
//    Accessor accessorAfterMutation = new ExhaustiveAccessor051920((PolicyImpl051920) policy, (PolicyImpl051920) newPolicy);
//
//    Set<ResourceAccess> accessesAfterMutation = null;
//    try {
//      accessesAfterMutation = accessorAfterMutation.generateAccesses();
//    } catch (MyPMException e) {
//      logger.fatal(() -> "Issue encountered generating accesses: " + e.getMessage());
//      System.exit(1);
//    }
//
//    for (ResourceAccess a : accessesAfterMutation) {
//      boolean b = newPolicyEngine.getDecision(a);
//      System.out.println(a.toString() + ". Allowed after mutation? " + b);
//    }
//
//
//    System.out.println("-----------Explicit access test------------");
//    ResourceAccess testAccess1 = new ResourceAccess("UA4", "OA2", "permission4");
//    boolean b = policyEngine.getDecision(testAccess1);
//    System.out.println("Explicit access: " + testAccess1.toString() + ". Allowed before mutation? " + b);
//
//    b = newPolicyEngine.getDecision(testAccess1);
//    System.out.println("Explicit access: " + testAccess1.toString() + ". Allowed after mutation? " + b);
//
//
//    ResourceAccess testAccess2 = new ResourceAccess("UA1", "OA2", "permission4");
//    b = policyEngine.getDecision(testAccess2);
//    System.out.println("New explicit access: " + testAccess2.toString() + ". Allowed before mutation? " + b);
//
//    b = newPolicyEngine.getDecision(testAccess2);
//    System.out.println("New explicit access: " + testAccess2.toString() + ". Allowed after mutation? " + b);
  }
}
