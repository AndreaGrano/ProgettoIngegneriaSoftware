package interfacciaOperatore;

//import java.net.URL;
//import java.util.Optional;
//import java.util.ResourceBundle;

import daoCredito.DAOFactoryCredito;
import daoCreditoDB2.Db2DAOFactoryCredito;
import dominioBonifico.BonificiNonRiconciliati;

//import com.google.gson.*;

import dominioBonifico.Bonifico;
import dominioBonifico.BonificoNonRiconciliato;
import dominioCredito.Cliente;
import dominioCredito.Crediti;
import dominioCredito.CreditiNonRiconciliati;
import dominioCredito.Credito;
import dominioCredito.CreditoNonRiconciliato;
import dominioCredito.CreditoScaduto;
import gestioneManualeCredito.GestioneManualeController;
import interfacciaAmministratore.MsgDialog;
//import interfacciaAutenticazione.LoginDialog;
import javafx.fxml.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import java.time.LocalDate;
import javafx.scene.control.TextArea;
public class HomeOperatore /*implements Initializable */{
	@FXML
	private TextField iNome, iCognome, iTelefono, iIndirizzo, iCodFisc, iCausale, iImporto;
	@FXML
	private DatePicker iDataNascita, iDataStipula;
	@FXML
	private ListView<BonificoNonRiconciliato> listBonificiNonRiconciliati;
	@FXML
	private ListView<Bonifico> listTuttiBonifici;
	@FXML
	private ListView<CreditoNonRiconciliato> listCreditiNonRiconciliati;
	@FXML
	private ListView<Credito> listTuttiCrediti;
	@FXML
	private ListView<CreditoScaduto> listCreditiScaduti;
	@FXML
	private TextArea infoApp;
//	private static final int USERNAME=0, PASSWORD=1;
//	private String[] credenziali;
//	@Override
//	public void initialize(URL arg0, ResourceBundle arg1) {
//		credenziali=new String[2];
//		LoginDialog login=new LoginDialog();
//		Optional<String[]> credenzialiIn=login.showAndWait();
//		if(credenzialiIn.isEmpty())
//			System.exit(0);
//		else
//			credenziali=credenzialiIn.get();
//	}
	@FXML
	private void selezioneRegistrazione(ActionEvent event) {
		iNome.clear();
		iCognome.clear();
		iTelefono.clear();
		iIndirizzo.clear();
		iCodFisc.clear();
		
		iCausale.clear();
		iImporto.clear();
	}
	@FXML
	private void selezioneRiconciliazione(ActionEvent event) {
		GestioneManualeController controller = new GestioneManualeController(Db2DAOFactoryCredito.getDAOFactoryCredito(DAOFactoryCredito.DB2));

		BonificiNonRiconciliati bonificiNonRiconciliati = controller.visualizzaBonificiNonRiconciliati();
		ObservableList<BonificoNonRiconciliato> olBonifici = FXCollections.observableArrayList();
		for(BonificoNonRiconciliato bonificoNonRiconciliato : bonificiNonRiconciliati) {
			olBonifici.add(bonificoNonRiconciliato);
		}
		
		Crediti crediti = controller.visualizzaCrediti();
		ObservableList<CreditoNonRiconciliato> olCrediti = FXCollections.observableArrayList();
		for(Credito credito : crediti) {
			if(credito instanceof CreditoNonRiconciliato) {
				olCrediti.add((CreditoNonRiconciliato) credito);
			}
		}
		
		listBonificiNonRiconciliati.setItems(olBonifici);
		listCreditiNonRiconciliati.setItems(olCrediti);
	}
	@FXML
	private void selezioneVisualizzaBonifici(ActionEvent event) {
		listTuttiBonifici.setItems(FXCollections.observableArrayList());
	}
	@FXML
	private void selezioneVisualizzaCrediti(ActionEvent event) {
		listTuttiCrediti.setItems(FXCollections.observableArrayList());
	}
	@FXML
	private void selezioneVisualizzaCreditiScaduti(ActionEvent event) {
		listCreditiScaduti.setItems(FXCollections.observableArrayList());
	}
	@FXML
	private void selezioneConfigurazione(ActionEvent event) {
		infoApp.clear();
	}
	@FXML
	private void eseguiInserimentoCredito(ActionEvent event)
	{
		String nome = iNome.getCharacters().toString();
		if(nome.isBlank()) {
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Nome cliente mancante", "Inserire il nome del cliente");
			return;
		}
		
		String cognome = iCognome.getCharacters().toString();
		if(cognome.isBlank()) {
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Cognome cliente mancante", "Inserire il cognome del cliente");
			return;
		}
		
		LocalDate dataNascita = iDataNascita.getValue();
		
		String telefono = iTelefono.getCharacters().toString();
		if(telefono.isBlank()) {
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Telefono cliente mancante", "Inserire il telefono del cliente");
			return;
		}
		
		String codFisc = iCodFisc.getCharacters().toString();
		if(codFisc.isBlank()) {
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Codice fiscale cliente mancante", "Inserire il codice fiscale del cliente");
			return;
		}
		
		Cliente cliente = new Cliente();
		cliente.setNome(nome);
		cliente.setCognome(cognome);
		cliente.setDataNascita(dataNascita);
		cliente.setTelefono(telefono);
		cliente.setCodiceFiscale(codFisc);
		
		String causale = iCausale.getCharacters().toString();
		if(causale.isBlank()) {
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Causale credito mancante", "Inserire la causale del credito");
			return;
		}
		
		LocalDate dataStipula = iDataStipula.getValue();
		
		double importo;
		try {
			importo = Double.parseDouble(iImporto.getCharacters().toString());
			if(importo <= 0) {
				MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Importo credito non positivo", "L'importo del credito deve essere positivo");
				return;
			}
		} catch (NullPointerException | NumberFormatException e) {
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Importo credito mancante", "Inserire l'importo del credito");
			return;
		}
		
		GestioneManualeController controller = new GestioneManualeController(Db2DAOFactoryCredito.getDAOFactoryCredito(DAOFactoryCredito.DB2));
		int esito = controller.registraCredito(cliente, importo, dataStipula, causale);
		
		if(esito == -1) {
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Cliente in blacklist", "Il cliente è in blacklist");
			return;
		}
		
		if(esito == -2) {
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Causale mal formattata", "Inserire una causale nel formato in vigore");
			return;
		}
	}
	@FXML
	private void eseguiRiconciliazioneManuale(ActionEvent event)
	{
		BonificoNonRiconciliato bonificoNonRiconciliato = listBonificiNonRiconciliati.getSelectionModel().getSelectedItem();
		ObservableList<CreditoNonRiconciliato> creditiSelezionati = listCreditiNonRiconciliati.getSelectionModel().getSelectedItems();
		
		CreditiNonRiconciliati creditiNonRiconciliati = new CreditiNonRiconciliati();
		for(CreditoNonRiconciliato creditoNonRiconciliato : creditiSelezionati) {
			creditiNonRiconciliati.add(creditoNonRiconciliato);
		}
		
		GestioneManualeController controller = new GestioneManualeController(Db2DAOFactoryCredito.getDAOFactoryCredito(DAOFactoryCredito.DB2));
		controller.riconciliazioneManuale(bonificoNonRiconciliato, creditiNonRiconciliati);
	}
	@FXML
	private void eseguiScaricamentoBonifici(ActionEvent event)
	{
		GestioneManualeController controller = new GestioneManualeController(Db2DAOFactoryCredito.getDAOFactoryCredito(DAOFactoryCredito.DB2));
		
		ObservableList<Bonifico> olBonifici = FXCollections.observableArrayList();
		for(Bonifico bonifico : controller.visualizzaTuttiBonifici()) {
			olBonifici.add(bonifico);
		}
		
		listTuttiBonifici.setItems(olBonifici);
	}
	@FXML
	private void eseguiScaricamentoCrediti(ActionEvent event)
	{
		GestioneManualeController controller = new GestioneManualeController(Db2DAOFactoryCredito.getDAOFactoryCredito(DAOFactoryCredito.DB2));
		
		ObservableList<Credito> olCrediti = FXCollections.observableArrayList();
		for(Credito credito : controller.visualizzaCrediti()) {
			olCrediti.add(credito);
		}
		
		listTuttiCrediti.setItems(olCrediti);
	}
	@FXML
	private void eseguiScaricamentoCreditiScaduti(ActionEvent event)
	{
		GestioneManualeController controller = new GestioneManualeController(Db2DAOFactoryCredito.getDAOFactoryCredito(DAOFactoryCredito.DB2));

		ObservableList<CreditoScaduto> olCreditiScaduti = FXCollections.observableArrayList();
		for(CreditoScaduto creditoScaduto : controller.visualizzaCreditiScaduti()) {
			olCreditiScaduti.add(creditoScaduto);
		}
		
		listCreditiScaduti.setItems(olCreditiScaduti);
	}
	@FXML
	private void visualizzaVersione(ActionEvent event)
	{
		infoApp.setText("Versione applicazione: 1.0.0\nVersione JRE: "+Runtime.version());
	}
	@FXML
	private void visualizzaElencoAggiornamenti(ActionEvent event)
	{
		infoApp.setText("Nessun aggiornamento disponibile");
	}
	@FXML
	private void eseguiInstallaAggiornamenti(ActionEvent event)
	{
		MsgDialog.showAndWait(AlertType.INFORMATION, "Info", "Aggiornamenti", "Nessun aggiornamento disponibile");
		return;
	}
}
