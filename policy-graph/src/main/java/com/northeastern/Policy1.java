package com.northeastern;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.traverse.DepthFirstIterator;
import java.util.Iterator;
import java.util.Set;

public class Policy1 {

  private static String prologPath = "policy-graph/src/main/resources/translatePolicy.pl";
  private static String pmlPath = "policy-graph/src/main/resources/translatePolicy.pal";
  protected static List<NodeElement> allPolicyClasses;
  protected static List<NodeElement> allUserAttributes;
  protected static List<NodeElement> allUsers;
  protected static List<NodeElement> allObjects;
  protected static List<NodeElement> allObjectAttributes;
  protected static List<Relation> allAssignments = new ArrayList<>();
  protected static List<Relation> allAssociations = new ArrayList<>();
  protected static List<Relation> allProhibitions = new ArrayList<>();
  protected static List<Element> allNodesDFSOrder = new ArrayList<>();

  private Policy1() {
  }

  public static String buildOneTypePML(List<Relation> oneRelation) {
    StringBuilder oneRelationPML = new StringBuilder();
    for (Element one: oneRelation) {
      oneRelationPML.append(one.toStringPML() + System.lineSeparator());
    }
    return oneRelationPML.toString();
  }

  public static String buildOneTypeProlog(List<? extends Element> oneType) {
    StringBuilder oneTypeProlog = new StringBuilder();
    for (Element one: oneType) {
      oneTypeProlog.append(one.toStringProlog() + System.lineSeparator());
    }
    return oneTypeProlog.toString();
  }

  public static void writeNodeElementsProlog(List<NodeElement> allKinds) {
    // create prolog file
    BufferedWriter writerProlog = null;
    try {
      writerProlog = Files.newBufferedWriter(Paths.get(prologPath));
    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.exit(1);
    }
    String policyString = null;
    String userString = null;
    String uaString = null;
    String obString = null;
    String obaString = null;
    for (NodeElement node : allKinds) {
      if (node instanceof PolicyClass) {
        allPolicyClasses = node.getAllElements();
        policyString = buildOneTypeProlog(allPolicyClasses);
      }
      else if (node instanceof User) {
        allUsers = node.getAllElements();
        userString = buildOneTypeProlog(allUsers);
      }
      else if (node instanceof UserAttribute) {
        allUserAttributes = node.getAllElements();
        uaString = buildOneTypeProlog(allUserAttributes);
      }
      else if (node instanceof Ob) {
        allObjects = node.getAllElements();
        obString = buildOneTypeProlog(allObjects);
      }
      else if (node instanceof ObjectAttribute) {
        allObjectAttributes = node.getAllElements();
        obaString = buildOneTypeProlog(allObjectAttributes);
      }
    }
    try {
      writerProlog.append(userString);
      writerProlog.append(uaString);
      writerProlog.append(obString);
      writerProlog.append(obaString);
      writerProlog.append(policyString);
      writerProlog.flush();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.exit(1);
    }
  }

  public static void main(String[] args) throws IOException {
    Graph<Element, Relation> assignGraph = createAssignGraph();

    Element startP =  assignGraph.
        vertexSet().stream().filter(pc -> pc instanceof PolicyClass).findAny()
        .get();

    // perform a graph traversal starting from that vertex
    depthTraverseAssignGraph(assignGraph, startP);
  }

  /**
   * Creates a simple directed graph based on policy node elements.
   *
   * @return a graph based on node objects.
   */
  public static Graph<Element, Relation> createAssignGraph() {
    Graph<Element, Relation> g = new DirectedMultigraph<>(Relation.class);

    // Create nodes
    User u1 = new User("u1");
    User u2 = new User("u2");
    UserAttribute ua1 = new UserAttribute("ua1");
    UserAttribute ua2 = new UserAttribute("ua2");
    UserAttribute ua3 = new UserAttribute("ua3");
    UserAttribute ua4 = new UserAttribute("ua4");
    UserAttribute ua5 = new UserAttribute("ua5");
    UserAttribute ua6 = new UserAttribute("ua6");
    UserAttribute ua7 = new UserAttribute("ua7");
    Ob o1 = new Ob("o1");
    ObjectAttribute oa1 = new ObjectAttribute("oa1");
    ObjectAttribute oa2 = new ObjectAttribute("oa2");
    ObjectAttribute oa3 = new ObjectAttribute("oa3");
    ObjectAttribute oa4 = new ObjectAttribute("oa4");
    ObjectAttribute oa5 = new ObjectAttribute("oa5");
    ObjectAttribute oa6 = new ObjectAttribute("oa6");
    ObjectAttribute oa7 = new ObjectAttribute("oa7");
    PolicyClass department = new PolicyClass("department");

    List<NodeElement> allKinds = new ArrayList<>();
    allKinds.add(department);
    allKinds.add(ua1);
    allKinds.add(u1);
    allKinds.add(o1);
    allKinds.add(oa1);

    // Add permissions and write to the files
    //TODO Is there a better way to create list of access right?
    AccessRight permission0 = new AccessRight("permission0");
    AccessRight permission1 = new AccessRight("permission1");
    AccessRight permission2 = new AccessRight("permission2");
    AccessRight permission3 = new AccessRight("permission3");
    AccessRight permission4 = new AccessRight("permission4");

    List<AccessRight> allPermissions = AccessRight.getAllPermissions();

    AccessRight[] ar0 = new AccessRight[]{permission0};
    AccessRight[] ar12 = new AccessRight[]{permission1, permission2};
    AccessRight[] ar4 = new AccessRight[]{permission4};
    AccessRight[] ar1 = new AccessRight[]{permission1};
    AccessRight[] ar2 = new AccessRight[]{permission2};

    // Write nodes to prolog and get the lists of nodes
    writeNodeElementsProlog(allKinds);
    // Add the vertices
    addVertices(g, allPolicyClasses);
    addVertices(g, allUsers);
    addVertices(g, allUserAttributes);
    addVertices(g, allObjects);
    addVertices(g, allObjectAttributes);

    // The direction is opposite
    // User and user attribute assignments
    g.addEdge(department, ua1, new Assignment());
    g.addEdge(department, ua2, new Assignment());
    g.addEdge(ua2, ua3, new Assignment());
    g.addEdge(department, ua4, new Assignment());
    g.addEdge(department, ua5, new Assignment());
    g.addEdge(ua5, ua6, new Assignment());
    g.addEdge(ua6, ua7, new Assignment());
    g.addEdge(ua4, ua6, new Assignment());
    g.addEdge(ua1, ua3, new Assignment());
    g.addEdge(ua7, u1, new Assignment());
    g.addEdge(ua1, u2, new Assignment());

    // Object and object attribute assignments
    g.addEdge(department, oa1, new Assignment());
    g.addEdge(department, oa2, new Assignment());
    g.addEdge(oa2, oa3, new Assignment());
    g.addEdge(department, oa4, new Assignment());
    g.addEdge(department, oa5, new Assignment());
    g.addEdge(oa5, oa6, new Assignment());
    g.addEdge(oa6, oa7, new Assignment());
    g.addEdge(oa4, oa6, new Assignment());
    g.addEdge(oa3, oa7, new Assignment());
    g.addEdge(oa1, oa3, new Assignment());
    g.addEdge(oa7, o1, new Assignment());

    g.addEdge(oa5, ua5, new Association(ar0));
    g.addEdge(oa5, ua4, new Association(ar0));
    g.addEdge(oa4, ua4, new Association(ar4));
    g.addEdge(oa2, ua4, new Association(ar4));
    g.addEdge(oa2, ua2, new Association(ar0));
    g.addEdge(oa4, ua1, new Association(ar12));
    g.addEdge(o1, u1, new Association(ar1));

    //TODO the target of prohibition should be a list of attributes
    g.addEdge(oa7, u2, new Prohibition(ar1));
    g.addEdge(o1, u2, new Prohibition(ar2));

    AccessRight.writeAccessRights(allPermissions);
    return g;
  }

  private static void addVertices(Graph<Element, Relation> g, List<NodeElement> nodes) {
    for (Element node: nodes) {
      g.addVertex(node);
    }
  }

  /**
   * Traverse a graph in depth-first order and print the vertices.
   *
   * @param assignGraph a graph based on policy node elements
   * @param start       the vertex where the traversal should start
   */
  private static void depthTraverseAssignGraph(Graph<Element, Relation> assignGraph,
      Element start) {
    Iterator<Element> iterator = new DepthFirstIterator<>(assignGraph, start);
    while (iterator.hasNext()) {
      Element node = iterator.next();
      allNodesDFSOrder.add(node);
      Set<Relation> edgeSet = assignGraph.outgoingEdgesOf(node);
      for (Relation edge : edgeSet) {
        if (edge instanceof Assignment) {
          allAssignments.add(edge);
        }
        else if (edge instanceof Association) {
          allAssociations.add(edge);
        }
        else if (edge instanceof Prohibition) {
          allProhibitions.add(edge);
        }
      }
    }
    // append after access rights in Prolog
    String assignmentStringProlog = buildOneTypeProlog(allAssignments);
    String associationStringProlog = buildOneTypeProlog(allAssociations);
    String prohibitionStringProlog = buildOneTypeProlog(allProhibitions);
    try {
      FileWriter writerProlog = new FileWriter(prologPath, true);
      writerProlog.append(assignmentStringProlog);
      writerProlog.append(associationStringProlog);
      writerProlog.append(prohibitionStringProlog);
      writerProlog.flush();
    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.exit(1);
    }

    // create nodes and relations in PML
    try {
      FileWriter writerPML = new FileWriter(pmlPath, true);
      List<Element> createdNodes = new ArrayList<>();
      List<Relation> remainingAssignments = allAssignments;
      for (Element node: allNodesDFSOrder) {
        StringBuilder createNodeString = new StringBuilder();
        if (assignGraph.incomingEdgesOf(node).isEmpty()) { // when it is a policy class
          createNodeString.append(node.toStringPML() + System.lineSeparator());
          createdNodes.add(node);
        }
        Set<Relation> outgoingEdges = assignGraph.outgoingEdgesOf(node);
        Iterator<Relation> outgoingEdge = outgoingEdges.iterator();
        while (outgoingEdge.hasNext()) {
          Relation relation = outgoingEdge.next();
          if (relation instanceof Assignment) {
            NodeElement target = ((Assignment) relation).getTarget();
            if (!createdNodes.contains(target)) {
              createNodeString.append(target.toStringPML() +
                  " in \'" + node.toString() + "\';" + System.lineSeparator());
              createdNodes.add(target);
              remainingAssignments.remove(relation);
            }
          }
        }
        writerPML.append(createNodeString);
      }
      // append after creating nodes
      String assignmentStringPML = buildOneTypePML(remainingAssignments);
      String associationStringPML = buildOneTypePML(allAssociations);
      String prohibitionStringPML = buildOneTypePML(allProhibitions);
      writerPML.append(assignmentStringPML);
      writerPML.append(associationStringPML);
      writerPML.append(prohibitionStringPML);
      writerPML.flush();
    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.exit(1);
    }
  }
}
