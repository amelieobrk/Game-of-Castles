package tests.student;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import base.*;

public class GraphConnectionTest {
	
	@Test
	public void testConnection1() {
		Graph<Integer> test1 = new Graph<>();
		Node<Integer> a = test1.addNode(1);
		Node<Integer> b = test1.addNode(2);
		Node<Integer> c = test1.addNode(3);
		Node<Integer> d = test1.addNode(4);
		
		test1.addEdge(a, b);
		test1.addEdge(b, c);
		test1.addEdge(c, d);
		test1.addEdge(a, d);
		
		assertEquals
				(test1.allNodesConnected(), true);
	}
	
	@Test
	public void testConnection2() {
		Graph<Integer> test1 = new Graph<>();
		Node<Integer> a = test1.addNode(1);
		Node<Integer> b = test1.addNode(2);
		Node<Integer> c = test1.addNode(3);
		Node<Integer> d = test1.addNode(4);
		
		test1.addEdge(a, b);
		test1.addEdge(b, c);
		
		assertEquals
				(test1.allNodesConnected(), false);
	}

}
