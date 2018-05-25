package org.insa.algo.shortestpath;
import org.insa.graph.Node;

public class LabelStar extends Label implements Comparable<Label> {
	/*
	 * Attribut supplémentaire
	 */
	private double coutmin;	

	public LabelStar(Node n) {
		super(n);
		this.coutmin = Double.POSITIVE_INFINITY;
	}
	
	/*
	 * Méthodes
	 */
	
	@Override
	public double getTotalCost() {
		return this.coutmin + this.cost;
	}
	
	public void setCoutMin(double val) {
		this.coutmin = val;
	}
	
	public double getCoutMin() {
		return this.coutmin;
	}
	
}