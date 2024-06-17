package interfacciaOperatore;

import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

//import com.google.gson.*;

import dominioBonifico.Bonifico;
import dominioBonifico.BonificoNonRiconciliato;
import dominioCredito.Credito;
import dominioCredito.CreditoNonRiconciliato;
import dominioCredito.CreditoScaduto;
import interfacciaAutenticazione.LoginDialog;
import javafx.fxml.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.collections.ObservableList;
import javafx.event.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpResponse.ResponseInfo;
import java.rmi.ConnectException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javafx.scene.control.TextArea;
import java.io.*;
public class HomeOperatore implements Initializable {
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
	private HttpClient httpClient=HttpClient.newHttpClient();
	private static final int USERNAME=0, PASSWORD=1;
	private String[] credenziali;
	@FXML
	private void eseguiInserimentoCredito(ActionEvent event)
	{
//		HttpRequest.Builder reqBuilder=HttpRequest.newBuilder(URI.create("http://localhost:8080/ServerCreditoManuale/RegistrazioneCreditoServlet"));
//		JsonObject jsonData=new JsonObject();
//		jsonData.addProperty("nome",iNome.getText());
//		jsonData.addProperty("cognome", iCognome.getText());
//		jsonData.addProperty("telefono", iTelefono.getText());
//		jsonData.addProperty("indirizzo", iIndirizzo.getText());
//		DateTimeFormatter formatter=DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale.ITALIAN);
//		jsonData.addProperty("dataNascita", iDataNascita.getValue().format(formatter));
//		jsonData.addProperty("CF", iCodFisc.getText());
//		jsonData.addProperty("causale", iCausale.getText());
//		jsonData.addProperty("importo", iImporto.getText());
//		jsonData.addProperty("dataStipula", iDataStipula.getValue().format(formatter));
//		System.out.println(jsonData.toString());
//		BodyPublisher bodyPub=BodyPublishers.ofString(jsonData.toString());
//		HttpRequest req = reqBuilder.POST(bodyPub).build();
//		httpClient.sendAsync(req, BodyHandlers.ofString());
	}
	@FXML
	private void eseguiRiconciliazioneManuale(ActionEvent event)
	{
//		BonificoNonRiconciliato bonifico=listBonificiNonRiconciliati.getSelectionModel().getSelectedItem();
//		ObservableList<CreditoNonRiconciliato> crediti=listCreditiNonRiconciliati.getSelectionModel().getSelectedItems();
//		Gson g=new Gson();
//		HttpRequest.Builder reqBuilder=HttpRequest.newBuilder(URI.create("http://localhost:8080/ServerCreditoManuale/RiconciliazioneManualeServlet"));
//		String jsonBonifico=g.toJson(bonifico);
//		String jsonListaCrediti=g.toJson(crediti);
//		BodyPublisher bodyPub=BodyPublishers.ofString(jsonBonifico+"\n"+jsonListaCrediti);
//		HttpRequest req=reqBuilder.POST(bodyPub).build();
//		httpClient.sendAsync(req, BodyHandlers.ofString());
	}
	private void scriviFile(Collection<? extends Object> lista, String nomefile)
	{
		PrintWriter pw=null;
		try {
			pw=new PrintWriter(nomefile);
			for(Object element : lista)
			{
				pw.println(element);
			}
		}
		catch(IOException ioe)
		{
			MsgDialog.showAndWait(AlertType.ERROR, "Errore scrittura file", "Impossibile scrivere sul file"+nomefile, ioe.getMessage());
		}
		finally
		{
			if(pw!=null)
				pw.close();
		}
	}
	@FXML
	private void eseguiScaricamentoBonifici(ActionEvent event)
	{
		scriviFile(listTuttiBonifici.getItems(), "bonifici.txt");
	}
	@FXML
	private void eseguiScaricamentoCrediti(ActionEvent event)
	{
		scriviFile(listTuttiCrediti.getItems(), "crediti.txt");
	}
	@FXML
	private void eseguiScaricamentoCreditiScaduti(ActionEvent event)
	{
		scriviFile(listCreditiScaduti.getItems(), "crediti_scaduti.txt");
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
		
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
//		credenziali=new String[2];
//		LoginDialog login=new LoginDialog();
//		Optional<String[]> credenzialiIn=login.showAndWait();
//		if(credenzialiIn.isEmpty())
//			System.exit(0);
//		else
//			credenziali=credenzialiIn.get();
//		try {
//			BodyHandler<String> respHandler=BodyHandlers.ofString();
//			HttpRequest builtReq=HttpRequest.newBuilder()
//					  .uri(URI.create("http://localhost:8080/ServerAutenticazioneGestioneOperatori/RetrieveSessionServlet"))
//					  .build();
//			HttpResponse<String> resp=httpClient.send(builtReq, respHandler);
//			if(!Boolean.valueOf(resp.body()))
//			{
//				builtReq=HttpRequest.newBuilder()
//						  .uri(URI.create("http://localhost:8080/ServerAutenticazioneGestioneOperatori/LoginServlet"))
//						  .header("Content-Type", "application/x-www-form-urlencoded")
//						  .POST(HttpRequest.BodyPublishers.ofString("username="+credenziali[USERNAME]+"&password="+credenziali[PASSWORD]))
//						  .build();
//				resp=httpClient.send(builtReq, respHandler);
//				if(resp.body().contains("allowed"))
//				{
//					MsgDialog.showAndWait(AlertType.WARNING, "Errore di autenticazione", "Credenziali errate", "");
//					System.exit(0);
//				}
//			}
//		}catch (InterruptedException | IOException e) {
//			MsgDialog.showAndWait(AlertType.ERROR, "Errore di autenticazione", "Autenticazione interrotta a causa dell'errore seguente:", e.getClass().getName()+": "+e.getMessage());
//			e.printStackTrace();
//			System.exit(-1);
//		}
//		
	}
}
