package it.polito.tdp.porto;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.jgrapht.Graphs;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import it.polito.tdp.porto.model.Paper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {

	private Model model;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Author> boxPrimo;

    @FXML
    private ComboBox<Author> boxSecondo;

    @FXML
    private TextArea txtResult;

    @FXML
    void handleCoautori(ActionEvent event) {
	
	    model.getGrafo();
	    model.creaListaArticoliAutore();
	    Author selez = boxPrimo.getValue();
	    if(selez==null){
	    	txtResult.appendText("Selezionare un autore!");
	    	return;
	    }
	    List<Author> coautori = model.getCoautoriGrafo(selez);
	    for(Author a : coautori){
	    	txtResult.appendText(a.toString()+"\n");
	    }
	    boxSecondo.getItems().addAll(model.getAutoriSecondaBox(selez));
    }

    @FXML
    void handleSequenza(ActionEvent event) {

    	Author a1 = boxPrimo.getValue();
    	Author a2 = boxSecondo.getValue();
    	if(a1==null || a2== null){
    		txtResult.appendText("Selezionare entrambi gli autori!\n");
    		return;
    	}
    	List<Paper> listaPubb = model.getPaperList(a1, a2);
    	System.out.println(listaPubb);
    	   
    	for(Paper p: listaPubb){
    		txtResult.appendText(p.getTitle().toString()+"\n");
    	}
    	
    	
    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		boxPrimo.getItems().addAll(model.getAutori());
	}
}
