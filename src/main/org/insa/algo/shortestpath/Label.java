package org.insa.algo.shortestpath;

import org.insa.graph.Node;

public class Label implements Comparable<Label> {

	private Node noeud;
	private double cost;
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
    
    public int compareTo(Label l) {
    	if(this.cost < l.cost) {
    		return -1;
    	} else if (this.cost > l.cost) {
    		return 1;
    	} else {
    		return 0;
    	}
    }
}