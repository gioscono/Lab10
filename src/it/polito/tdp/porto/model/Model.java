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
	private Graph<Author, arcoConArticolo> grafo;
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
	
	public Graph<Author, arcoConArticolo> getGrafo(){
		if(grafo==null){
			this.creaGrafo();
		}
		return grafo;
	}
	
	private void creaGrafo(){
		
			grafo = new SimpleGraph<Author, arcoConArticolo>(arcoConArticolo.class);
			
			Graphs.addAllVertices(grafo, autori);
			
			for(Author a : autori){
				for(Author coautore : this.getCoautori(a)){
					arcoConArticolo arco = grafo.addEdge(a, coautore);
					if(arco!=null){
						arco.setP(dao.getArticoloComune(a, coautore, paperIdMap));
						//System.out.println(arco.getP().toString());
					}
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
		DijkstraShortestPath<Author,arcoConArticolo> percorso = new  DijkstraShortestPath<Author,arcoConArticolo>(grafo, a1, a2); 
		List<arcoConArticolo> listaArchi = percorso.getPathEdgeList();
		if(listaArchi!=null){
			for(arcoConArticolo arco : listaArchi){
				System.out.println(arco);
				listaPaper.add(arco.getP());
				System.out.println(arco.getP().toString());
			}
		return listaPaper;
		}else{
			return null;
		}
		
	}
		
}
