package com.northeastern.analyzer;

import static com.northeastern.policygraph.GraphRunner.createFile;
import static com.northeastern.policygraph.Mutation.mutateAddNode;
import static com.northeastern.policygraph.PolicyGraph.buildPMLPolicy;
import static com.northeastern.policygraph.PolicyGraph.buildPrologPolicy;

import com.northeastern.policygraph.Mutation;
import com.northeastern.policygraph.MutationStatus;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;

import com.northeastern.prologpolicyengine.PrologPolicyEngine;
import org.apache.commons.csv.CSVPrinter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.northeastern.policy.Accessor;
import com.northeastern.policy.MyPMException;
import com.northeastern.policy.ResourceAccess;
import com.northeastern.policyengine.ExhaustiveAccessor;
import com.northeastern.policyengine.PolicyEngine;
import com.northeastern.policyengine.PolicyImpl;
import com.northeastern.policygraph.PolicyGraph;

public class Runner {
  static Logger logger = LogManager.getLogger(Runner.class);
  private static final String prologMutated = "policy-graph/src/main/resources/mutantPolicy.pl";
  private static final String pmlMutated = "policy-graph/src/main/resources/mutantPolicy.pal";
  private static final String prologRule = "prolog-policy-engine/src/main/resources/rules.pl";
  private static Random random;
  private static Mutation mutation;
  private static PolicyGraph mutatedGraph;
  private static Integer roundNo = 0;
  private static Boolean consistent = true;
  private static MutationStatus mutationStatus;
  
  public static void main(String[] args) {
    if (args.length == 4) {
      initializeRandom();
    } else if (args.length == 5) {
      Long fixedSeed = Long.parseLong(args[4]);
      initializeRandom(fixedSeed);
    } else {
      System.err.printf("%d arguments provided. \n "
          + "Need 4 for random mutation. Need 5 for fixed mutation.", args.length);
      System.exit(1);
    }

    Path prologRulesPath = java.nio.file.Path.of(args[0]);
    PolicyImpl pmlPolicy = new PolicyImpl(Path.of(args[1]));
    PolicyImpl prologPolicy = new PolicyImpl(Path.of(args[2]));
    Integer rounds = Integer.parseInt(args[3]);
    testNewPolicyEngine(pmlPolicy, prologRulesPath, prologPolicy);
    if (!consistent) {
      System.err.print("Discrepancies found in initial policy!");
      System.exit(2);
    };
    PolicyGraph initialGraph = new PolicyGraph();
    mutate(initialGraph, rounds);
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
    List<String[]> allDecisions = new ArrayList<>();
    List<String[]> timerRecords = new ArrayList<>();
    String[] HEADERS =
        {"round_no", "mutation", "subject", "object", "permission", "prolog_decision", "nist_decision"};
    String[] HEADERS_TIMER =
        {"round_no", "duration"};

    while (roundNo < rounds) {
      roundNo++;
      List<String[]> roundDecisons = new ArrayList<>();
      long startTime = System.nanoTime();
      int choice = random.nextInt(3);
      System.out.println("----------------------");
      System.out.println(String.format("No.%d mutation begins!", roundNo));
      if (choice == 0) {
        roundDecisons = mutateAddNodeImp(initialGraph, 1);
      } else if (choice == 1) {
        roundDecisons = mutateAddAssiImp(initialGraph, 1);
      } else if (choice == 2) {
        roundDecisons = mutateAddProhImp(initialGraph, 1);
      }
      allDecisions.addAll(roundDecisons);
      timerRecords.add(new String[]{String.valueOf(roundNo),
          String.valueOf(System.nanoTime()-startTime)});
      if (!consistent) {
        break;
      }
      initialGraph = mutatedGraph;
    }

    // Write to files
    writeToCSV("decisions.csv", HEADERS, allDecisions);
    writeToCSV("timer.csv", HEADERS_TIMER, timerRecords);
    if (!consistent) {
      List<String[]> discrepancies = new ArrayList<>();
      discrepancies.add(allDecisions.get(roundNo - 1));
      writeToCSV("discrepancies.csv", HEADERS, discrepancies);
    }
    System.out.println("Finished with " + roundNo + " rounds");
  }

  private static List<String[]> testNewPolicyEngine(
      PolicyImpl pmlPolicy, Path prologRulesPath, PolicyImpl prologPolicy) {
    List<String[]> results = new ArrayList<>();
    PolicyEngine policyEngine = null;
    try {
      policyEngine = new PolicyEngine(pmlPolicy);
    } catch (MyPMException e) {
      logger.fatal(() -> "Issue encountered creating v3.0.0-alpha.3 policy engine: " + e.getMessage());
      System.exit(1);
    }
    Accessor accessor = null;
    try {
      accessor = new ExhaustiveAccessor(pmlPolicy);
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
      prologPolicyEngine = new PrologPolicyEngine(prologRulesPath);
      prologPolicyEngine.loadPolicy(prologPolicy.getPolicyPath());
    } catch (MyPMException e) {
      logger.fatal(() -> "Issue encountered loading prolog rules: " + e.getMessage());
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

      String prologDecisionS = convertToString(prologDecision);
      String nistDecisionS = convertToString(nistDecision);
      results.add(new String[]{String.valueOf(roundNo), Mutation.getCurMutation(), a.getSubject(),
          a.getObject(), a.getPermissions(), prologDecisionS, nistDecisionS});
      if (prologDecision != nistDecision) {
        System.out.println("NIST decision for " + a.toString() + ". Allowed? " + nistDecision);
        System.out.println("Prolog decision for " + a.toString() + ". Allowed? " + prologDecision);
        System.out.println("------------------");
        consistent = false;
        }
      }
    return results;
  }

  private static void writeToCSV(String fileName, String[] headers, List<String[]> data) {
    try (Writer writer = Files.newBufferedWriter(Paths.get(fileName))) {
      CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
          .setHeader(headers)
          .setAutoFlush(true)
          .build();

      try (CSVPrinter printer = new CSVPrinter(writer, csvFormat)) {
        printer.printRecords(data);
      }
    } catch (IOException e) {
      logger.fatal(() -> "Issue encountered loading opening csv for writing: " + e.getMessage());
      System.exit(1);
    }
  }

  private static String convertToString(boolean decision) {
    if (decision) {
      return "Grant";
    }
    return "Deny";
  }

  private static List<String[]> mutateAddNodeImp(PolicyGraph initialGraph, int rounds) {
    List<String[]> roundDecisions = new ArrayList<>();
    for (int i = 0; i < rounds; i++) {
      mutatedGraph = mutation.mutateAddNode(initialGraph).getPolicyGraph();
      roundDecisions.addAll(evalMutation(mutatedGraph));
      if (!consistent) {
        return roundDecisions;
      }
      initialGraph = mutatedGraph;
    }
    return roundDecisions;
  }

  private static List<String[]> evalMutation(PolicyGraph mutatedGraph) {
      PolicyImpl newPMLPolicy = buildPMLPolicy(mutatedGraph, mutatedGraph.getNodeLists());
      PolicyImpl newPrologPolicy = buildPrologPolicy(mutatedGraph.getNodeLists(), mutatedGraph.getRelationLists());
      createFile(newPMLPolicy.getPolicyString(), pmlMutated);
      createFile(newPrologPolicy.getPolicyString(), prologMutated);
      return testNewPolicyEngine(newPMLPolicy, Path.of(prologRule), newPrologPolicy);
  }

  private static List<String[]> mutateAddAssiImp(PolicyGraph initialGraph, int rounds) {
    List<String[]> roundDecisions = new ArrayList<>();
    for (int i = 0; i < rounds; i++) {
      mutationStatus = mutation.mutateAddAssignment(initialGraph);
      Mutation.skipNumber = 0;
      // when it is impossible to add more assignments in the graph, add a node instead
      if (!mutationStatus.isSuccess()) {
        mutationStatus = mutation.mutateAddNode(mutationStatus.getPolicyGraph());
      }
      mutatedGraph = mutationStatus.getPolicyGraph();
      roundDecisions.addAll(evalMutation(mutatedGraph));
      if (!consistent) {
        return roundDecisions;
      }
    }
    return roundDecisions;
  }

  private static List<String[]> mutateAddProhImp(PolicyGraph initialGraph, int rounds) {
    List<String[]> roundDecisions = new ArrayList<>();
    for (int i = 0; i < rounds; i++) {
      mutatedGraph = mutation.mutateAddProhibition(initialGraph).getPolicyGraph();
      roundDecisions.addAll(evalMutation(mutatedGraph));
      if (!consistent) {
        return roundDecisions;
      }
    }
    return roundDecisions;
  }
}
