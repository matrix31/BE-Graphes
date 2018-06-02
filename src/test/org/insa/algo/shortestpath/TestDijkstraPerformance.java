package org.insa.algo.shortestpath;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.List;

import org.insa.algo.ArcInspector;
import org.insa.algo.ArcInspectorFactory;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.GraphReader;
import org.junit.Test;

public class TestDijkstraPerformance {
		public enum Status {
	        UNKNOWN, INFEASIBLE, FEASIBLE, OPTIMAL,
	    };
		// Small graph use for tests
	    private static Graph graph;
	
	    // List of nodes
	    private static Node[] nodes;
		@Test
	    // Dijkstra vs Astar 
	    // on peut changer les cartes et les filters 
	    
	    public void TestPerformanceDijkstra_Astar() throws Exception{
	    	
	    	int compteurDij= 0 ;
	    	int compteurAstar = 0 ;
	    	int egalite = 0 ;   //egalite a 3%
	    	
	    	List<ArcInspector> filters = ArcInspectorFactory.getAllFilters();
	    	String mapName = "./src/maps/haute-garonne.mapgr";
		// Create a graph reader.
	    GraphReader reader = new BinaryGraphReader(
	    new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
	    //Read the graph.
	    graph = reader.read();
	    int nbNode = graph.getSize();
	    	
	    	  
	    for ( int i=0;i<100;i+=1) {
	    	int originId = (int) (Math.random()*nbNode); // generation des id aléatoires
		    int destinationId = (int) (Math.random()*nbNode);
		    Node origin = graph.get(originId);
		    Node destination = graph.get(destinationId);
		    ShortestPathData data = new ShortestPathData(graph, origin, destination, filters.get(0));

		    DijkstraAlgorithm algoD = new DijkstraAlgorithm(data);
		    AStarAlgorithm algoA = new AStarAlgorithm(data);
		    
		    System.out.println(originId);
		    // calcul temps d'execution 
		    long debutAstar = System.nanoTime();
		    ShortestPathSolution solution = algoA.run();
		    long tempsAstar = (System.nanoTime()-debutAstar); 
		    
		    long debut_Dijkstra = System.nanoTime(); 
	        ShortestPathSolution solutionD = algoD.run();
	        long tempsDijkstra = (System.nanoTime()-debut_Dijkstra) ; // temps en ms
	       //System.out.println(solution.getPath().getLength());
	        
	        if (tempsDijkstra > tempsAstar) {
		    		if (  (tempsAstar/ tempsDijkstra) >= 0.97) {
		    			egalite++;	
		    		}
		    		else {
		    			compteurAstar++;
			    		
		    		}
		    	}
	        if ( tempsDijkstra < tempsAstar) {
	        		if ( (tempsDijkstra / tempsAstar ) >= 0.97) {
	        			egalite++;
	        		}
	        		else {
	        			compteurDij++;
	        		}
	        	if (tempsDijkstra == tempsAstar) {
	        		egalite++; // a la milliseconde pres 
	        	}
	      } 
	      System.out.println(i);
	   }
	  System.out.println("Echantillons testes : 100");
	  System.out.println("Dijkstra plus performant sur "+ compteurDij+ " tests");
	  System.out.println("Astar plus performant sur "+ compteurAstar+ " tests");
	  System.out.println("Egalité à 3% près pour "+ egalite+ " test(s)"); 
	 }


	 
	    @Test
	    // Dijkstra vs BF 
	    // on peut changer les cartes et les filters 
	    public void TestPerformanceDijkstra_BF() throws Exception{
	    	
	    	int compteurDij= 0 ;
	    	int compteurBF = 0 ;
	    	int egalite = 0 ;   //egalite a 5%
	    	
	    	List<ArcInspector> filters = ArcInspectorFactory.getAllFilters();
			String mapName = "./src/maps/toulouse.mapgr";
		// Create a graph reader.
	    GraphReader reader = new BinaryGraphReader(
	    new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
	    //Read the graph.
	    graph = reader.read();
	    int nbNode = graph.getSize();
	    	
	    	  
	    for ( int i=0;i<10;i+=1) {
	    		int originId = (int) (Math.random()*nbNode);
		    int destinationId = (int)( Math.random()*nbNode);
		    Node origin = graph.get(originId);
		    Node destination = graph.get(destinationId);
		    ShortestPathData data = new ShortestPathData(graph, origin, destination, filters.get(1));

		    BellmanFordAlgorithm algoBF = new BellmanFordAlgorithm(data);
		    DijkstraAlgorithm algoD = new DijkstraAlgorithm(data);
		    
		    // calcul temps d'execution 
		    long debutBF = System.nanoTime();
		    ShortestPathSolution solution = algoBF.doRun();
		    long tempsBF = (System.nanoTime()-debutBF); 
		    
		    long debut_Dijkstra = System.nanoTime(); 
	        ShortestPathSolution solutionD = algoD.doRun();
	        long tempsDijkstra = (System.nanoTime()-debut_Dijkstra) ; // temps en ms
	    		
	        if (tempsDijkstra > tempsBF) {
		    		if (  (tempsBF/ tempsDijkstra) >= 0.95) {
		    			egalite++;	
		    		}
		    		else {
		    			compteurBF++;
			    		
		    		}
		    	}
	        if ( tempsDijkstra < tempsBF) {
	        		if ( (tempsDijkstra / tempsBF ) >= 0.95 ) {
	        			egalite++;
	        		}
	        		else {
	        			compteurDij++;
	        		}
	        	if (tempsDijkstra == tempsBF) {
	        		egalite++;  
	        	}
	      } 
	   }
	  System.out.println("Echantillons testes : 10");
	  System.out.println("Dijkstra plus performant sur "+ compteurDij+ " tests");
	  System.out.println("Bellman-Ford plus performant sur "+ compteurBF+ " tests");
	  System.out.println("Egalité à 5% près pour "+ egalite+ " test(s)"); 
	 }
	}


