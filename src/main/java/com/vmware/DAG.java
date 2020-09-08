package com.vmware;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Create a class which represents a DAG (directed acyclic graph) and has the following methods:
// https://en.wikipedia.org/wiki/Directed_acyclic_graph
// https://medium.com/kriptapp/guide-what-is-directed-acyclic-graph-364c04662609
// * acyclic = without circles

//Node createNode(String value): Creates a new node in the graph. Returns the created Node.
//void createEdge(Node src, Node dest): Creates a new edge which connects two existing nodes in the graph. The direction of the new edge is from src to dest.
//boolean isAncestor(Node src, Node dest): Return true if and only if there is a (directed) path from src to dest.
//Set<Node> findAllAncestors(Node dest): Returns a set of all ancestors of dest.
public class DAG {

	private static final Logger LOGGER = LoggerFactory.getLogger(DAG.class);

	public static void main(String[] args) {

		DAG dag = new DAG();

		// The following DAG example is taken from the charts in DAG-example.png (in the project home directory).
		Node a = dag.createNode("A");
		Node b = dag.createNode("B");
		Node c = dag.createNode("C");
		Node d = dag.createNode("D");
		Node e = dag.createNode("E");
		Node f = dag.createNode("F");
		Node g = dag.createNode("G");

		dag.createEdge(a, b);
		dag.createEdge(b, c);
		dag.createEdge(b, e);
		dag.createEdge(b, d);
		dag.createEdge(c, e);
		dag.createEdge(e, f);
		dag.createEdge(d, e);
		dag.createEdge(g, d);

		// we print the results as sorted for easy readability
		LOGGER.info("dag.findAllAncestors(e)={}", getAsSorted(dag.findAllAncestors(e)));
		LOGGER.info("dag.findAllAncestors(f)={}", getAsSorted(dag.findAllAncestors(f)));
		LOGGER.info("dag.findAllAncestors(d)={}", getAsSorted(dag.findAllAncestors(d)));
		LOGGER.info("dag.findAllAncestors(g)={}", getAsSorted(dag.findAllAncestors(g)));
		LOGGER.info("dag.findAllAncestors(a)={}", getAsSorted(dag.findAllAncestors(a)));
		LOGGER.info("dag.findAllAncestors(c)={}", getAsSorted(dag.findAllAncestors(c)));
	}

	private static Set<Node> getAsSorted(Set<Node> set) {

		// Sorting HashSet using TreeSet 
		return new TreeSet<>(set); 
	}

	private Map<Node, Set<Node>> nodeToNeighborsMap = new HashMap<>();

	public Node createNode(String value) {
		
		if (value == null) {

			throw new IllegalArgumentException("parameter value can't be null");
		}

		return new Node(value);
	}

	public void createEdge(Node src, Node dest) {

		validateNotNull("src", src);
		validateNotNull("dest", dest);

		nodeToNeighborsMap.computeIfAbsent(src, k -> new HashSet<>()).add(dest);
	}

	/**
	 * Time complexity: O(n)
	 * 
	 * @param src
	 * @param dest
	 * @return
	 */
	public boolean isAncestor(Node src, Node dest) {

		validateNotNull("src", src);
		validateNotNull("dest", dest);

		if (src == dest) {

			throw new IllegalArgumentException("parameters 'src' and 'dest' are the same (src == dest)");
		}

		Helper helper = new Helper();

		isAncestor(src, dest, helper);

		return helper.isAncestor();
	}

	public Set<Node> findAllAncestors(Node dest) {

		validateNotNull("dest", dest);

		Set<Node> allAncestorsSet = new HashSet<>();

		for (Node node : nodeToNeighborsMap.keySet()) {

			if (dest != node && isAncestor(node, dest)) {

				allAncestorsSet.add(node);
			}
		}

		return allAncestorsSet;
	}

	private void validateNotNull(String name, Node node) {

		if (node == null) {

			String message = String.format("parameter '%s' can't be null", name);

			throw new IllegalArgumentException(message);
		}
	}

	private void isAncestor(Node src, Node dest, Helper helper) {

		Set<Node> neighbors = nodeToNeighborsMap.get(src);

		if (neighbors == null) {
			return;
		}

		if (neighbors.contains(dest)) {

			helper.changeIsAncestorToTrue();

			return;
		}

		for (Node neighbor : neighbors) {

			if (helper.isAncestor()) {
				return;
			}

			isAncestor(neighbor, dest, helper);
		}
	}

	private static class Helper {

		private boolean isAncestor = false;

		private Helper() {

		}

		private void changeIsAncestorToTrue() {

			isAncestor = true;
		}

		private boolean isAncestor() {
			return isAncestor;
		}
	}
}

class Node implements Comparable<Node> {

	private String value;

	public Node(String value) {

		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Node [value=");
		builder.append(value);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(Node o) {

		return this.value.compareTo(o.value);
	}
}
