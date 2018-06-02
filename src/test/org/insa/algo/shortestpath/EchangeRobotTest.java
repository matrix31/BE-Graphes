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


public class EchangeRobotTest {
	public enum Status {
        UNKNOWN, INFEASIBLE, FEASIBLE, OPTIMAL,
    };
    @Test 
    public void EchangeRobot() throws IOException {
		List<ArcInspector> filters = ArcInspectorFactory.getAllFilters();
        ShortestPathSolution[] Solutions = new ShortestPathSolution[2];
        // Create a graph reader.
        GraphReader reader = new BinaryGraphReader(
        new DataInputStream(new BufferedInputStream(new FileInputStream("./src/maps/toulouse.mapgr"))));
        //Read the graph.
        Graph graph = reader.read();
        int nbNode = graph.getSize();
        int originIdR1D1 = (int) (Math.random()*nbNode); // generation des id aléatoires
		int destinationIdR1D1 = (int) (Math.random()*nbNode);

		int originIdR2D2 = (int) (Math.random()*nbNode); // generation des id aléatoires
		int destinationIdR2D2 = (int) (Math.random()*nbNode);
		/*
		int originIdR1D1 = 17860; // generation des id aléatoires
		int destinationIdR1D1 = 263;

		int originIdR2D2 = 14289; // generation des id aléatoires
		int destinationIdR2D2 = 5890;*/
		
        Node originR1D1 = graph.get(originIdR1D1);
        Node destinationR1D1 = graph.get(destinationIdR1D1);
        ShortestPathData dataR1D1 = new ShortestPathData(graph, originR1D1, destinationR1D1, filters.get(2));
        //Solution Dijkstra1D1 
        DijkstraAlgorithm  DijkstraR1D1  = new  DijkstraAlgorithm(dataR1D1);
        Solutions[0] =  DijkstraR1D1.run();
        
        Node originR2D2 = graph.get(originIdR2D2);
        Node destinationR2D2 = graph.get(destinationIdR2D2);
        ShortestPathData dataR2D2 = new ShortestPathData(graph, originR2D2, destinationR2D2, filters.get(2));
        //Solution DijkstraR2D2 
        DijkstraAlgorithm  DijkstraR2D2  = new  DijkstraAlgorithm(dataR2D2);
        Solutions[1] =  DijkstraR2D2.run();
        double minDist = Double.POSITIVE_INFINITY;
        double val;
        Arc arcBestR1D1 = null;
        Arc arcBestR2D2 = null;
        if(Solutions[0].isFeasible() && Solutions[1].isFeasible()){
	        List<Arc> arcsR1D1 = Solutions[0].getPath().getArcs();
	        List<Arc> arcsR2D2 = Solutions[1].getPath().getArcs();
	        for(Arc arcR1D1 : arcsR1D1) {
	        	for(Arc arcR2D2 : arcsR2D2) {
	        		val = arcR1D1.getDestination().getPoint().distanceTo(arcR2D2.getDestination().getPoint());
	        		if(val < minDist){
	        			minDist = val;
	        			arcBestR1D1 = arcR1D1;
	        			arcBestR2D2 = arcR2D2;
	        		}
	            }
	        }
	        Node NodeRDV = null;
	        if(arcBestR1D1.getDestination().getId() == arcBestR2D2.getDestination().getId()){
	        	NodeRDV = arcBestR1D1.getDestination();
	        } else {
		        ShortestPathData dataMilieu = new ShortestPathData(graph, arcBestR1D1.getDestination(), arcBestR2D2.getDestination(), filters.get(2));
		        //Solution Dijkstra1D1 
		        DijkstraAlgorithm  DijkstraMilieu  = new  DijkstraAlgorithm(dataMilieu);
		        ShortestPathSolution SolutionMilieu = DijkstraMilieu.run();
		        if(SolutionMilieu.isFeasible()){
			        int solutionSize = SolutionMilieu.getPath().size();
			      	List<Arc> Arcs = SolutionMilieu.getPath().getArcs();
			      	Arc arcMilieu = Arcs.get((int) solutionSize/2);
			      	NodeRDV = arcMilieu.getDestination();
		        } else {
		        	EchangeRobot();
		        }
		    }
	      	System.out.println("Origin R1D1 = " + originIdR1D1+" / destination R1D1 =  " + destinationIdR1D1);
	      	System.out.println("Origin R2D2 = " + originIdR2D2+" / destination R2D2 =  " + destinationIdR2D2);
	      	System.out.println("Point de RDV : " + NodeRDV.getId());
	      	
	      	ShortestPathData data1R1D1 = new ShortestPathData(graph, originR1D1, NodeRDV, filters.get(2));
	        //Solution Dijkstra1D1 
	        DijkstraAlgorithm  Dijkstra1R1D1  = new  DijkstraAlgorithm(data1R1D1);
	        ShortestPathSolution Solution1R1D1 =  Dijkstra1R1D1.run();
	        
	        ShortestPathData data2R1D1 = new ShortestPathData(graph, NodeRDV, destinationR1D1, filters.get(2));
	        //Solution Dijkstra1D1 
	        DijkstraAlgorithm  Dijkstra2R1D1  = new  DijkstraAlgorithm(data2R1D1);
	        ShortestPathSolution Solution2R1D1 =  Dijkstra2R1D1.run();
	        
	        ShortestPathData data1R2D2 = new ShortestPathData(graph, originR2D2, NodeRDV, filters.get(2));
	        //Solution Dijkstra1D1 
	        DijkstraAlgorithm  Dijkstra1R2D2  = new  DijkstraAlgorithm(data1R2D2);
	        ShortestPathSolution Solution1R2D2 =  Dijkstra1R2D2.run();
	        
	        ShortestPathData data2R2D2 = new ShortestPathData(graph, NodeRDV, destinationR2D2, filters.get(2));
	        //Solution Dijkstra1D1 
	        DijkstraAlgorithm  Dijkstra2R2D2  = new  DijkstraAlgorithm(data2R2D2);
	        ShortestPathSolution Solution2R2D2 =  Dijkstra2R2D2.run();
	        
	        double timeR1D1 = Solution1R1D1.getPath().getMinimumTravelTime() + Solution2R1D1.getPath().getMinimumTravelTime();
	        double timeR2D2 = Solution1R2D2.getPath().getMinimumTravelTime() + Solution2R2D2.getPath().getMinimumTravelTime();
		        
	        System.out.println("Temps de trajet R1D1 : " + (int) timeR1D1/60 + "min " + (int) timeR1D1%60 + "sec");
	        System.out.println("Temps de trajet R2D2 : " + (int) timeR2D2/60 + "min " + (int) timeR2D2%60 + "sec");
	      	
        } else {
        	EchangeRobot();
        }
    }
}


