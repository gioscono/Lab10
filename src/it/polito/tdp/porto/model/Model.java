package it.polito.tdp.porto.model;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	
	List<Author> autori;
	PortoDAO dao = new PortoDAO();
	private Graph<Author, DefaultEdge> grafo;
	
	public List<Author> getAutori(){
		
		autori = dao.getAutori();
		return autori;
	}
	
	public List<Author> getCoautori(Author a){
		
		List<Author> coautori = dao.getCoautori(a.getId());
		return coautori;
	}
	
	public Graph<Author, DefaultEdge> getGrafo(){
		if(grafo==null){
			this.creaGrafo();
		}
		return grafo;
	}
	
	private void creaGrafo(){
		
			grafo = new SimpleGraph<Author, DefaultEdge>(DefaultEdge.class);
			
			Graphs.addAllVertices(grafo, autori);
			
			for(Author a : autori){
				for(Author coautore : this.getCoautori(a)){
					grafo.addEdge(a, coautore);
				}
			}
		
		System.out.println(grafo);
	}

	public List<Author> getCoautoriGrafo(Author a){
		
		
		return Graphs.neighborListOf(grafo, a);
	} 
}
