package org.insa.algo.shortestpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import org.insa.algo.AbstractSolution.Status;
import org.insa.algo.utils.BinaryHeap;
import org.insa.algo.utils.ElementNotFoundException;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Path;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        ShortestPathData data = getInputData();
        Graph graph = data.getGraph();
        
        final int nbNodes = graph.size();
        Label[] labels = new Label[nbNodes];
        Arrays.fill(labels, null);
        
        //Premier sommet
        labels[data.getOrigin().getId()] = new Label(data.getOrigin());
        labels[data.getOrigin().getId()].setCost(0);
        BinaryHeap<Label> heap = new BinaryHeap<Label>();
        heap.insert(labels[data.getOrigin().getId()]);
        
        //compteur des mark
        int mark = 0;
        Label labelx;
        Node noeudx;
        
        //Algo de dijkstra
        while(mark < heap.size()) {
        	labelx = heap.deleteMin();
        	labelx.setMark(1);
        	noeudx = labelx.getNoeud();
        	for(Arc arc : noeudx) {
        		if(labels[arc.getDestination().getId()] == null) {
        			labels[arc.getDestination().getId()] = new Label(arc.getDestination());
        		}
        		if(labels[arc.getDestination().getId()].getCost() > labelx.getCost() + arc.getLength()) {
        			labels[arc.getDestination().getId()].setCost(labelx.getCost() + arc.getLength());
        			try {
        				heap.remove(labels[arc.getDestination().getId()]);
        			} catch(ElementNotFoundException e) {
        				
        			} finally {
        				heap.insert(labels[arc.getDestination().getId()]);
        				labels[arc.getDestination().getId()].setFather(labelx.getNoeud());
        			}
        		}
        	}
        }
        ShortestPathSolution solution = null;
        // Destination has no predecessor, the solution is infeasible...
     	if (labels[data.getDestination().getId()] == null) {
     		solution = new ShortestPathSolution(data, Status.INFEASIBLE);
     	} else {

     	// The destination has been found, notify the observers.
     		notifyDestinationReached(data.getDestination());
     	// Create the path from the array of predecessors...
     		ArrayList<Node> nodes = new ArrayList<>();
     		Node node = labels[data.getDestination().getId()].getNoeud();
     		while(node != null) {
     			nodes.add(node);
     			node = labels[node.getId()].getFather();
     		}
     		// Reverse the path...
     		Collections.reverse(nodes);
     		// Create the final solution.
     		solution = new ShortestPathSolution(data, Status.OPTIMAL, Path.createShortestPathFromNodes(graph, nodes));
     	}
     		
        return solution;
    }
}
