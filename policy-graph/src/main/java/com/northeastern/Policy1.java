package com.northeastern;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Set;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;

public class Policy1 {
  private Policy1() {
  }

  public static void main(String[] args) throws IOException {
    Graph<NodeElement, DefaultEdge> assignGraph = createAssignGraph();

    // note directed edges are printed as: (<v1>,<v2>)
    System.out.println("-- toString output");
    System.out.println(assignGraph.toString());
    String output = assignGraph.toString();

    System.out.println();

    NodeElement startP = assignGraph.
        vertexSet().stream().filter(pc -> pc.toString().startsWith("de")).findAny()
        .get();

    // perform a graph traversal starting from that vertex
    System.out.println("-- depth traverse AssignGraph output");
    try {
      depthTraverseAssignGraph(assignGraph, startP);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    System.out.println();

    System.out.println("-- breadth traverse AssignGraph output");
    breadthTraverseAssignGraph(assignGraph, startP);
    System.out.println();

//    System.out.println(" -- a list of vertices that are the direct successors of pc");
//    List<NodeElement> directNodes = Graphs.successorListOf(assignGraph, startP);
//    System.out.println(directNodes);
  }

  /**
   * Creates a simple directed graph based on policy node elements.
   *
   * @return a graph based on node objects.
   */
  public static Graph<NodeElement, DefaultEdge> createAssignGraph()
  {

    Graph<NodeElement, DefaultEdge> g = new DirectedMultigraph<>(DefaultEdge.class);

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

    AccessRight[] ar0 = new AccessRight[]{AccessRight.PERMISSION0};
    AccessRight[] ar12 = new AccessRight[]{AccessRight.PERMISSION1, AccessRight.PERMISSION2};
    AccessRight[] ar4 = new AccessRight[]{AccessRight.PERMISSION4};
    AccessRight[] ar1 = new AccessRight[]{AccessRight.PERMISSION1};
    AccessRight[] ar2 = new AccessRight[]{AccessRight.PERMISSION2};

    // add the vertices
    g.addVertex(u1);
    g.addVertex(u2);
    g.addVertex(o1);
    UserAttribute[] listOfUA = {ua1, ua2, ua3, ua4, ua5, ua6, ua7};
    for (int i = 0; i < listOfUA.length; i++) {
      g.addVertex(listOfUA[i]);
    }
    ObjectAttribute[] listOfOA = {oa1, oa2, oa3, oa4, oa5, oa6, oa7};
    for (int i = 0; i < listOfUA.length; i++) {
      g.addVertex(listOfOA[i]);
    }
    g.addVertex(department);

    //the direction is opposite
    /**
     * how to use getSpanningTreeEdge
     * https://jgrapht.org/javadoc/org.jgrapht.core/org/jgrapht/traverse/BreadthFirstIterator.html
     */
    // add edges to create linking structure
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


    g.addEdge(ua5, oa5, new Association(ar0));
    g.addEdge(ua4, oa5, new Association(ar0));
    g.addEdge(ua4, oa4, new Association(ar4));
    g.addEdge(ua4, oa2, new Association(ar4));
    g.addEdge(ua2, oa2, new Association(ar0));
    g.addEdge(ua1, oa4, new Association(ar12));
    g.addEdge(u1, o1, new Association(ar1));

    //TODO the target of prohibition should be a list of attributes
    g.addEdge(u2,oa7, new Prohibition(ar1));
    g.addEdge(u1, o1, new Prohibition(ar2));

    return g;
  }

  /**
   * Traverse a graph in depth-first order and print the vertices.
   *
   * @param assignGraph a graph based on policy node elements
   *
   * @param start the vertex where the traversal should start
   */
  private static void depthTraverseAssignGraph(Graph<NodeElement, DefaultEdge> assignGraph, NodeElement start)
      throws IOException {
    // create a writer
    BufferedWriter writerProlog = null;
    BufferedWriter writerPML = null;
    try {
      writerProlog = Files.newBufferedWriter(Paths.get("policy-graph/src/main/resources/translatePolicy.pl"));
      writerPML = Files.newBufferedWriter(Paths.get("policy-graph/src/main/resources/translatePolicy.pal"));
    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.exit(1);
    }
    Iterator<NodeElement> iterator = new DepthFirstIterator<>(assignGraph, start);
    while (iterator.hasNext()) {
      NodeElement node = iterator.next();
      try {
        writerProlog.write(node.toStringProlog() + System.lineSeparator());
        writerPML.write(node.toStringPML() + System.lineSeparator());
//        Iterator<DefaultEdge> edgeIterator = (Iterator<DefaultEdge>) assignGraph.outgoingEdgesOf(node);
        Set<DefaultEdge> edgeSet = assignGraph.outgoingEdgesOf(node);
        for (DefaultEdge edge : edgeSet) {
          writerProlog.write(edge.toString() + System.lineSeparator());
          writerPML.write(edge.toString() + System.lineSeparator());
        }
      } catch (IOException e) {
        System.out.println(e.getMessage());
        System.exit(1);
      }
//      System.out.println(node.toString() + assignGraph.outgoingEdgesOf(node));
      writerProlog.flush();
      writerPML.flush();
    }
  }

  /**
   * Traverse a graph in breadth-first order and print the vertices.
   *
   * @param assignGraph a graph based on policy node elements
   *
   * @param start the vertex where the traversal should start
   */
  private static void breadthTraverseAssignGraph(Graph<NodeElement, DefaultEdge> assignGraph, NodeElement start)
  {
    Iterator<NodeElement> iterator = new BreadthFirstIterator<>(assignGraph, start);
    while (iterator.hasNext()) {
      NodeElement node = iterator.next();
      System.out.println(node.toString() + assignGraph.outgoingEdgesOf(node));
    }
  }
}
