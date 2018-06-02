package org.insa.algo.shortestpath;


import static org.junit.Assert.*;


import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.insa.algo.AbstractSolution.Status;
import org.insa.algo.ArcInspector;
import org.insa.algo.ArcInspectorFactory;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Path;
import org.insa.graph.RoadInformation;
import org.insa.graph.RoadInformation.RoadType;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.BinaryPathReader;
import org.insa.graph.io.GraphReader;
import org.insa.graph.io.PathReader;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.Random;
import static java.lang.Math.abs;


public class DijkstraAlgorithmTest {
	public enum Status {
        UNKNOWN, INFEASIBLE, FEASIBLE, OPTIMAL,
    };
	// Small graph use for tests
    private static Graph graph;

    // List of nodes
    private static Node[] nodes;

    // List of arcs in the graph, a2b is the arc from node A (0) to B (1).
    @SuppressWarnings("unused")
    private static Arc x12x2, x12x3, x3tx1, x3tx2, x3tx6, x2tx4, x2tx5, x2tx6, x5tx4, x5tx3, x5tx6, x6tx5;


    @BeforeClass
    public static void initAll() throws IOException {

        // 10 and 20 meters per seconds
        RoadInformation speed10 = new RoadInformation(RoadType.MOTORWAY, null, true, 50, "");

        // Create nodes
        nodes = new Node[6];
        for (int i = 0; i < nodes.length; ++i) {
            nodes[i] = new Node(i, null);
        }

        // Add arcs...
        x12x2 = Node.linkNodes(nodes[0], nodes[1], 7, speed10, null);
        x12x3 = Node.linkNodes(nodes[0], nodes[2], 8, speed10, null);
        x3tx1 = Node.linkNodes(nodes[2], nodes[0], 7, speed10, null);
        x3tx2 = Node.linkNodes(nodes[2], nodes[1], 2, speed10, null);
        x3tx6 = Node.linkNodes(nodes[2], nodes[5], 2, speed10, null);
        x2tx4 = Node.linkNodes(nodes[1], nodes[3], 7, speed10, null);
        x2tx5 = Node.linkNodes(nodes[1], nodes[4], 1, speed10, null);
        x2tx6 = Node.linkNodes(nodes[1], nodes[5], 5, speed10, null);
        x5tx4 = Node.linkNodes(nodes[4], nodes[3], 2, speed10, null);
        x5tx3 = Node.linkNodes(nodes[4], nodes[2], 2, speed10, null);
        x5tx6 = Node.linkNodes(nodes[4], nodes[5], 3, speed10, null);
        x6tx5 = Node.linkNodes(nodes[5], nodes[4], 3, speed10, null);
        
        graph = new Graph("ID", "", Arrays.asList(nodes), null);

    }
   
    @Test
    public void testDijkstra() {
    	List<ArcInspector> filters = ArcInspectorFactory.getAllFilters();
    	//Solution Dijkstra
    	float[][] solD = new float [nodes.length][nodes.length];
    	//Solution Bellman Ford
    	float[][] solBF = new float [nodes.length][nodes.length];
    	//Creation des solution
    	for (int i = 0; i < nodes.length; ++i) {
    		for (int j = 0; j < nodes.length; ++j) {
    			//Solution Dijkstra
               	ShortestPathData data = new ShortestPathData(graph, nodes[i], nodes[j], filters.get(0));
               	DijkstraAlgorithm algo = new DijkstraAlgorithm(data);
               	ShortestPathSolution solution = algo.doRun();
               	if(solution.isFeasible() && solution.getPath().getLength() != 0) {
               		solD[i][j] = solution.getPath().getLength();
               	} else {
               		solD[i][j] = -1;
               	}
               	//Solution Bellman Ford
               	BellmanFordAlgorithm algoBF = new BellmanFordAlgorithm(data);
               	ShortestPathSolution solutionBF = algoBF.doRun();
               	if(solutionBF.isFeasible() && solutionBF.getPath().getLength() != -1) {
               		solBF[i][j] = solutionBF.getPath().getLength();
               	} else {
               		solBF[i][j] = -1;
               	}
            }
        }
    	for (int i = 0; i < nodes.length; ++i) {
    		for (int j = 0; j < nodes.length; ++j) {
    			//Comparaison entre les 2 solutions
    			assertEquals(solBF[i][j], solD[i][j], 1e-4);
    		}
    	}
    	//Visualisation dans la console//
    	for (int i = 0; i < nodes.length; ++i) {
    		for (int j = 0; j < nodes.length; ++j) {
    			if(solD[i][j] != -1) {
    				System.out.print((int) solD[i][j] + " ");
    			}else {
    				System.out.print("o ");
    			}
    		}
    		System.out.print("\n");
    	}
    	System.out.print("\n");
    	for (int i = 0; i < nodes.length; ++i) {
    		for (int j = 0; j < nodes.length; ++j) {
    			if(solBF[i][j] != -1) {
    				System.out.print((int) solBF[i][j] + " ");
    			}else {
    				System.out.print("o ");
    			}
    		}
    		System.out.print("\n");
    	}
    	System.out.print("\n");
    }
    
       	
    @Test
    // Test simple en distance sur 100 tests
    public void testDijkstraLength() throws Exception { 
    	int compteur = 0 ;
    	int nbNode = graph.getSize();
    	int nbrTest_faisable = 100 ;
  
    	String mapName = "/Users/franck/Desktop/toulouse.mapgr";
    	
    	for (int i=0 ; i<100 ; i++){
    
    			int originId = (int) (Math.random()*nbNode); // generation des id aléatoires
	   		int destinationId = (int) (Math.random()*nbNode);
	        ShortestPathSolution[] Solutions = new ShortestPathSolution[2];
	        Solutions = TestDijkstraBF(mapName,originId,destinationId,6);
	        System.out.println(Solutions[1].isFeasible());
	        if ( Solutions[0].isFeasible() == false || Solutions[1].isFeasible() == false ) {
	        		nbrTest_faisable-- ;// on ne prend pas en compte le test si le chemin n'est pas faisable
	        }
	        else {
	        		if ( Solutions[0].getPath().getArcs().equals(Solutions[1].getPath().getArcs())) {
	        		compteur++;	// test ok 
	        		}
	        }
    	}
    	if ( compteur==nbrTest_faisable) {
    		System.out.println("Les 100 tests en distance sont validés");
    		assertTrue(compteur==nbrTest_faisable);
    	}
    	else {
    		System.out.println(compteur+" tests validés sur 100");
    	}
	        
    }
    
    @Test
    // Test simple en temps sur 100 tests
    public void testDijkstraTime() throws Exception { 	
    	int compteur = 0 ;
    	int nbNode = graph.getSize();
    	int nbrTest_faisable = 100 ;
  
    	String mapName = "/Users/franck/Desktop/toulouse.mapgr";
    	
    	for (int i=0 ; i<100 ; i++){
    
    			int originId = (int) (Math.random()*nbNode); // generation des id aléatoires
	   		int destinationId = (int) (Math.random()*nbNode);
	        ShortestPathSolution[] Solutions = new ShortestPathSolution[2];
	        Solutions = TestDijkstraBF(mapName,originId,destinationId,2);
	        System.out.println(Solutions[1].isFeasible());
	        if ( Solutions[0].isFeasible() == false || Solutions[1].isFeasible() == false ) {
	        		nbrTest_faisable-- ;// on ne prend pas en compte le test si le chemin n'est pas faisable
	        }
	        else {
	        		if ( Solutions[0].getPath().getArcs().equals(Solutions[1].getPath().getArcs())) {
	        		compteur++;	// test ok 
	        		}
	        }
    	}
    	if ( compteur==nbrTest_faisable) {
    		System.out.println("Les 100 tests en temps sont validés");
    		assertTrue(compteur==nbrTest_faisable);
    	}
    	else {
    		System.out.println(compteur+" tests validés sur 100");
    	}
	        
    }
  

    @Test 
    // Chemin inexistant
    public void testDijkstraNoWay() throws Exception { 	
    	String mapName = "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/new-zealand.mapgr";
        ShortestPathSolution[] Solutions = new ShortestPathSolution[2];
        Solutions = TestDijkstraBF(mapName,15357,313202,0);
        assertEquals(Solutions[0].getStatus(), Solutions[1].getStatus());
    }
    
    @Test 
    // Chemin inexistant début = fin sur 100 chemins
    public void testDijkstraStartEEnd() throws Exception { 	
	
    	int compteur = 0 ;
    	int nbNode = graph.getSize();
    	int nbrTest_total = 100 ;
  
    	String mapName = "/Users/franck/Desktop/toulouse.mapgr";
    	
    	for (int i=0 ; i< nbrTest_total ; i++){
    
    		int originId= (int) (Math.random()*nbNode); // generation des id aléatoires
    		int destinationId = originId ;
	    ShortestPathSolution[] Solutions = new ShortestPathSolution[2];
	    Solutions = TestDijkstraBF(mapName,originId,destinationId,2);
	    System.out.println(Solutions[1].isFeasible());
	    if ( Solutions[0].getStatus().equals(Solutions[1].getStatus())); {
	        		compteur++;	// test ok 
	    }
    	}
    	if ( compteur==nbrTest_total) {
    		System.out.println("Les "+ nbrTest_total+ " tests sont validés");
    		assertTrue(compteur==nbrTest_total);
    		
    	}
    	else {
    		System.out.println(compteur+" tests validés sur " + nbrTest_total);
    	}
	        
    }
   
    
    @Test 
    // Chemin avec des routes interdites aux voitures
    public void testDijkstraNoCarAllowed() throws Exception { 	
    	String mapName = "/Users/franck/Desktop/toulouse.mapgr";
    	ShortestPathSolution[] Solutions = new ShortestPathSolution[2];
        Solutions = TestDijkstraBF(mapName,3669,17290,1);
        //System.out.println(Solutions[0].getPath());
        assertTrue(Solutions[0].getPath().getArcs().equals(Solutions[1].getPath().getArcs()));
    }
    
    @Test 
    // TEST Sans Oracle
    public void testDijkstraNoOracle() throws Exception { 	
    	String mapName = "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/midi-pyrenees.mapgr";
    	List<ArcInspector> filters = ArcInspectorFactory.getAllFilters();
        // Create a graph reader.
        GraphReader reader = new BinaryGraphReader(
        new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
        //Read the graph.
        Graph graph = reader.read();
        int nbNode = graph.size();
        int originId = (int) (Math.random()*nbNode);
        int destinationId = (int)( Math.random()*nbNode);
        Node origin = graph.get(originId);
        Node destination = graph.get(destinationId);
        ShortestPathData data = new ShortestPathData(graph, origin, destination, filters.get(1));
        //solution Dijkstra
      	DijkstraAlgorithm algoD = new DijkstraAlgorithm(data);
      	ShortestPathSolution solution = algoD.doRun();
      	int solutionSize = solution.getPath().size();
      	List<Arc> Arcs = solution.getPath().getArcs();
      	Arc arcMilieu = Arcs.get((int) solutionSize/2);
      	Node arcNode = arcMilieu.getOrigin();
      	
      	//On a récupéré le milieu du chemin refaire 2 chemin du début au milieu puis du milieu a la fin et comparer la somme des distances à l'autre
    }
 

 
     
    // Méthodes pour les test
    /* @param : String mapName
     * 			int originId
     * 			int destinationId
     * 			int filterId
     * @return : ShortestPathSolution[2] Tableau avec les 2 solutions (Dijstra et Bellman-Ford)
     */
   
    private ShortestPathSolution[] TestDijkstraBF(String mapName, int originId, int destinationId, int filterId) throws Exception {
        List<ArcInspector> filters = ArcInspectorFactory.getAllFilters();
        ShortestPathSolution[] Solutions = new ShortestPathSolution[2];
        // Create a graph reader.
        GraphReader reader = new BinaryGraphReader(
        new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
        //Read the graph.
        Graph graph = reader.read();
        Node origin = graph.get(originId);
        Node destination = graph.get(destinationId);
        ShortestPathData data = new ShortestPathData(graph, origin, destination, filters.get(filterId));
        //solution Dijkstra
      	DijkstraAlgorithm algoD = new DijkstraAlgorithm(data);
      	Solutions[0] = algoD.run();
        //Solution Bellman Ford
        BellmanFordAlgorithm algoBF = new BellmanFordAlgorithm(data);
        Solutions[1] = algoBF.run();
        return Solutions;
    }
    
    private ShortestPathSolution[] TestDijkstra_Astar(String mapName, int originId, int destinationId, int filterId) throws Exception {
        List<ArcInspector> filters = ArcInspectorFactory.getAllFilters();
        ShortestPathSolution[] Solutions = new ShortestPathSolution[2];
        // Create a graph reader.
        GraphReader reader = new BinaryGraphReader(
        new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
        //Read the graph.
        Graph graph = reader.read();
        Node origin = graph.get(originId);
        Node destination = graph.get(destinationId);
        ShortestPathData data = new ShortestPathData(graph, origin, destination, filters.get(filterId));
        //solution Dijkstra
      	DijkstraAlgorithm algoD = new DijkstraAlgorithm(data);
      	Solutions[0] = algoD.doRun();
        //Solution Astar
        AStarAlgorithm algoAstar = new AStarAlgorithm(data);
        Solutions[1] = algoAstar.doRun();
        return Solutions;
    }
}


