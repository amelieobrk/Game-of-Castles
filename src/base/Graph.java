package base;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import game.map.Castle;

/**
 * Diese Klasse representiert einen generischen Graphen mit einer Liste aus Knoten und Kanten.
 *
 * @param <T> Die zugrundeliegende Datenstruktur, beispielsweise {@link game.map.Castle}
 */
public class Graph<T> {

    private List<Edge<T>> edges;
    private List<Node<T>> nodes;

    /**
     * Konstruktor für einen neuen, leeren Graphen
     */
    public Graph() {
        this.nodes = new ArrayList<>();
        this.edges = new LinkedList<>();
    }

    /**
     * Einen neuen Knoten zum Graphen hinzufügen
     * @param value Der Wert des Knotens
     * @return Der erstellte Knoten
     */
    public Node<T> addNode(T value) {
        Node<T> node = new Node<>(value);
        this.nodes.add(node);
        return node;
    }

    /**
     * Eine neue Kante zwischen zwei Knoten hinzufügen. Sollte die Kante schon existieren, wird die vorhandene Kante zurückgegeben.
     * @param nodeA Der erste Knoten
     * @param nodeB Der zweite Knoten
     * @return Die erstellte oder bereits vorhandene Kante zwischen beiden gegebenen Knoten
     */
    public Edge<T> addEdge(Node<T> nodeA, Node<T> nodeB) {
        Edge<T> edge = getEdge(nodeA, nodeB);
        if(edge != null) {
            return edge;
        }

        edge = new Edge<>(nodeA, nodeB);
        this.edges.add(edge);
        return edge;
    }

    /**
     * Gibt die Liste aller Knoten zurück
     * @return die Liste aller Knoten
     */
    public List<Node<T>> getNodes() {
        return this.nodes;
    }

    /**
     * Gibt die Liste aller Kanten zurück
     * @return die Liste aller Kanten
     */
    public List<Edge<T>> getEdges() {
        return this.edges;
    }

    /**
     * Diese Methode gibt alle Werte der Knoten in einer Liste mittels Streams zurück.
     * @see java.util.stream.Stream#map(Function)
     * @see java.util.stream.Stream#collect(Collector)
     * @return Eine Liste aller Knotenwerte
     */
    public List<T> getAllValues() {
        // TODO: Graph<T>#getAllValues()
        return nodes.stream().map(n -> n.getValue()).collect(Collectors.toList());
    }

    /**
     * Diese Methode gibt alle Kanten eines Knotens als Liste mittels Streams zurück.
     * @param node Der Knoten für die dazugehörigen Kanten
     * @see java.util.stream.Stream#filter(Predicate)
     * @see java.util.stream.Stream#collect(Collector)
     * @return Die Liste aller zum Knoten zugehörigen Kanten
     */
    public List<Edge<T>> getEdges(Node<T> node) {
        // TODO: Graph<T>#getEdges(Node<T>)
        return edges.stream().filter(n -> n.contains(node)).collect(Collectors.toList());
    }

    /**
     * Diese Methode sucht eine Kante zwischen beiden angegebenen Knoten und gibt diese zurück
     * oder null, falls diese Kante nicht existiert
     * @param nodeA Der erste Knoten
     * @param nodeB Der zweite Knoten
     * @return Die Kante zwischen beiden Knoten oder null
     */
   public Edge<T> getEdge(Node<T> nodeA, Node<T> nodeB) {
        // TODO: Graph<T>#getEdge(Node<T>, Node<T>)
    	for (Edge<T> e : this.getEdges()) {
    		if (e.contains(nodeA) && e.contains(nodeB)) {
    			return e;
    		}
    	}
        return null;
    }


    /**
     * Gibt den ersten Knoten mit dem angegebenen Wert zurück oder null, falls dieser nicht gefunden wurde
     * @param value Der zu suchende Wert
     * @return Ein Knoten mit dem angegebenen Wert oder null
     */
    public Node<T> getNode(T value) {
        // TODO: Graph<T>#getNode(T)
        return nodes.stream().filter(n -> n.getValue() == value).findFirst().orElse(null);
    }
    
    /**
     * Überprüft, ob alle Knoten in dem Graphen erreichbar sind.
     * @return true, wenn alle Knoten erreichbar sind
     */
    public boolean allNodesConnected() {
    	// TODO: Graph<T>#allNodesConnected()
    	    Node<T> n=nodes.get(0);
    		boolean boucle=false;
    		int prev=0;
    		List<Node<T>> newRes = new ArrayList<Node<T>>();
    		List<Edge<T>> a = this.getEdges(n);
    		while (!boucle) {
    			prev =newRes.size();
    		for (Edge<T> e : a) {
    			if (!newRes.contains(e.getNodeA()))
    				newRes.add(e.getNodeA());
    			if(!newRes.contains(e.getNodeB()))
    				newRes.add(e.getNodeB());
    				}
    		for (int j=0; j<newRes.size();j++) {
    			List<Edge<T>> b = this.getEdges(newRes.get(j));
    			for (Edge<T> e1 : b) {
    				if (!a.contains(e1)) {
    					a.add(e1);
    				}
    			}
    		}
    		if ( prev==newRes.size() ) {
    			boucle=true;
    		}
    		}
    		if (boucle && newRes.size()<nodes.size())
    			return false;
    		else
                return true;
    
}

}
