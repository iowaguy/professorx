package com.northeastern;

import java.util.Iterator;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;

public class PolicyExample {
  private PolicyExample()
  {
  } // ensure non-instantiability.

  /**
   * The starting point.
   *
   * @param args ignored.
   */
  public static void main(String[] args)
  {
    Graph<NodeElement, DefaultEdge> assignGraph = createAssignGraph();

    // note directed edges are printed as: (<v1>,<v2>)
    System.out.println("-- toString output");
    System.out.println(assignGraph.toString());
    System.out.println();

    NodeElement startP = assignGraph.
        vertexSet().stream().filter(pc -> pc.toString().startsWith("<pc")).findAny()
            .get();

    //TODO define the start vertex to traverse the tree
    /**
     * if we add edge from attributes to policy, the traverse should be separated from user and object
     */
//    NodeElement startO = assignGraph.
//        vertexSet().stream().filter(pc -> pc.toString().startsWith("<o1")).findAny()
//        .get();

    // perform a graph traversal starting from that vertex
    System.out.println("-- depth traverse AssignGraph output");
    depthTraverseAssignGraph(assignGraph, startP);
    System.out.println();

    System.out.println("-- breadth traverse AssignGraph output");
    breadthTraverseAssignGraph(assignGraph, startP);
    System.out.println();
  }

  /**
   * Creates a simple directed graph based on policy node elements.
   *
   * @return a graph based on node objects.
   */
  //TODO use edges to represent different relations. Need to have multiple types of edges.
  private static Graph<NodeElement, DefaultEdge> createAssignGraph()
  {

    Graph<NodeElement, DefaultEdge> g = new SimpleDirectedGraph<>(DefaultEdge.class);

    User u1 = new User("u1");
    User u2 = new User("u2");
    Ob o1 = new Ob("o1");
    Ob o2 = new Ob("o2");
    Ob o3 = new Ob("o3");
    PolicyClass pc1 = new PolicyClass("pc1");


    // add the vertices
    g.addVertex(u1);
    g.addVertex(o1);
    g.addVertex(pc1);
    g.addVertex(u2);
    g.addVertex(o2);

    //TODO the current direction is opposite
    /**
     * how to use getSpanningTreeEdge
     * https://jgrapht.org/javadoc/org.jgrapht.core/org/jgrapht/traverse/BreadthFirstIterator.html
     */
    // add edges to create linking structure
    g.addEdge(pc1, o1);
    g.addEdge(pc1, o2);
    g.addEdge(pc1, u1);
    g.addEdge(u1, u2);

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
  {
    Iterator<NodeElement> iterator = new DepthFirstIterator<>(assignGraph, start);
    while (iterator.hasNext()) {
      NodeElement node = iterator.next();
      System.out.println(node);
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
      System.out.println(node);
    }
  }
}
