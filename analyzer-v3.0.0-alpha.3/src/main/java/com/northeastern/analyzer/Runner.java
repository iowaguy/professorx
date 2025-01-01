package com.northeastern.analyzer;

import static com.northeastern.policygraph.GraphRunner.createFile;
import static com.northeastern.policygraph.Mutation.mutateAddAssignment;
import static com.northeastern.policygraph.Mutation.mutateAddNode;
import static com.northeastern.policygraph.PolicyGraph.buildPMLString;
import static com.northeastern.policygraph.PolicyGraph.buildPrologString;

import com.northeastern.policygraph.Mutation;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
import com.northeastern.policygraph.PolicyGraph;

public class Runner {
  static Logger logger = LogManager.getLogger(Runner.class);
  private static String prologMutated = "analyzer-111822/src/main/resources/mutatedPolicy.pl";
  private static String prologRule = "prolog-policy-engine/src/main/resources/rules.pl";
  private static Random random;
  private static Mutation mutation;
  private static PolicyGraph mutatedGraph;
  
  public static void main(String[] args) {
    if (args.length == 3) {
      initializeRandom();
    }
    else if (args.length == 4) {
      Long fixedSeed = Long.parseLong(args[3]);
      initializeRandom(fixedSeed);
    } else {
      System.err.printf("%d arguments provided. \n "
          + "Need 3 for random mutation. Need 4 for fixed mutation.", args.length);
      System.exit(1);
    }

    if (!testNewPolicyEngineInitial(args)) {
      System.err.printf("Discrepancies found in initial policy!");
      System.exit(2);
    };
    PolicyGraph initialGraph = new PolicyGraph();
    mutate(initialGraph, 10);
  }

  public static void initializeRandom() {
    Long randomSeed = System.nanoTime();
    random = new Random(randomSeed);
    mutation = new Mutation(randomSeed);
    System.out.println("Used random seed " + randomSeed + "!");
  }

  public static void initializeRandom(long fixedSeed) {
    random = new Random(fixedSeed);
    mutation = new Mutation(fixedSeed);
    System.out.println("Used fixed seed " + fixedSeed + "!");
  }

  private static void mutate(PolicyGraph initialGraph, Integer rounds) {
    for (int round = 0; round < rounds; round++) {
      int choice = random.nextInt(3);
      System.out.println("----------------------");
      System.out.println(String.format("No. %d mutation begins!", round+1));
      if (choice == 0) {
        if (!mutateAddNodeImp(initialGraph, 1)){
          break;
        };
      } else if (choice == 1) {
        if (!mutateAddAssiImp(initialGraph, 1)){
          break;
        };
      } else if (choice == 2) {
        if (!mutateAddProhImp(initialGraph, 1)){
          break;
        }
      }
      initialGraph = mutatedGraph;
    }
  }

  private static boolean testNewPolicyEngineInitial(String[] args) {
    boolean consistent = true;
    Path fileName = java.nio.file.Path.of(args[0]);
    // Read the policy from disk
    try {
      String policyString = Files.readString(fileName);
      args[0] = policyString;
      consistent = testNewPolicyEngine(args);
    } catch (IOException io) {
      logger.fatal("Problem reading file: {}", io.getMessage());
    }
    return consistent;
  }
  private static boolean testNewPolicyEngine(String[] args) {
    boolean consistent = true;
    //    Path fileName = java.nio.file.Path.of(args[0]);
//    Policy policy = new PolicyImpl(fileName);
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
        String prologDecisionS = convertToString(prologDecision);
        String nistDecisionS = convertToString(nistDecision);
        printer.printRecord(writerDecisions, a.getSubject(), a.getObject(), a.getPermissions(), prologDecisionS, nistDecisionS);
//        printer.printRecord(writerDecisions, a.getSubject(), a.getObject(), a.getPermissions(), prologDecision, nistDecision);

        if (prologDecision != nistDecision) {
          System.out.println("NIST decision for " + a.toString() + ". Allowed? " + nistDecisionS);
          System.out.println("Prolog decision for " + a.toString() + ". Allowed? " + prologDecisionS);
          System.out.println("------------------");
          printer.printRecord(writerDiscrepencies, a.getSubject(), a.getObject(), a.getPermissions(), prologDecisionS, nistDecisionS);
          consistent = false;
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
//      Query unloadPolicy = new Query(
//          "unload_file('policy-graph/src/main/resources/translatePolicy.pl')");
//      unloadPolicy.hasSolution();

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
    return consistent;
  }

  private static String convertToString(boolean decision) {
    if (decision) {
      return "Grant";
    }
    return "Deny";
  }

  private static String[] getStrings(PolicyGraph graph) {
    List<String> proPMLString = new ArrayList<>();
    proPMLString.add(buildPrologString(graph.getNodeLists(), graph.getRelationLists()));
    proPMLString.add(buildPMLString(graph, graph.getNodeLists()));
    createFile(proPMLString.get(0), prologMutated);
    // Build the new args
    String[] newArgs = {proPMLString.get(1), prologRule, prologMutated};
    return newArgs;
  }

  private static boolean mutateAddNodeImp(PolicyGraph initialGraph, int rounds) {
    boolean consistent = true;
    for (int i = 0; i < rounds; i++) {
      mutatedGraph = mutation.mutateAddNode(initialGraph);
      consistent = testNewPolicyEngine(getStrings(mutatedGraph));
      if (!consistent) {
//        System.out.println(String.format("No. %d add NODE mutation terminated!", i+1));
        return consistent;
      };
//      System.out.println(String.format("No. %d round add NODE mutation completed!", i+1));
      initialGraph = mutatedGraph;
    }
    return consistent;
  }

  private static boolean mutateAddAssiImp(PolicyGraph initialGraph, int rounds) {
    boolean consistent = true;
    for (int i = 0; i < rounds; i++) {
      mutatedGraph = mutation.mutateAddAssignment(initialGraph);
      consistent = testNewPolicyEngine(getStrings(mutatedGraph));
      if (!consistent) {
//        System.out.println(String.format("No. %d add ASSI mutation terminated!", i+1));
        return consistent;
      }
//      System.out.println(String.format("No. %d round add ASSI mutation completed!", i+1));
    }
    return consistent;
  }

  private static boolean mutateAddProhImp(PolicyGraph initialGraph, int rounds) {
    boolean consistent = true;
    for (int i = 0; i < rounds; i++) {
      mutatedGraph = mutation.mutateAddProhibition(initialGraph);
      consistent = testNewPolicyEngine(getStrings(mutatedGraph));
      if (!consistent) {
        return consistent;
      }
    }
    return consistent;
  }
}
