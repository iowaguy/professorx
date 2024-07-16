package com.northeastern.policygraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.traverse.DepthFirstIterator;

public class PolicyGraph extends DirectedMultigraph<NodeElement, Relation> {

  private static String prologPath = "policy-graph/src/main/resources/translatePolicy.pl";
  private static String pmlPath = "policy-graph/src/main/resources/translatePolicy.pal";

  private final static Class edgeClass = Relation.class;
  private Integer policyNumber;
  protected List<List<NodeElement>> nodeLists = new ArrayList<>();
  protected List<List<Relation>> relationLists = new ArrayList<>();
  protected static List<AccessRight> allPermissions;

  public PolicyGraph() {
    super(edgeClass);
    createDefaultGraph();
  }

  public PolicyGraph(Integer policyNumber, List<List<NodeElement>> nodeLists,
      List<List<Relation>> relationLists) {
    super(edgeClass);
    this.policyNumber = policyNumber;
    this.nodeLists = nodeLists;
    this.relationLists = relationLists;
    addVertices();
  }

  public void buildNodeList() {
    nodeLists.add(PolicyClass.getAllElements());
    nodeLists.add(User.getAllElements());
    nodeLists.add(UserAttribute.getAllElements());
    nodeLists.add(Ob.getAllElements());
    nodeLists.add(ObjectAttribute.getAllElements());
  }

  public void addVertices() {
    for (List<NodeElement> nodes: nodeLists) {
      for (NodeElement node: nodes) {
        this.addVertex(node);
      }
    }
  }

  public void buildRelationList() {
    relationLists.add(Assignment.getAllAssignments());
    relationLists.add(Association.getAllAssociations());
    relationLists.add(Prohibition.getAllProhibitions());
  }
  private void createDefaultGraph() {
    // create nodes
    User u1 = new User("u1");
    User u2 = new User("u2");
    User uNew = new User("uNew");
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

    buildNodeList();
    addVertices();

    // add permissions
    // TODO Is there a better way to create list of access right?
    AccessRight permission0 = new AccessRight("permission0");
    AccessRight permission1 = new AccessRight("permission1");
    AccessRight permission2 = new AccessRight("permission2");
    AccessRight permission3 = new AccessRight("permission3");
    AccessRight permission4 = new AccessRight("permission4");
    allPermissions = AccessRight.getAllPermissions();

    AccessRight[] ar0 = new AccessRight[]{permission0};
    AccessRight[] ar12 = new AccessRight[]{permission1, permission2};
    AccessRight[] ar4 = new AccessRight[]{permission4};
    AccessRight[] ar1 = new AccessRight[]{permission1};
    AccessRight[] ar2 = new AccessRight[]{permission2};

    // add edges
    // User and user attribute assignments
    this.addEdge(department, ua1, new Assignment());
    this.addEdge(department, ua2, new Assignment());
    this.addEdge(ua2, ua3, new Assignment());
    this.addEdge(department, ua4, new Assignment());
    this.addEdge(department, ua5, new Assignment());
    this.addEdge(ua5, ua6, new Assignment());
    this.addEdge(ua6, ua7, new Assignment());
    this.addEdge(ua4, ua6, new Assignment());
    this.addEdge(ua1, ua3, new Assignment());
    this.addEdge(ua7, u1, new Assignment());
    this.addEdge(ua1, u2, new Assignment());
    this.addEdge(ua3, uNew, new Assignment());
    this.addEdge(ua7, uNew, new Assignment());

    // Object and object attribute assignments
    this.addEdge(department, oa1, new Assignment());
    this.addEdge(department, oa2, new Assignment());
    this.addEdge(oa2, oa3, new Assignment());
    this.addEdge(department, oa4, new Assignment());
    this.addEdge(department, oa5, new Assignment());
    this.addEdge(oa5, oa6, new Assignment());
    this.addEdge(oa6, oa7, new Assignment());
    this.addEdge(oa4, oa6, new Assignment());
    this.addEdge(oa3, oa7, new Assignment());
    this.addEdge(oa1, oa3, new Assignment());
    this.addEdge(oa7, o1, new Assignment());

    this.addEdge(oa5, ua5, new Association(ar0));
    this.addEdge(oa5, ua4, new Association(ar0));
    this.addEdge(oa4, ua4, new Association(ar4));
    this.addEdge(oa2, ua4, new Association(ar4));
    this.addEdge(oa2, ua2, new Association(ar0));
    this.addEdge(oa4, ua1, new Association(ar12));
    this.addEdge(o1, u1, new Association(ar1));

    // TODO the target of prohibition should be a list of attributes
    this.addEdge(oa7, u2, new Prohibition(ar1));
    this.addEdge(o1, u2, new Prohibition(ar2));

    buildRelationList();
  }

  public static String buildOneTypePML(List<Relation> oneRelation) {
    StringBuilder oneRelationPML = new StringBuilder();
    for (Element one : oneRelation) {
      oneRelationPML.append(one.toStringPML() + System.lineSeparator());
    }
    return oneRelationPML.toString();
  }

  public static String buildOneTypeProlog(List<? extends Element> oneType) {
    StringBuilder oneTypeProlog = new StringBuilder();
    for (Element one : oneType) {
      oneTypeProlog.append(one.toStringProlog() + System.lineSeparator());
    }
    return oneTypeProlog.toString();
  }

  /**
   * Build string in Prolog.
   * @param nodeLists
   * @param relationLists
   */
  public static String buildPrologString(List<List<NodeElement>> nodeLists,
      List<List<Relation>> relationLists) {
    StringBuilder proString = new StringBuilder();
    String policyString = buildOneTypeProlog(nodeLists.get(0));
    String userString = buildOneTypeProlog(nodeLists.get(1));
    String uaString = buildOneTypeProlog(nodeLists.get(2));
    String obString = buildOneTypeProlog(nodeLists.get(3));
    String obaString = buildOneTypeProlog(nodeLists.get(4));
    String permString = AccessRight.buildAccessRights(allPermissions).get(0);
    String assiString = buildOneTypeProlog(relationLists.get(0));
    String assoString = buildOneTypeProlog(relationLists.get(1));
    String prohString = buildOneTypeProlog(relationLists.get(2));
    proString.append(userString).append(uaString).append(obString)
        .append(obaString).append(policyString).append(permString).
        append(assiString).append(assoString).append(prohString);
    return proString.toString();
  }

  /**
   * Build string in PML.
   * @param assignGraph
   * @param nodeLists
   * @return
   */
  public static String buildPMLString(PolicyGraph assignGraph,
      List<List<NodeElement>> nodeLists) {
    StringBuilder pmlString = new StringBuilder();
    pmlString.append(AccessRight.buildAccessRights(allPermissions).get(1));

    // use every policy class node to build the DFS trees
    for (NodeElement node : nodeLists.get(0)) {
      pmlString.append(depthTraverseOnePC(assignGraph, node));
    }
    return pmlString.toString();
  }

  private static String depthTraverseOnePC(PolicyGraph assignGraph,
      NodeElement startP) {
    // create nodes and relations in PML
    StringBuilder pmlString = new StringBuilder();
    Set<NodeElement> createdNodes = new HashSet<>();
    List<Relation> remainingAssignments = new ArrayList<>();
    List<Relation> associations = new ArrayList<>();
    List<Relation> prohibitions = new ArrayList<>();

    // build three relations list in one policy class
    for (NodeElement node: buildDFSLists(assignGraph, startP)) {
      for (Relation relation : assignGraph.outgoingEdgesOf(node)) {
        if (relation instanceof Assignment) {
          remainingAssignments.add(relation);
          System.out.println("Assignment relation during dfs: " + relation.toStringPML());
        }
        else if (relation instanceof Association) {
          associations.add(relation);
        }
        else {
          prohibitions.add(relation);
        }
      }
    }

    createdNodes.add(startP);
    pmlString.append(startP.toStringPML() + System.lineSeparator());
    for (NodeElement node : buildDFSLists(assignGraph, startP)) {
      Iterator<Relation> outgoingEdge = assignGraph.outgoingEdgesOf(node)
          .iterator();
      while (outgoingEdge.hasNext()) {
        Relation relation = outgoingEdge.next();
        if (relation instanceof Assignment) {
          NodeElement target = ((Assignment) relation).getTarget();
          if (!createdNodes.contains(target)) {
            pmlString.append(target.toStringPML() +
                " in '" + node.toString() + "';" + System.lineSeparator());
            createdNodes.add(target);
            remainingAssignments.remove(relation);
          }
        }
      }
    }
    // append after creating nodes
    pmlString.append(buildOneTypePML(remainingAssignments))
        .append(buildOneTypePML(associations))
        .append(buildOneTypePML(prohibitions));
    return pmlString.toString();
  }

  private static List<NodeElement> buildDFSLists(Graph
      assignGraph, Element start) {
    DepthFirstIterator iterator = new DepthFirstIterator(assignGraph, start);
    List<NodeElement> nodesDFSOrder = new ArrayList<>();
    while (iterator.hasNext()) {
      NodeElement node = (NodeElement) iterator.next();
      nodesDFSOrder.add(node);
    }
    System.out.println("PML DFS order node list: " + nodesDFSOrder);
    return nodesDFSOrder;
  }

  public List<List<NodeElement>> getNodeLists() {
    return nodeLists;
  }

  public List<List<Relation>> getRelationLists() {
    return relationLists;
  }
}
