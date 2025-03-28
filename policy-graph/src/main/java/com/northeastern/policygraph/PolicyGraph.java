package com.northeastern.policygraph;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.northeastern.policyengine.PolicyImpl;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;

public class PolicyGraph extends DirectedMultigraph<NodeElement, Relation> {

  private static Path prologPath = Path.of("policy-graph/src/main/resources/mutantPolicy.pl");
  private static Path pmlPath = Path.of("policy-graph/src/main/resources/mutantPolicy.pal");

  private final static Class edgeClass = Relation.class;
  protected static List<List<NodeElement>> nodeLists;
  protected static List<List<Relation>> relationLists;
  protected static List<String> allPermissions;
  private Set<NodeElement> createdNodesOneTra = new HashSet<>();
  private Set<Relation> remainingAssignments = new HashSet<>();
  private List<Relation> associations = new ArrayList<>();
  private List<Relation> prohibitions = new ArrayList<>();
  public static Integer maxNodeNumber;

  public PolicyGraph() {
    super(edgeClass);
    createDefaultGraphSimple();
//    createDefaultGraphComplex();
//    createChineseWallPolicy();
//    createMutateSimple(); // Add association discrepancy
  }

  public void addVertices(List<NodeElement> nodes) {
    for (NodeElement node: nodes) {
      this.addVertex(node);
    }
  }

  private void createDefaultGraphSimple() {
    maxNodeNumber = 2;
    List<NodeElement> tempNodeList = new ArrayList<>();
    // create nodes
    PolicyClass aClass = new PolicyClass("class");
//    PolicyClass pc2 = new PolicyClass("pc2"); // Introduce discrepancy
//    tempNodeList.add(pc2); // Introduce discrepancy
    tempNodeList.add(aClass);
    User u1 = new User("u1");
    User u2 = new User("u2");
    tempNodeList.add(u1);
    tempNodeList.add(u2);
    UserAttribute ua1 = new UserAttribute("ua1");
    UserAttribute ua2 = new UserAttribute("ua2");
    tempNodeList.add(ua1);
    tempNodeList.add(ua2);
    Ob o1 = new Ob("o1");
//    Ob o2 = new Ob("o2"); // Introduce discrepancy
//    tempNodeList.add(o2); // Introduce discrepancy
    tempNodeList.add(o1);
    ObjectAttribute oa1 = new ObjectAttribute("oa1");
    tempNodeList.add(oa1);
    addVertices(tempNodeList);
    // add permissions
    // TODO Is there a better way to create list of access right?
    AccessRight permission1 = new AccessRight("p1");
    AccessRight permission2 = new AccessRight("p2");
    allPermissions = AccessRight.getAllPermissions().stream().toList();
    AccessRight[] ar1 = new AccessRight[]{permission1};
    AccessRight[] ar2 = new AccessRight[]{permission2};

    // add edges
    // User and user attribute assignments
    this.addEdge(aClass, ua1, new Assignment());
    this.addEdge(aClass, ua2, new Assignment());
    this.addEdge(ua1, u1, new Assignment());
    this.addEdge(ua2, u2, new Assignment());

    // Object and object attribute assignments
    this.addEdge(aClass, oa1, new Assignment());
    this.addEdge(oa1, o1, new Assignment());
//    this.addEdge(oa1, o2, new Assignment()); // Introduce discrepancy
//    this.addEdge(pc2, o2, new Assignment()); // Introduce discrepancy

    // Associations and prohibitions
    this.addEdge(oa1, ua1, new Association(ar1));
    this.addEdge(oa1, ua2, new Association(ar2));

    // TODO the target of prohibition should be a list of attributes
    this.addEdge(o1, u2, new Prohibition(ar1));
    syncLists();
  }

  private void createMutateSimple() {
    maxNodeNumber = 2;
    List<NodeElement> tempNodeList = new ArrayList<>();
    // create nodes
    PolicyClass aClass = new PolicyClass("class");
    tempNodeList.add(aClass);
    User u1 = new User("u1");
    User u2 = new User("u2");
    tempNodeList.add(u1);
    tempNodeList.add(u2);
    UserAttribute ua1 = new UserAttribute("ua1");
    UserAttribute ua2 = new UserAttribute("ua2");
    tempNodeList.add(ua1);
    tempNodeList.add(ua2);
    Ob o1 = new Ob("o1");
    tempNodeList.add(o1);
    ObjectAttribute oa1 = new ObjectAttribute("oa1");
    tempNodeList.add(oa1);
    addVertices(tempNodeList);
    // add permissions
    // TODO Is there a better way to create list of access right?
    AccessRight permission1 = new AccessRight("p1");
    AccessRight permission2 = new AccessRight("p2");
    allPermissions = AccessRight.getAllPermissions().stream().toList();
    AccessRight[] ar1 = new AccessRight[]{permission1};
    AccessRight[] ar2 = new AccessRight[]{permission2};

    // add edges
    // User and user attribute assignments
    this.addEdge(aClass, ua1, new Assignment());
    this.addEdge(aClass, ua2, new Assignment());
    this.addEdge(ua1, u1, new Assignment());
    this.addEdge(ua2, u2, new Assignment());

    // Object and object attribute assignments
    this.addEdge(aClass, oa1, new Assignment());
    this.addEdge(oa1, o1, new Assignment());

    // Associations and prohibitions
    this.addEdge(oa1, ua1, new Association(ar1));
    this.addEdge(oa1, ua2, new Association(ar2));
    this.addEdge(oa1, ua2, new Association(ar1)); // introduce discrepancy

    // TODO the target of prohibition should be a list of attributes
    this.addEdge(o1, u2, new Prohibition(ar1));
    syncLists();
  }

  private void createDefaultGraphComplex() {
    maxNodeNumber = 7;
    List<NodeElement> tempNodeList = new ArrayList<>();
    // create nodes
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

    tempNodeList.add(department);
    tempNodeList.add(u1);
    tempNodeList.add(u2);
    tempNodeList.add(ua1);
    tempNodeList.add(ua2);
    tempNodeList.add(ua3);
    tempNodeList.add(ua4);
    tempNodeList.add(ua5);
    tempNodeList.add(ua6);
    tempNodeList.add(ua7);
    tempNodeList.add(o1);
    tempNodeList.add(oa1);
    tempNodeList.add(oa2);
    tempNodeList.add(oa3);
    tempNodeList.add(oa4);
    tempNodeList.add(oa5);
    tempNodeList.add(oa6);
    tempNodeList.add(oa7);

    addVertices(tempNodeList);

    // add permissions
    AccessRight permission0 = new AccessRight("permission0");
    AccessRight permission1 = new AccessRight("permission1");
    AccessRight permission2 = new AccessRight("permission2");
    AccessRight permission3 = new AccessRight("permission3");
    AccessRight permission4 = new AccessRight("permission4");
    allPermissions = AccessRight.getAllPermissions().stream().toList();

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

    this.addEdge(oa7, u2, new Prohibition(ar1));
    this.addEdge(o1, u2, new Prohibition(ar2));

    syncLists();
  }

  private void createChineseWallPolicy() {
    maxNodeNumber = 9;
    List<NodeElement> tempNodeList = new ArrayList<>();
    // create nodes
    User u1 = new User("u1");
    UserAttribute ua1 = new UserAttribute("ua1");
    Ob o1 = new Ob("o1");
    Ob o2 = new Ob("o2");
    Ob o3 = new Ob("o3");
    Ob o4 = new Ob("o4");
    Ob o5 = new Ob("o5");
    Ob o6 = new Ob("o6");
    ObjectAttribute oa1 = new ObjectAttribute("oa1");
    ObjectAttribute oa2 = new ObjectAttribute("oa2");
    ObjectAttribute oa3 = new ObjectAttribute("oa3");
    ObjectAttribute oa4 = new ObjectAttribute("oa4");
    ObjectAttribute oa5 = new ObjectAttribute("oa5");
    ObjectAttribute oa6 = new ObjectAttribute("oa6");
    ObjectAttribute oa7 = new ObjectAttribute("oa7");
    ObjectAttribute oa8 = new ObjectAttribute("oa8");
    ObjectAttribute oa9 = new ObjectAttribute("oa9"); // "Data Store"
    PolicyClass chineseWall = new PolicyClass("chineseWall");

    tempNodeList.add(chineseWall);
    tempNodeList.add(u1);
    tempNodeList.add(ua1);
    tempNodeList.add(o1);
    tempNodeList.add(o2);
    tempNodeList.add(o3);
    tempNodeList.add(o4);
    tempNodeList.add(o5);
    tempNodeList.add(o6);
    tempNodeList.add(oa1);
    tempNodeList.add(oa2);
    tempNodeList.add(oa3);
    tempNodeList.add(oa4);
    tempNodeList.add(oa5);
    tempNodeList.add(oa6);
    tempNodeList.add(oa7);
    tempNodeList.add(oa8);
    tempNodeList.add(oa9);

    addVertices(tempNodeList);

    // add permissions
    AccessRight read = new AccessRight("read");
    AccessRight write = new AccessRight("write");
    allPermissions = AccessRight.getAllPermissions().stream().toList();

    AccessRight[] ar = new AccessRight[]{read};
    AccessRight[] aw = new AccessRight[]{write};
    AccessRight[] arw = new AccessRight[]{read, write};

    // add edges
    // User and user attribute assignments
    this.addEdge(chineseWall, ua1, new Assignment());
    this.addEdge(ua1, u1, new Assignment());

    // Object and object attribute assignments
    this.addEdge(chineseWall, oa9, new Assignment());
    this.addEdge(oa9, oa6, new Assignment());
    this.addEdge(oa9, oa7, new Assignment());
    this.addEdge(oa9, oa8, new Assignment());
    this.addEdge(oa6, oa1, new Assignment());
    this.addEdge(oa6, oa2, new Assignment());
    this.addEdge(oa7, oa3, new Assignment());
    this.addEdge(oa7, oa4, new Assignment());
    this.addEdge(oa8, oa5, new Assignment());
    this.addEdge(oa1, o1, new Assignment());
    this.addEdge(oa2, o2, new Assignment());
    this.addEdge(oa3, o3, new Assignment());
    this.addEdge(oa3, o4, new Assignment());
    this.addEdge(oa4, o5, new Assignment());
    this.addEdge(oa5, o6, new Assignment());

    this.addEdge(oa9, ua1, new Association(arw));
    this.addEdge(o1, u1, new Prohibition(aw));

    syncLists();
  }

  public PolicyImpl buildPrologPolicy() {
    syncLists();
    StringBuilder proString = new StringBuilder();
    for (Integer i = 0; i < nodeLists.size(); i++) {
      proString.append(buildOneTypeProlog(nodeLists.get(i)));
    }
    proString.append(AccessRight.buildAccessRights(allPermissions).get(0));
    for (Integer i = 0; i < relationLists.size(); i++) {
      proString.append(buildOneTypeProlog(relationLists.get(i)));
    }
    return new PolicyImpl(proString.toString(), prologPath);
  }

  public static String buildOneTypeProlog(List<? extends Element> oneType) {
    StringBuilder oneTypeProlog = new StringBuilder();
    for (Element one : oneType) {
      oneTypeProlog.append(one.toStringProlog() + System.lineSeparator());
    }
    return oneTypeProlog.toString();
  }

  public PolicyImpl buildPMLPolicy() {
    syncLists();
    StringBuilder pmlString = new StringBuilder();
    pmlString.append(AccessRight.buildAccessRights(allPermissions).get(1));
    Set<NodeElement> createdNodes = new HashSet<>();
    List<Relation> uniqueRelations;
    remainingAssignments.clear();
    associations.clear();
    prohibitions.clear();

    // use every policy class node to build the BFS trees
    for (NodeElement node : nodeLists.get(0)) {
      createdNodes.add(node);
      pmlString.append(breadthTraverseOnePC(this, node, createdNodes));
      createdNodes.addAll(createdNodesOneTra);
      createdNodesOneTra.clear();
    }

    pmlString.append(buildOneTypePML(remainingAssignments.stream().toList()));
    uniqueRelations = associations.stream().distinct().collect(Collectors.toList());
    uniqueRelations.addAll(prohibitions.stream().distinct().collect(Collectors.toList()));
    pmlString.append(buildOneTypePML(uniqueRelations));
    return new PolicyImpl(pmlString.toString(), pmlPath);
  }

  public PolicyImpl buildPMLPolicy22() {
    syncLists();
    StringBuilder pmlString = new StringBuilder();
    pmlString.append(AccessRight.buildAccessRights22(allPermissions).get(1));
    Set<NodeElement> createdNodes = new HashSet<>();
    List<Relation> uniqueRelations;
    remainingAssignments.clear();
    associations.clear();
    prohibitions.clear();

    // use every policy class node to build the BFS trees
    for (NodeElement node : nodeLists.get(0)) {
      createdNodes.add(node);
      pmlString.append(breadthTraverseOnePC22(this, node, createdNodes));
      createdNodes.addAll(createdNodesOneTra);
      createdNodesOneTra.clear();
    }

    pmlString.append(buildOneTypePML22(remainingAssignments.stream().toList()));
    uniqueRelations = associations.stream().distinct().collect(Collectors.toList());
    uniqueRelations.addAll(prohibitions.stream().distinct().collect(Collectors.toList()));
    pmlString.append(buildOneTypePML22(uniqueRelations));
    return new PolicyImpl(pmlString.toString(), pmlPath);
  }

  public static String buildOneTypePML(List<Relation> oneRelation) {
    StringBuilder oneRelationPML = new StringBuilder();
    for (Element one : oneRelation) {
      oneRelationPML.append(one.toStringPML() + System.lineSeparator());
    }
    return oneRelationPML.toString();
  }

  public static String buildOneTypePML22(List<Relation> oneRelation) {
    StringBuilder oneRelationPML = new StringBuilder();
    for (Element one : oneRelation) {
      oneRelationPML.append(one.toStringPML22() + System.lineSeparator());
    }
    return oneRelationPML.toString();
  }

  private String breadthTraverseOnePC(PolicyGraph initialGraph, NodeElement startP, Set<NodeElement> createdNodes) {
    StringBuilder pmlString = new StringBuilder();
    List<NodeElement> nodesInBFS = buildBFSLists(initialGraph, startP);
    createdNodesOneTra.add(startP);
    pmlString.append(startP.toStringPML() + System.lineSeparator());

    // build three relations list in one policy class
    for (NodeElement node : nodesInBFS) {
      for (Relation relation : initialGraph.outgoingEdgesOf(node)) {
        if (relation instanceof Assignment) {
          remainingAssignments.add(relation);
          NodeElement target = ((Assignment) relation).getTarget();
          if (!createdNodes.contains(target) && !createdNodesOneTra.contains(target)) {
            pmlString.append(target.toStringPML() +
                " in [\"" + node.toString() + "\"]" + System.lineSeparator());
            createdNodesOneTra.add(target);
            remainingAssignments.remove(relation);
          }
        }
        else if (relation instanceof Association) {
          associations.add(relation);
        }
        else {
          prohibitions.add(relation);
        }
      }
    }
    return pmlString.toString();
  }

  private String breadthTraverseOnePC22(PolicyGraph initialGraph, NodeElement startP, Set<NodeElement> createdNodes) {
    StringBuilder pmlString = new StringBuilder();
    List<NodeElement> nodesInBFS = buildBFSLists(initialGraph, startP);
    createdNodesOneTra.add(startP);
    pmlString.append(startP.toStringPML22() + System.lineSeparator());

    // build three relations list in one policy class
    for (NodeElement node : nodesInBFS) {
      for (Relation relation : initialGraph.outgoingEdgesOf(node)) {
        if (relation instanceof Assignment) {
          remainingAssignments.add(relation);
          NodeElement target = ((Assignment) relation).getTarget();
          if (!createdNodes.contains(target) && !createdNodesOneTra.contains(target)) {
            pmlString.append(target.toStringPML() +
                " in '" + node.toString() + "';" + System.lineSeparator());
            createdNodes.add(target);
            createdNodesOneTra.add(target);
            remainingAssignments.remove(relation);
          }
        }
        else if (relation instanceof Association) {
          associations.add(relation);
        }
        else {
          prohibitions.add(relation);
        }
      }
    }
    return pmlString.toString();
  }

  private static List<NodeElement> buildBFSLists(PolicyGraph assignGraph, NodeElement start) {
    List<NodeElement> nodesBFSOrder = new ArrayList<>();
    Queue<NodeElement> queue = new LinkedList<>();
    Set<NodeElement> visited = new HashSet<>();

    queue.add(start);
    visited.add(start);

    while (!queue.isEmpty()) {
      NodeElement current = queue.remove();
      nodesBFSOrder.add(current);
      for (Relation relation : assignGraph.outgoingEdgesOf(current)) {
        if (relation instanceof Assignment) {
          NodeElement neighbor = assignGraph.getEdgeTarget(relation);
          if (!visited.contains(neighbor)) {
            queue.add(neighbor);
            visited.add(neighbor);
          }
        }
      }
    }
    return nodesBFSOrder;
  }

  private static List<NodeElement> buildDFSLists(PolicyGraph assignGraph, NodeElement start) {
    DepthFirstIterator iterator = new DepthFirstIterator(assignGraph, start);
    List<NodeElement> nodesDFSOrder = new ArrayList<>();
    while (iterator.hasNext()) {
      NodeElement node = (NodeElement) iterator.next();
      nodesDFSOrder.add(node);
    }
    return nodesDFSOrder;
  }

  public List<List<NodeElement>> getNodeLists() {
    return nodeLists;
  }

  public void computeNodeLists() {
    Map<Integer, Set<NodeElement>> nodesMap = new HashMap<>();
    List<NodeElement> allPCs = new ArrayList<>();
    BreadthFirstIterator iterator = new BreadthFirstIterator<>(this);
    while (iterator.hasNext()) {
      NodeElement node = (NodeElement) iterator.next();
      if (node instanceof PolicyClass) {
        allPCs.add(node);
      }
    }
    for (NodeElement node : allPCs) {
      for (NodeElement bfsNode: buildBFSLists(this, node)) {
        if (bfsNode instanceof PolicyClass) {
          nodesMap.computeIfAbsent(0, k -> new HashSet<>()).add(bfsNode);
        }
        else if (bfsNode instanceof User) {
          nodesMap.computeIfAbsent(1, k -> new HashSet<>()).add(bfsNode);
        }
        else if (bfsNode instanceof UserAttribute) {
          nodesMap.computeIfAbsent(2, k -> new HashSet<>()).add(bfsNode);
        }
        else if (bfsNode instanceof  Ob) {
          nodesMap.computeIfAbsent(3, k -> new HashSet<>()).add(bfsNode);
        }
        else {
          nodesMap.computeIfAbsent(4, k -> new HashSet<>()).add(bfsNode);
        }
      }
    }
    nodeLists = new ArrayList<>(Collections.nCopies(nodesMap.size(), null));
    for (Map.Entry<Integer, Set<NodeElement>> entry : nodesMap.entrySet()) {
      nodeLists.set(entry.getKey(), new ArrayList<>(entry.getValue()));
    }
  }

  public List<List<Relation>> getRelationLists() {
    return relationLists;
  }

  public void computeRelationLists() {
    List<List<NodeElement>> nodeLists = getNodeLists();
    Map<Integer, Set<Relation>> relationsMap = new HashMap<>();
    for (NodeElement pc : nodeLists.get(0)) {
      for (NodeElement nodeElement : buildBFSLists(this, pc)) {
        for (Relation relation : outgoingEdgesOf(nodeElement)) {
          if (relation instanceof Assignment) {
            relationsMap.computeIfAbsent(0, k -> new HashSet<>()).add(relation);
          }
          else if (relation instanceof Association) {
            relationsMap.computeIfAbsent(1, k -> new HashSet<>()).add(relation);
          }
          else {
            relationsMap.computeIfAbsent(2, k -> new HashSet<>()).add(relation);
          }
        }
      }
    }
    relationLists = new ArrayList<>(Collections.nCopies(relationsMap.size(), null));
    for (Map.Entry<Integer, Set<Relation>> entry : relationsMap.entrySet()) {
      relationLists.set(entry.getKey(), new ArrayList<>(entry.getValue()));
    }
  }

  private void syncLists() {
    computeNodeLists();
    computeRelationLists();
  }

  public static boolean isAncestor(PolicyGraph graph, NodeElement descendant, NodeElement ancestor) {
    AllDirectedPaths<NodeElement, Relation> allPaths = new AllDirectedPaths<>(graph);
    List<GraphPath<NodeElement, Relation>> paths = allPaths.getAllPaths(ancestor, descendant, true, null);

    return !paths.isEmpty();
  }

  public int checkDegree(String node) {
    for (List<NodeElement> nodeElements: getNodeLists()) {
      for (NodeElement nodeElement : nodeElements) {
        if (node.equals(nodeElement.toString())) {
          return degreeOf(nodeElement);
        }
      }
    }
    return 0;
  }

  public String countDegreeNodes(int targetDegree) {
    Map<NodeElement, Integer> degreeMap = new HashMap<>();
    for (NodeElement node : this.vertexSet()) {
      int nodeDegree = this.degreeOf(node);
      degreeMap.put(node, nodeDegree);
    }

    int count = 0;
    for (Map.Entry<NodeElement, Integer> entry : degreeMap.entrySet()) {
      if (entry.getValue() == targetDegree) {
        count++;
      }
    }

    return String.valueOf(count);
  }

  public String countTypeNodes(String target) {
    if (target.startsWith("u", 0)) {
      return String.valueOf(nodeLists.get(1).size() + nodeLists.get(2).size());
    }
    else if (target.startsWith("o", 0)) {
      return String.valueOf(nodeLists.get(3).size() + nodeLists.get(4).size());
    }
    else {
      return String.valueOf(nodeLists.get(0).size());
    }
  }

  @Override
  public boolean addVertex(NodeElement node) {
    boolean added = super.addVertex(node);
    return added;
  }

  public boolean addVertex(NodeElement node, PolicyGraph graph) {
    boolean added = graph.addVertex(node);
    syncLists();
    return added;
  }

  @Override
  public boolean removeVertex(NodeElement node) {
    boolean removed = super.removeVertex(node);
    syncLists();
    return removed;
  }

  @Override
  public boolean addEdge(NodeElement sourceVertex, NodeElement targetVertex, Relation relation) {
    boolean added = super.addEdge(sourceVertex, targetVertex, relation);
    return added;
  }

  public boolean addEdge(NodeElement sourceVertex, NodeElement targetVertex, Relation relation, PolicyGraph graph) {
    boolean added = graph.addEdge(sourceVertex, targetVertex, relation);
    syncLists();
    return added;
  }
}
