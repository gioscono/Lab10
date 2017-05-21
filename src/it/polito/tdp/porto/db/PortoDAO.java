package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.AuthorIdMap;
import it.polito.tdp.porto.model.Paper;
import it.polito.tdp.porto.model.PaperIdMap;

public class PortoDAO {

	/*
	 * Dato l'id ottengo l'autore.
	 */
	public Author getAutore(int id) {

		final String sql = "SELECT * FROM author where id=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				return autore;
			}

			conn.close();
			return null;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public List<Author> getAutori(AuthorIdMap authorIdMap) {

		final String sql = "SELECT * FROM author";
		List<Author> autori = new ArrayList<Author>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Author a = authorIdMap.get(rs.getInt("id"));
				if(a==null){
					 a = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
					 authorIdMap.put(a);
				}
				
				autori.add(a);
			}
			
			conn.close();
			return autori;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	/*
	 * Dato l'id ottengo l'articolo.
	 */
	public Paper getArticolo(int eprintid) {

		final String sql = "SELECT * FROM paper where eprintid=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, eprintid);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				return paper;
			}

			conn.close();
			return null;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	
	public List<Paper> getArticoli() {

		final String sql = "SELECT * FROM paper where eprintid=?";
		List<Paper> articoli = new ArrayList<Paper>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				articoli.add(paper);
			}

			conn.close();
			return articoli;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public List<Author> getCoautori(int id, AuthorIdMap authorIdMap){
		
		final String sql = "Select distinct id, lastname, firstname "+
                           "from creator as c1, creator as c2, author as au "+
                           "where c1.authorid=? and c1.eprintid=c2.eprintid and au.id=c2.authorid and c1.authorid<> c2.authorid ";
		List<Author> coautori= new ArrayList<Author>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				Author a = authorIdMap.get(rs.getInt("id"));
				if(a==null){
					a = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
					authorIdMap.put(a);
				}
				coautori.add(a);
			}

			conn.close();
			return coautori;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
		
		
		
		
		
	}

	public List<Paper> getArticoliAutore(int id, PaperIdMap map) {
		final String sql = "select p.eprintid, p.title,p.issn, p.publication, p.`type`, p.types "+
                           "from paper as p, creator as c "+
                           "where p.eprintid=c.eprintid and c.authorid=?";
		List<Paper> articoli = new ArrayList<Paper>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setInt(1, id);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Paper p = map.get(rs.getInt("eprintid"));
				if(p==null){
				 p = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				 map.put(p);
				}
				articoli.add(p);
			}

			conn.close();
			return articoli;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}

	}

	public Paper getArticoloComune(Author autore, Author coautore, PaperIdMap paperIdMap) {
		final String sql = "select c1.eprintid, p.title, p.issn, p.publication, p.`type`, p.types "+
                           "from creator as c1, creator as c2, paper as p "+
                           "where c1.eprintid=c2.eprintid and c1.authorid=? and c2.authorid=? and p.eprintid=c1.eprintid";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setInt(1, autore.getId());
			st.setInt(2, coautore.getId());
			ResultSet rs = st.executeQuery();

			Paper p = null;
			if (rs.next()) {
				p = paperIdMap.get(rs.getInt("eprintid"));
				if(p==null){
					 p = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
							rs.getString("publication"), rs.getString("type"), rs.getString("types"));
					 paperIdMap.put(p);
				}
			}

			conn.close();
			return p;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

}