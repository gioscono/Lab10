package it.polito.tdp.porto.model;

import java.util.HashMap;
import java.util.Map;

public class PaperIdMap {

	private Map<Integer, Paper> map;
	
	
	public PaperIdMap() {
		map = new HashMap<>() ;
	}

	public Paper put(Paper articolo) {
		Paper old = map.get(articolo.getEprintid()) ; 
		if(old==null) {
			map.put(articolo.getEprintid(), articolo) ;
			return articolo ;
		} else {
			return old ;
		}
	}
	
	public Paper get(int id){
		return map.get(id);
	}
}
