package org.insa.algo.shortestpath;

import org.insa.graph.Node;

public class Label implements Comparable<Label> {

	private Node noeud;
	protected double cost;
	private Node father;
	private int mark;
	
	
    public Label(Node noeud) {
    	this.noeud = noeud;
    	this.cost = Double.POSITIVE_INFINITY;
    	this.father = null;
    	this.mark = 0;
    }
    
    public Node getNoeud() {
    	return this.noeud;
    }
    public double getCost() {
    	return this.cost;
    }
    public Node getFather() {
    	return this.father;
    }
    public int getMark() {
    	return this.mark;
    }
    public void setMark(int mark) {
    	this.mark = mark;
    }
    public void setCost(double cost) {
    	this.cost = cost;
    }
    public void setFather(Node father) {
    	this.father = father;
    }
    public double getTotalCost() {
    	return this.cost;
    }
    public double getCoutMin() {
    	return 0;
    }
    public int compareTo(Label l) {
    	if(this.getTotalCost() < l.getTotalCost()) {
    		return -1;
    	} else if (this.getTotalCost() > l.getTotalCost()) {
    		return 1;
    	} else {
    		if(this.getCoutMin() < l.getCoutMin()) {
    			return -1;
    		} else {
    			return 1;
    		}
    	}
    }
}