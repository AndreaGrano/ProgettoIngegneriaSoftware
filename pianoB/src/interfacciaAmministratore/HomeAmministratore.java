package interfacciaAmministratore;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import daoCredito.DAOFactoryCredito;
import daoCreditoDB2.Db2DAOFactoryCredito;
import daoOperatori.DAOFactoryOperatori;
import daoOperatoriDB2.Db2DAOFactoryOperatori;
import dominioBonifico.Bonifici;
import dominioBonifico.BonificiNonRiconciliati;
import dominioBonifico.Bonifico;
import dominioBonifico.BonificoNonRiconciliato;
import dominioCredito.Cliente;
import dominioCredito.Crediti;
import dominioCredito.CreditiNonRiconciliati;
import dominioCredito.Credito;
import dominioCredito.CreditoNonRiconciliato;
import dominioCredito.CreditoScaduto;
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
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import log.LogController;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;

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
	private TableColumn<Cliente, String> colNome, colCognome, colDataNascita, colTelefono, colIndirizzo, colCodFisc;
	@FXML
	private TableView<Credito> tableCrediti;
	@FXML
	private TableColumn<Credito, String> colCausaleCredito, colDataStipula, colImpCredito, colCFCliente;
	@FXML
	private TableView<Bonifico> tableBonifici;
	@FXML
	private TableColumn<Bonifico, String> colIBAN, colDataValuta, colCausaleBonifico, colImpBonifico;
	@FXML
	private ListView<Entry> log;
	@FXML
	private DatePicker iDataInizioLog, iDataFineLog;
	private static final int USERNAME=0, PASSWORD=1;
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
		credenziali=new String[2];
		LoginDialog login=new LoginDialog();
		Optional<String[]> credenzialiIn=login.showAndWait();
		if(credenzialiIn.isEmpty())
			System.exit(0);
		else
			credenziali=credenzialiIn.get();
		

		tableClienti.setEditable(true);
		colNome.setCellValueFactory(new PropertyValueFactory<Cliente,String>("nome"));
		colNome.setCellFactory(TextFieldTableCell.forTableColumn());
		colNome.setOnEditCommit(new EventHandler<CellEditEvent<Cliente,String>>() { 
			@Override
			public void handle(CellEditEvent<Cliente,String> t) {
				((Cliente) t.getTableView().getItems().get(t.getTablePosition().getRow())).setNome(t.getNewValue());
			}
		});
		colCognome.setCellValueFactory(new PropertyValueFactory<Cliente,String>("cognome"));
		colCognome.setCellFactory(TextFieldTableCell.forTableColumn());
		colCognome.setOnEditCommit(new EventHandler<CellEditEvent<Cliente,String>>() { 
			@Override
			public void handle(CellEditEvent<Cliente,String> t) {
				((Cliente) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCognome(t.getNewValue());
			}
		});
		colDataNascita.setCellValueFactory(new PropertyValueFactory<Cliente,String>("dataStringa"));
		colDataNascita.setCellFactory(TextFieldTableCell.forTableColumn());
		colDataNascita.setOnEditCommit(new EventHandler<CellEditEvent<Cliente,String>>() { 
			@Override
			public void handle(CellEditEvent<Cliente,String> t) {
				try {
					((Cliente) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDataNascita(t.getNewValue());
				} catch(Exception e) {
					e.printStackTrace();
					MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Formato data errato", "La data deve essere nel formato YYYY-MM-dd");
					return;
				}
			}
		});
		colTelefono.setCellValueFactory(new PropertyValueFactory<Cliente,String>("telefono"));
		colTelefono.setCellFactory(TextFieldTableCell.forTableColumn());
		colTelefono.setOnEditCommit(new EventHandler<CellEditEvent<Cliente,String>>() { 
			@Override
			public void handle(CellEditEvent<Cliente,String> t) {
				((Cliente) t.getTableView().getItems().get(t.getTablePosition().getRow())).setTelefono(t.getNewValue());
			}
		});
		colIndirizzo.setCellValueFactory(new PropertyValueFactory<Cliente,String>("indirizzo"));
		colIndirizzo.setCellFactory(TextFieldTableCell.forTableColumn());
		colIndirizzo.setOnEditCommit(new EventHandler<CellEditEvent<Cliente,String>>() { 
			@Override
			public void handle(CellEditEvent<Cliente,String> t) {
				((Cliente) t.getTableView().getItems().get(t.getTablePosition().getRow())).setIndirizzo(t.getNewValue());
			}
		});
		colCodFisc.setCellValueFactory(new PropertyValueFactory<Cliente,String>("codiceFiscale"));
		colCodFisc.setCellFactory(TextFieldTableCell.forTableColumn());
		colCodFisc.setOnEditCommit(new EventHandler<CellEditEvent<Cliente,String>>() { 
			@Override
			public void handle(CellEditEvent<Cliente,String> t) {
				((Cliente) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCodiceFiscale(t.getNewValue());
			}
		});
		
		tableCrediti.setEditable(true);
		colCausaleCredito.setCellValueFactory(new PropertyValueFactory<Credito,String>("causale"));
		colCausaleCredito.setCellFactory(TextFieldTableCell.forTableColumn());
		colCausaleCredito.setOnEditCommit(new EventHandler<CellEditEvent<Credito,String>>() { 
			@Override
			public void handle(CellEditEvent<Credito,String> t) {
				((Credito) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCausale(t.getNewValue());
			}
		});
		colDataStipula.setCellValueFactory(new PropertyValueFactory<Credito,String>("dataStringa"));
		colDataStipula.setCellFactory(TextFieldTableCell.forTableColumn());
		colDataStipula.setOnEditCommit(new EventHandler<CellEditEvent<Credito,String>>() { 
			@Override
			public void handle(CellEditEvent<Credito,String> t) {
				try {
					((Credito) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDataStipula(t.getNewValue());
				} catch(Exception e) {
					MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Formato data errato", "La data deve essere nel formato YYYY-MM-dd");
					return;
				}
			}
		});
		colImpCredito.setCellValueFactory(new PropertyValueFactory<Credito,String>("importoStringa"));
		colImpCredito.setCellFactory(TextFieldTableCell.forTableColumn());
		colImpCredito.setOnEditCommit(new EventHandler<CellEditEvent<Credito,String>>() { 
			@Override
			public void handle(CellEditEvent<Credito,String> t) {
				try {
					((Credito) t.getTableView().getItems().get(t.getTablePosition().getRow())).setImporto(t.getNewValue());
				} catch (NullPointerException | NumberFormatException e) {
					MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Formato importo errato", "L'importo deve essere un numero decimale");
					return;
				}
			}
		});
		colCFCliente.setCellValueFactory(new PropertyValueFactory<Credito,String>("codiceFiscale"));
		colCFCliente.setCellFactory(TextFieldTableCell.forTableColumn());
		colCFCliente.setOnEditCommit(new EventHandler<CellEditEvent<Credito,String>>() { 
			@Override
			public void handle(CellEditEvent<Credito,String> t) {
				((Credito) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCodiceFiscale(t.getNewValue());
			}
		});

		tableBonifici.setEditable(true);
		colIBAN.setCellValueFactory(new PropertyValueFactory<Bonifico,String>("IBAN"));
		colIBAN.setCellFactory(TextFieldTableCell.forTableColumn());
		colIBAN.setOnEditCommit(new EventHandler<CellEditEvent<Bonifico,String>>() { 
			@Override
			public void handle(CellEditEvent<Bonifico,String> t) {
				((Bonifico) t.getTableView().getItems().get(t.getTablePosition().getRow())).setIBAN(t.getNewValue());
			}
		});
		colDataValuta.setCellValueFactory(new PropertyValueFactory<Bonifico,String>("dataStringa"));
		colDataValuta.setCellFactory(TextFieldTableCell.forTableColumn());
		colDataValuta.setOnEditCommit(new EventHandler<CellEditEvent<Bonifico,String>>() { 
			@Override
			public void handle(CellEditEvent<Bonifico,String> t) {
				try {
					((Bonifico) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDataValuta(t.getNewValue());
				} catch(Exception e) {
					MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Formato data errato", "La data deve essere nel formato YYYY-MM-dd");
					return;
				}
			}
		});
		colCausaleBonifico.setCellValueFactory(new PropertyValueFactory<Bonifico,String>("causale"));
		colCausaleBonifico.setCellFactory(TextFieldTableCell.forTableColumn());
		colCausaleBonifico.setOnEditCommit(new EventHandler<CellEditEvent<Bonifico,String>>() { 
			@Override
			public void handle(CellEditEvent<Bonifico,String> t) {
				((Bonifico) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCausale(t.getNewValue());
			}
		});
		colImpBonifico.setCellValueFactory(new PropertyValueFactory<Bonifico,String>("importoStringa"));
		colImpBonifico.setCellFactory(TextFieldTableCell.forTableColumn());
		colImpBonifico.setOnEditCommit(new EventHandler<CellEditEvent<Bonifico,String>>() { 
			@Override
			public void handle(CellEditEvent<Bonifico,String> t) {
				try {
					((Bonifico) t.getTableView().getItems().get(t.getTablePosition().getRow())).setImporto(t.getNewValue());
				} catch(NullPointerException | NumberFormatException e) {
					MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Formato importo errato", "L'importo deve essere un numero decimale");
					return;
				}
			}
		});
	}
	@FXML
	private void selezioneDati() {
		GestioneManualeController controller = new GestioneManualeController(Db2DAOFactoryCredito.getDAOFactoryCredito(DAOFactoryCredito.DB2));

		Crediti crediti = controller.visualizzaCrediti();
		ObservableList<Cliente> olClienti = FXCollections.observableArrayList();
		ObservableList<Credito> olCrediti = FXCollections.observableArrayList();
		for(Credito credito : crediti) {
			olClienti.add(credito.getCliente());
			olCrediti.add(credito);
		}
		tableClienti.setItems(olClienti);
		tableCrediti.setItems(olCrediti);
		
		Bonifici bonifici = controller.visualizzaTuttiBonifici();
		ObservableList<Bonifico> olBonifici = FXCollections.observableArrayList();
		for(Bonifico bonifico : bonifici) {
			olBonifici.add(bonifico);
		}
		tableBonifici.setItems(olBonifici);
	}
	@FXML
	private void selezioneParametri() {
		iPercMulta.setText("");
		iRitardoCredito.setText("");
		iRitardoBlacklist.setText("");
		iFormatoCausale.setText("");
	}
	@FXML
	private void selezioneGestioneOperatori() {
		iNomeOperatore.setText("");
		iCognomeOperatore.setText("");
		iTelefonoOperatore.setText("");
		iUsrOperatore.setText("");
		iPwdOperatore.setText("");
		iConfPwdOp.setText("");
		
		selEliminaOp.setText("");
		selSbloccaOp.setText("");
	}
	@FXML
	private void selezioneLog() {
		LogController controller = new LogController();
		
		try{ 
			Log entries = controller.getLog();
			
			ObservableList<Entry> olLog = FXCollections.observableArrayList();
			for(Entry entry : entries) {
				olLog.add(entry);
			}
			
			log.setItems(olLog);
		} catch(Exception e) {
			LogController controllerLog = new LogController();
			String[] operazione = {LocalDateTime.now().toString(), " - Creazione nuovo file di log - ", "Sistema"};
			controllerLog.printLogOperazione(operazione);

			Log entries = controller.getLog();
			
			ObservableList<Entry> olLog = FXCollections.observableArrayList();
			for(Entry entry : entries) {
				olLog.add(entry);
			}
			
			log.setItems(olLog);
		}
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
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Algoritmo di hashing errato", "Contattare l'amministratore");
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
		
		try {
			ArrayList<Entry> logFiltrato = controller.getEntry(inizio, fine);
			
			ObservableList<Entry> olLog = FXCollections.observableArrayList();
			for(Entry entry : logFiltrato) {
				olLog.add(entry);
			}
			
			log.setItems(olLog);
		} catch(Exception e) {
			MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Log mancante", "Riprovare");
			
		}
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
