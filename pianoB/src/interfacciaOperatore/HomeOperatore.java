package interfacciaOperatore;

import daoCredito.DAOFactoryCredito;
import daoCreditoDB2.Db2DAOFactoryCredito;
import daoOperatori.DAOFactoryOperatori;
import daoOperatoriDB2.Db2DAOFactoryOperatori;
import dominioBonifico.BonificiNonRiconciliati;

import dominioBonifico.Bonifico;
import dominioBonifico.BonificoNonRiconciliato;
import dominioCredito.Cliente;
import dominioCredito.Crediti;
import dominioCredito.CreditiNonRiconciliati;
import dominioCredito.Credito;
import dominioCredito.CreditoNonRiconciliato;
import dominioCredito.CreditoScaduto;
import gestioneManualeCredito.GestioneManualeController;
import gestioneOperatori.GestioneOperatoriController;
import interfacciaAmministratore.MsgDialog;
import interfacciaAutenticazione.LoginDialog;
import javafx.fxml.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import autenticazione.AutenticazioneController;
import javafx.scene.control.TextArea;

public class HomeOperatore implements Initializable {
	private static final int USERNAME=0, HASH=1;
	private String[] credenziali;
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
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		boolean autenticato = false;
		HashMap<String,Integer> errori = new HashMap<String,Integer>();
		AutenticazioneController controller = new AutenticazioneController(Db2DAOFactoryOperatori.getDAOFactoryOperatori(DAOFactoryCredito.DB2));
		do {
			credenziali=new String[2];
			LoginDialog login=new LoginDialog();
			Optional<String[]> credenzialiIn = login.showAndWait();
			if(credenzialiIn.isEmpty())
				System.exit(0);
			else
				credenziali=credenzialiIn.get();
			
			try {
				autenticato = controller.verificaCredenziali(credenziali[USERNAME], credenziali[HASH]);
				
				if(!autenticato) {
					errori.merge(credenziali[USERNAME], 1, Integer::sum);
			
					if(errori.get(credenziali[USERNAME]) >= 3) {
						GestioneOperatoriController controllerOperatori = new GestioneOperatoriController(Db2DAOFactoryOperatori.getDAOFactoryOperatori(DAOFactoryOperatori.DB2));
						controllerOperatori.bloccaOperatore(credenziali[USERNAME]);
						
						MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Operatore bloccato", "L'operatore " + credenziali[USERNAME] + " è stato bloccato, contattare l'amministratore");
					} else {
						MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Credenziali errate o operatore bloccato", "Reinserire le credenziali o contattare l'amministratore");
					}
				}
			} catch(IllegalArgumentException e) {
				MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Operatore inesistente", "L'operatore " + credenziali[USERNAME] + " è inesistente");
			}
			
			if(credenziali[USERNAME].equals("admin")) {
				MsgDialog.showAndWait(AlertType.WARNING, "Attenzione", "Client operatore", "Questo è un cliente operatore, le funzionalità dell'amministratore non sono disponibili");
			}
		} while(!autenticato);
		
		listCreditiNonRiconciliati.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}
	
	@FXML
	private void selezioneRegistrazione() {
		iNome.setText("");
		iCognome.setText("");
		iTelefono.setText("");
		iIndirizzo.setText("");
		iCodFisc.setText("");
		
		iCausale.setText("");
		iImporto.setText("");
	}
	@FXML
	private void selezioneRiconciliazione() {
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
	private void selezioneVisualizzaBonifici() {
		listTuttiBonifici.setItems(FXCollections.observableArrayList());
	}
	@FXML
	private void selezioneVisualizzaCrediti() {
		listTuttiCrediti.setItems(FXCollections.observableArrayList());
	}
	@FXML
	private void selezioneVisualizzaCreditiScaduti() {
		listCreditiScaduti.setItems(FXCollections.observableArrayList());
	}
	@FXML
	private void selezioneConfigurazione() {
		infoApp.setText("");
	}
	@FXML
	private void eseguiInserimentoCredito()
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
		
		String indirizzo = iIndirizzo.getCharacters().toString();
		if(indirizzo.isBlank()) {
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Indirizzo cliente mancante", "Inserire l'indirizzo del cliente");
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
		cliente.setIndirizzo(indirizzo);
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
		
		MsgDialog.showAndWait(AlertType.CONFIRMATION, "Conferma operazione", "Successo", "Operazione eseguita con successo");
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
		
		this.selezioneRiconciliazione();
		
		MsgDialog.showAndWait(AlertType.CONFIRMATION, "Conferma operazione", "Successo", "Operazione eseguita con successo");
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