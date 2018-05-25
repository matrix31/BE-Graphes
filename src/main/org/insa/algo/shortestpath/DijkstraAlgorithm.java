package org.insa.algo.shortestpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import org.insa.algo.AbstractInputData.Mode;
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
        //Tableau de label de la taille du nombre de noeuds
        Label[] labels = new Label[nbNodes];
        //Tous les labels <= null
        Arrays.fill(labels, null);
        
        //Init label premier sommet
        labels[data.getOrigin().getId()] = new Label(data.getOrigin());
        labels[data.getOrigin().getId()].setCost(0);
        
        //observateur origine
        this.notifyOriginProcessed(data.getOrigin());
        
        //Création arbre binaire
        BinaryHeap<Label> heap = new BinaryHeap<Label>();
        
        //Premier label dans l'arbre
        heap.insert(labels[data.getOrigin().getId()]);
        
        //compteur des mark
        int mark = 0;
        Label labelx;
        Node noeudx;
        boolean found = false;
        //Algo de dijkstra
        //Calcul itérations pour les tests
        int iter = 0;
        while(mark < heap.size() && !found ) {
        	iter++;
        	
        	//On recup le premier element de l'arbre et on met sa mark à 1
        	labelx = heap.deleteMin();
        	labelx.setMark(1);
        	if(labelx.getNoeud() == data.getDestination()) {
        		found = true;
             	// The destination has been found, notify the observers.
         		notifyDestinationReached(data.getDestination());
        	}
	        //observateur neud marker
	        this.notifyNodeMarked(labelx.getNoeud());
	        
        	//On récupère ses noeuds adjacents et on boucle sur les arcs
        	noeudx = labelx.getNoeud();
        	int labelId;
        	for(Arc arc : noeudx) {
        		if(data.isAllowed(arc)) {
        			labelId = arc.getDestination().getId();
	        		//Si le label de l'arc n'existe pas on l'initialise
	        		if(labels[labelId] == null) {
	        			labels[labelId] = new Label(arc.getDestination());
	        	        //observateur 1er passage
	        	        this.notifyNodeReached(arc.getDestination());
	        	        labels[labelId].setCost(labelx.getCost() + data.getCost(arc));
        				heap.insert(labels[labelId]);
        				//On modifie son "père"
        				labels[labelId].setFather(labelx.getNoeud());
	        		}
	        		
	        		//Si coût actuel du noeud > au cout nouveau on l'actualise et si l'arc est autorisé au mode de transport
	        		else if(labels[labelId].getCost() > labelx.getCost() + data.getCost(arc)) {
	       				try {
	       					//On supprime le label pour le remettre avec sa mark modifiée
	       					heap.remove(labels[labelId]);
	       				} catch(ElementNotFoundException e) {
	       					System.out.println("Erreur de suppression du noeud " +  labelId);
	       				} finally {
	       					labels[labelId].setCost(labelx.getCost() + data.getCost(arc));
	       					heap.insert(labels[labelId]);
	       					//On modifie son "père"
	       					labels[labelId].setFather(labelx.getNoeud());
	       				}
	        		}
        		}
        		//System.out.println(labels[labelId].getCost());
        	}
        }
        ShortestPathSolution solution = null;
        
        // Destination has no predecessor, the solution is infeasible...
     	if (labels[data.getDestination().getId()] == null || data.getOrigin().getId() == data.getDestination().getId()) {
     		solution = new ShortestPathSolution(data, Status.INFEASIBLE);
     	} else {

     	// Create the path from the array of nodes...
     		ArrayList<Node> nodes = new ArrayList<>();
     		//On crée un tableau de nodes en récupant tous les father
     		Node node = labels[data.getDestination().getId()].getNoeud();
     		while(node != null) {
     			nodes.add(node);
     			node = labels[node.getId()].getFather();
     		}
     		// Reverse the path...
     		Collections.reverse(nodes);
     		//System.out.println("Nombre d'arcs : " + nodes.size() + " & Nombre d'itérations : " + iter);	
     		// Create the final solution.
     		if(data.getMode() == Mode.LENGTH) {
         		solution = new ShortestPathSolution(data, Status.OPTIMAL, Path.createShortestPathFromNodes(graph, nodes));
     		} else if(data.getMode() == Mode.TIME) {
         		solution = new ShortestPathSolution(data, Status.OPTIMAL, Path.createFastestPathFromNodes(graph, nodes));
     		} else {
         		solution = new ShortestPathSolution(data, Status.OPTIMAL, Path.createShortestPathFromNodes(graph, nodes));
     		}
     	}
        return solution;
    }
}
