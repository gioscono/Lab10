package it.polito.tdp.porto.model;

import java.util.HashMap;
import java.util.Map;

public class AuthorIdMap {
	
private Map<Integer,Author> map ;
	
	public AuthorIdMap() {
		map = new HashMap<>() ;
	}

	public Author put(Author autore) {
		Author old = map.get(autore.getId()) ; 
		if(old==null) {
			map.put(autore.getId(), autore) ;
			return autore ;
		} else {
			return old ;
		}
	}
	
	public Author get(int id){
		return map.get(id);
	}
}
