package interfacciaAmministratore;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;

import daoCredito.DAOFactoryCredito;
import daoCreditoDB2.Db2DAOFactoryCredito;
import daoOperatori.DAOFactoryOperatori;
import daoOperatoriDB2.Db2DAOFactoryOperatori;
import dominioBonifico.Bonifici;
import dominioBonifico.Bonifico;
import dominioCredito.Cliente;
import dominioCredito.Crediti;
import dominioCredito.Credito;
import dominioLog.Entry;
import dominioLog.Log;
import dominioOperatore.Operatore;
import gestioneDati.AccessoDirettoAiDatiController;
import gestioneDati.ImpostazioneParametriController;
import gestioneManualeCredito.GestioneManualeController;
import gestioneOperatori.GestioneOperatoriController;
import interfacciaAutenticazione.LoginDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import log.LogController;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;

public class HomeAmministratore implements Initializable {
	@FXML
	private TextField iPercMulta, iRitardoCredito, iRitardoBlacklist, iFormatoCausale;
	@FXML
	private TextField iNomeOperatore, iCognomeOperatore, iTelefonoOperatore, iUsrOperatore, iPwdOperatore, iConfPwdOp;
	@FXML
	private TextField selEliminaOp, selSbloccaOp;
	@FXML
	private TableView<Cliente> tableClienti;
	@FXML
	private TableView<Credito> tableCrediti;
	@FXML
	private TableView<Bonifico> tableBonifici;
	@FXML
	private ListView<Entry> log;
	@FXML
	private DatePicker iDataInizioLog, iDataFineLog;
	private static final int USERNAME=0, PASSWORD=1;
	private String[] credenziali;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		credenziali=new String[2];
		LoginDialog login=new LoginDialog();
		Optional<String[]> credenzialiIn=login.showAndWait();
		if(credenzialiIn.isEmpty())
			System.exit(0);
		else
			credenziali=credenzialiIn.get();
		
		tableClienti.setEditable(true);
		tableCrediti.setEditable(true);
		tableBonifici.setEditable(true);
	}
	@FXML
	private void selezioneDati(ActionEvent event) {
		GestioneManualeController controller = new GestioneManualeController(Db2DAOFactoryCredito.getDAOFactoryCredito(DAOFactoryCredito.DB2));

		Crediti crediti = controller.visualizzaCrediti();
		ObservableList<Cliente> olCliente = FXCollections.observableArrayList();
		ObservableList<Credito> olCrediti = FXCollections.observableArrayList();
		for(Credito credito : crediti) {
			olCliente.add(credito.getCliente());
			olCrediti.add(credito);
		}
		tableClienti.setItems(olCliente);
		tableCrediti.setItems(olCrediti);
		
		Bonifici bonifici = controller.visualizzaTuttiBonifici();
		ObservableList<Bonifico> olBonifico = FXCollections.observableArrayList();
		for(Bonifico bonifico : bonifici) {
			olBonifico.add(bonifico);
		}
		tableBonifici.setItems(olBonifico);
	}
	@FXML
	private void selezioneParametri(ActionEvent event) {
		iPercMulta.clear();
		iRitardoCredito.clear();
		iRitardoBlacklist.clear();
		iFormatoCausale.clear();
	}
	@FXML
	private void selezioneGestioneOperatori(ActionEvent event) {
		iNomeOperatore.clear();
		iCognomeOperatore.clear();
		iTelefonoOperatore.clear();
		iUsrOperatore.clear();
		iPwdOperatore.clear();
		iConfPwdOp.clear();
		
		selEliminaOp.clear();
		selSbloccaOp.clear();
	}
	@FXML
	private void selezioneLog(ActionEvent event) {
		LogController controller = new LogController();
		
		Log entries = controller.getLog();
		
		ObservableList<Entry> olLog = FXCollections.observableArrayList();
		for(Entry entry : entries) {
			olLog.add(entry);
		}
		
		log.setItems(olLog);
	}
	@FXML
	private void aggiornaDati(ActionEvent event)
	{
		AccessoDirettoAiDatiController controller = new AccessoDirettoAiDatiController(Db2DAOFactoryCredito.getDAOFactoryCredito(DAOFactoryCredito.DB2));
		
		ObservableList<Cliente> olClienti = tableClienti.getItems();
		for(Cliente cliente : olClienti) {
			controller.modificaDati(cliente);
		}
		
		ObservableList<Credito> olCrediti = tableCrediti.getItems();
		for(Credito credito : olCrediti) {
			controller.modificaDati(credito);
		}
		
		ObservableList<Bonifico> olBonifici = tableBonifici.getItems();
		for(Bonifico bonifico : olBonifici) {
			controller.modificaDati(bonifico);
		}
	}
	@FXML
	private void aggiornaMulta(ActionEvent event)
	{
		ImpostazioneParametriController controller = new ImpostazioneParametriController(Db2DAOFactoryCredito.getDAOFactoryCredito(DAOFactoryCredito.DB2));
		
		try {
			double percentualeMulta = Double.parseDouble(iPercMulta.getCharacters().toString());
			
			if(percentualeMulta < 0) {
				MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Percentuale multa negativa", "Percentuale multa non può essere negativa");
				return;
			}
			
			controller.impostaPercentualeMulta(percentualeMulta);
		} catch (NullPointerException | NumberFormatException e) {
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Percentuale multa mancante", "Inserire percentuale multa");
			return;
		}
	}
	@FXML
	private void aggiornaRitardoCredito(ActionEvent event)
	{
		ImpostazioneParametriController controller = new ImpostazioneParametriController(Db2DAOFactoryCredito.getDAOFactoryCredito(DAOFactoryCredito.DB2));
		
		try {
			int ritardoCredito = Integer.parseInt(iRitardoCredito.getCharacters().toString());
			
			if(ritardoCredito <= 0) {
				MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Ritardo credito non positivo", "Intervallo ritardo credito deve essere positivo");
				return;
			}
		
			controller.impostaIntervalloRitardoCredito(ritardoCredito);
		} catch (NullPointerException | NumberFormatException e) {
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Intervallo ritardo credito mancante", "Inserire intervallo ritardo credito");
			return;
		}
	}
	@FXML
	private void aggiornaRitardoBlacklist(ActionEvent event)
	{
		ImpostazioneParametriController controller = new ImpostazioneParametriController(Db2DAOFactoryCredito.getDAOFactoryCredito(DAOFactoryCredito.DB2));
		
		try {
			int ritardoBlacklist = Integer.parseInt(iRitardoBlacklist.getCharacters().toString());
			
			if(ritardoBlacklist <= 0) {
				MsgDialog.showAndWait(AlertType.ERROR, "Errore", "IntervalloRitardoBlacklist non positivo", "Intervallo ritardo blacklist deve essere un positivo");
				return;
			}
		
			controller.impostaIntervalloRitardoCredito(ritardoBlacklist);
		} catch (NullPointerException | NumberFormatException e) {
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Intervallo ritardo blacklist mancante", "Inserire intervallo ritardo blacklist");
			return;
		}
	}
	@FXML
	private void aggiornaFormatoCausale(ActionEvent event)
	{
		ImpostazioneParametriController controller = new ImpostazioneParametriController(Db2DAOFactoryCredito.getDAOFactoryCredito(DAOFactoryCredito.DB2));
		
		String formatoCausale = iFormatoCausale.getCharacters().toString();
		
		if(formatoCausale.length() <= 0) {
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Formato causale mancante", "Inserire una regex per il formato della causale");
			return;
		}
		
		if(!isRegex(formatoCausale)) {
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Regex formato causale", "Il formato causale inserito non è una regular expression");
			return;
		}
		
		controller.impostaFormatoCausale(formatoCausale);
	}
	@FXML
	private void clickCreaOp(ActionEvent event)
	{
		Operatore operatore = new Operatore();
		
		String nomeOperatore = iNomeOperatore.getCharacters().toString();
		if(nomeOperatore.isBlank()) {
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Nome operatore mancante", "Inserire il nome dell'operatore");
			return;
		}
		operatore.setNome(nomeOperatore);
		
		String cognomeOperatore = iNomeOperatore.getCharacters().toString();
		if(cognomeOperatore.isBlank()) {
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Cognome operatore mancante", "Inserire il cognome dell'operatore");
			return;
		}
		operatore.setCognome(cognomeOperatore);
		
		String telefonoOperatore = iTelefonoOperatore.getCharacters().toString();
		if(telefonoOperatore.isBlank()) {
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Telefono operatore mancante", "Inserire il numero di telefono dell'operatore");
			return;
		}
		operatore.setTelefono(telefonoOperatore);
		
		String usernameOperatore = iUsrOperatore.getCharacters().toString();
		if(usernameOperatore.isBlank()) {
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Username operatore mancante", "Inserire lo username dell'operatore");
			return;
		}
		operatore.setUsername(usernameOperatore);
		
		String pwdOperatore = iPwdOperatore.getCharacters().toString();
		if(pwdOperatore.isBlank()) {
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Password operatore mancante", "Inserire la password dell'operatore");
			return;
		}
		
		String confPwdOperatore = iConfPwdOp.getCharacters().toString();
		if(confPwdOperatore.isBlank()) {
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Conferma password operatore mancante", "Reinserire la password dell'operatore per confermare");
			return;
		}
		
		if(!pwdOperatore.equals(confPwdOperatore)) {
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Conferma password errata", "Reinserire la password dell'operatore per confermare");
			return;
		}
		
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA3-256");
		} catch (NoSuchAlgorithmException e) {
			if(pwdOperatore.length() <= 0) {
				MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Algoritmo di hashing errato", "Contattare l'amministratore");
				return;
			}
		}
		String hash = bytesToHex(digest.digest(pwdOperatore.getBytes()));
		
		GestioneOperatoriController controller = new GestioneOperatoriController(Db2DAOFactoryOperatori.getDAOFactoryOperatori(DAOFactoryOperatori.DB2));
		controller.aggiungiOperatore(operatore, hash);
	}
	@FXML
	private void clickEliminaOp(ActionEvent event)
	{
		GestioneOperatoriController controller = new GestioneOperatoriController(Db2DAOFactoryOperatori.getDAOFactoryOperatori(DAOFactoryOperatori.DB2));
		
		String username = selEliminaOp.getCharacters().toString();
		if(username.length() <= 0) {
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Selezionare un operatore", "Selezionare un operatore da eliminare");
			return;
		}
		
		controller.rimuoviOperatore(username);
	}
	@FXML
	private void clickSbloccaOp(ActionEvent event)
	{
		GestioneOperatoriController controller = new GestioneOperatoriController(Db2DAOFactoryOperatori.getDAOFactoryOperatori(DAOFactoryOperatori.DB2));
		
		String username = selSbloccaOp.getCharacters().toString();
		if(username.length() <= 0) {
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Selezionare un operatore", "Selezionare un operatore da sbloccare");
			return;
		}
		
		controller.sbloccaOperatore(username);
	}
	@FXML
	private void filtraLog(ActionEvent event)
	{
		LogController controller = new LogController();
		
		LocalDate dataInizio = iDataInizioLog.getValue();
		if(dataInizio == null) {
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Data mancante", "Selezionare una data iniziale");
			return;
		}
		
		LocalDate dataFine = iDataFineLog.getValue();
		if(dataFine == null) {
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Data mancante", "Selezionare una data finale");
			return;
		}
		
		if(dataInizio.isAfter(dataFine)) {
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Date invertite", "La data iniziale non può seguire quella finale");
			return;
		}
		
		LocalDateTime inizio = LocalDateTime.of(dataInizio, LocalTime.of(0, 0));
		LocalDateTime fine = LocalDateTime.of(dataFine, LocalTime.of(0, 0));
		
		HashSet<Entry> logFiltrato = controller.getEntry(inizio, fine);
		
		ObservableList<Entry> olLog = FXCollections.observableArrayList();
		for(Entry entry : logFiltrato) {
			olLog.add(entry);
		}
		
		log.setItems(olLog);
	}
	
	//funzione di utilità per convertire in esadecimale
	private static String bytesToHex(byte[] hash) {
	    StringBuilder hexString = new StringBuilder(2 * hash.length);
	    for (int i = 0; i < hash.length; i++) {
	        String hex = Integer.toHexString(0xff & hash[i]);
	        if(hex.length() == 1) {
	            hexString.append('0');
	        }
	        hexString.append(hex);
	    }
	    return hexString.toString();
	}
	
	//funzione di utilità molto basilare per verificare se una stringa sia una regex (quasi sempre true)
	private boolean isRegex(String str) {
	    try {
	        java.util.regex.Pattern.compile(str);
	        return true;
	    } catch (java.util.regex.PatternSyntaxException e) {
	        return false;
	    }
	}
}
