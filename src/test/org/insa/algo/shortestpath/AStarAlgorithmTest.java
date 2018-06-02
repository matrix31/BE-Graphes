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


public class AStarAlgorithmTest {
	public enum Status {
        UNKNOWN, INFEASIBLE, FEASIBLE, OPTIMAL,
    };
	

    // Small graph use for tests
    private static int nbtests = 2;
    
       	
    @Test
    // Test simple en distance sur 100 tests
    public void testAStarLength() throws Exception { 
    	int compteur = 0 ;
    	int nbrTest_faisable = nbtests ;
  
    	String mapName = "./src/maps/toulouse.mapgr";
    	for (int i=0 ; i<nbtests ; i++){
	        ShortestPathSolution[] Solutions = new ShortestPathSolution[2];
	        Solutions = TestDijkstra_Astar(mapName,-1,-1,6);
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
    		System.out.println("Les  "+ nbtests+ "  tests en distance sont validés");
    		assertTrue(compteur==nbrTest_faisable);
    	}
    	else {
    		System.out.println(compteur+" tests validés sur  "+ nbtests+ " ");
    	}
	        
    }
    
    @Test
    // Test simple en temps sur 100 tests
    public void testAStarTime() throws Exception { 	
    	int compteur = 0 ;
    	int nbrTest_faisable = nbtests ;
  
    	String mapName = "./src/maps/toulouse.mapgr";
    	
    	for (int i=0 ; i<nbtests ; i++){
    
	        ShortestPathSolution[] Solutions = new ShortestPathSolution[2];
	        Solutions = TestDijkstra_Astar(mapName,-1,-1,2);
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
    		System.out.println("Les "+ nbtests+ " tests en temps sont validés");
    		assertTrue(compteur==nbrTest_faisable);
    	}
    	else {
    		System.out.println(compteur+" tests validés sur  "+ nbtests+ " ");
    	}
	        
    }
  

    @Test 
    // Chemin inexistant
    public void testAStarNoWay() throws Exception { 	
    	String mapName = "./src/maps/new-zealand.mapgr";
        ShortestPathSolution[] Solutions = new ShortestPathSolution[2];
        Solutions = TestDijkstra_Astar(mapName,15357,313202,0);
        assertEquals(Solutions[0].getStatus(), Solutions[1].getStatus());
    }
    
    @Test 
    // Chemin inexistant début = fin sur 100 chemins
    public void testAStarStartEEnd() throws Exception { 	
	
    	int compteur = 0 ;
    	int nbrTest_total = nbtests ;
  
    	String mapName = "./src/maps/toulouse.mapgr";
    	
    	for (int i=0 ; i< nbrTest_total ; i++){
    
	    ShortestPathSolution[] Solutions = new ShortestPathSolution[2];
	    Solutions = TestDijkstra_Astar(mapName,-1,-1,2);
	    System.out.println(Solutions[1].isFeasible());
	    if ( Solutions[0].getStatus().equals(Solutions[1].getStatus())); {
	        		compteur++;	// test ok 
	    }
    	}
    	if ( compteur==nbrTest_total) {
    		System.out.println("Les "+ nbrTest_total+ " tests sont validés ");
    		assertTrue(compteur == nbrTest_total);
    		
    	}
    	else {
    		System.out.println(compteur+" tests validés sur " + nbrTest_total);
    	}
	        
    }
   
    
    @Test 
    // Chemin avec des routes interdites aux voitures
    public void testAStarNoCarAllowed() throws Exception { 	
    	String mapName = "./src/maps/toulouse.mapgr";
    	ShortestPathSolution[] Solutions = new ShortestPathSolution[2];
        Solutions = TestDijkstra_Astar(mapName,3669,17290,1);
        //System.out.println(Solutions[0].getPath());
        assertTrue(Solutions[0].getPath().getArcs().equals(Solutions[1].getPath().getArcs()));
    }
    
    @Test 
    // TEST Sans Oracle
    public void testAStarNoOracle() throws Exception { 	
    	String mapName = "./src/maps/toulouse.mapgr";
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
      	if(solution.isFeasible()){
	      	int solutionSize = solution.getPath().size();
	      	List<Arc> Arcs = solution.getPath().getArcs();
	      	Arc arcMilieu = Arcs.get((int) solutionSize/2);
	      	Node arcMilieuNode = arcMilieu.getOrigin();
	      	
	      	//Première moitié
	      	ShortestPathData data2 = new ShortestPathData(graph, origin, arcMilieuNode, filters.get(1));
	      	DijkstraAlgorithm algo2 = new DijkstraAlgorithm(data2);
	      	ShortestPathSolution solution2 = algo2.doRun();
	      	int solutionSize2 = solution2.getPath().size() - 1;
	      //Deuxième moitié
	      	ShortestPathData data3 = new ShortestPathData(graph, arcMilieuNode, destination, filters.get(1));
	      	DijkstraAlgorithm algo3 = new DijkstraAlgorithm(data3);
	      	ShortestPathSolution solution3 = algo3.doRun();
	      	solutionSize2 += solution3.getPath().size();
	      	System.out.println(solutionSize+" / " + solutionSize2);
	      	assertEquals(solutionSize, solutionSize2);
      	} else {
      		testAStarNoOracle();
      	}
      }
 

 
     
    // Méthodes pour les tests
    /* @param : String mapName
     * 			int originId
     * 			int destinationId
     * 			int filterId
     * @return : ShortestPathSolution[2] Tableau avec les 2 solutions (AStar et Bellman-Ford)
     */
    
    private ShortestPathSolution[] TestDijkstra_Astar(String mapName, int originId, int destinationId, int filterId) throws Exception {
        List<ArcInspector> filters = ArcInspectorFactory.getAllFilters();
        ShortestPathSolution[] Solutions = new ShortestPathSolution[2];
        // Create a graph reader.
        GraphReader reader = new BinaryGraphReader(
        new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
        //Read the graph.
        Graph graph = reader.read();
        if(originId == -1){
        	int nbNode = graph.getSize();
			originId = (int) (Math.random()*nbNode); // generation des id aléatoires
			destinationId = (int) (Math.random()*nbNode);
        } 
        Node origin = graph.get(originId);
        Node destination = graph.get(destinationId);
        ShortestPathData data = new ShortestPathData(graph, origin, destination, filters.get(filterId));
      //Solution Bellman Ford
        BellmanFordAlgorithm algoBF = new BellmanFordAlgorithm(data);
        Solutions[0] = algoBF.run();
        //Solution Astar
        AStarAlgorithm algoAstar = new AStarAlgorithm(data);
        Solutions[1] = algoAstar.run();
        return Solutions;
    }
}


