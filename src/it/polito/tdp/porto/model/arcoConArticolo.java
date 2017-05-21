package it.polito.tdp.porto.model;

import org.jgrapht.graph.DefaultEdge;

public class arcoConArticolo extends DefaultEdge{
	
	private Paper p;

	public arcoConArticolo() {
		super();
	}

	public Paper getP() {
		return p;
	}

	public void setP(Paper p) {
		this.p = p;
	}
	
	

}
