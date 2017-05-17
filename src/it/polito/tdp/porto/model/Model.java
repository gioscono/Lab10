package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	
	List<Author> autori;
	PortoDAO dao = new PortoDAO();
	private Graph<Author, DefaultEdge> grafo;
	private AuthorIdMap authorIdMap;
	private PaperIdMap paperIdMap;
	
	public Model(){
		this.authorIdMap = new AuthorIdMap();
		this.paperIdMap = new PaperIdMap();
	}
	
	public List<Author> getAutori(){
		
		autori = dao.getAutori(authorIdMap);
		return autori;
	}
	
	public List<Author> getCoautori(Author a){
		
		List<Author> coautori = dao.getCoautori(a.getId(), this.authorIdMap);
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

	public void creaListaArticoliAutore() {

		for(Author a : autori){
			a.setArticoli(dao.getArticoliAutore(a.getId(), this.paperIdMap));
		}
	} 
	public List<Author> getAutoriSecondaBox(Author selez){
		List<Author> autori2 = new ArrayList<Author>();
		autori2.addAll(autori);
		autori2.removeAll(Graphs.neighborListOf(grafo, selez));
		autori2.remove(selez);
		return autori2;		
	}
	
	
	
	
public List<Paper> getPaperList(Author a1, Author a2){
		
		List<Paper> listaPaper = new ArrayList<Paper>();
		DijkstraShortestPath<Author,DefaultEdge> percorso = new  DijkstraShortestPath<Author,DefaultEdge>(grafo, a1, a2); 
		List<DefaultEdge> listaArchi = percorso.getPathEdgeList();
		for(DefaultEdge arco : listaArchi){
			System.out.println(arco);
			Author a = grafo.getEdgeSource(arco);
			Author b = grafo.getEdgeTarget(arco);
			boolean trovato = false;
			List<Paper> paperPa = a.getArticoli();
			List<Paper> paperPb = b.getArticoli();
			for(int i = 0; i< paperPa.size() && trovato==false; i++){
				for(int j=0; j<paperPb.size() && trovato==false; j++){
					if(paperPa.get(i).equals(paperPb.get(j))){
						listaPaper.add(paperPa.get(i));
						trovato = true;
					}
				}
			}
			
			
		}
		
		return listaPaper;
	}
	
	
	
	
}
