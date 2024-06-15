package interfacciaAmministratore;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import dominioBonifico.Bonifico;
import dominioCredito.Cliente;
import dominioCredito.Credito;
import dominioLog.Entry;
import dominioOperatore.Operatore;
import interfacciaAutenticazione.LoginDialog;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;

public class HomeAmministratore implements Initializable {
	@FXML
	private TextField iPercMulta, iRitardoCredito, iRitardoBlacklist, iFormatoCausale;
	@FXML
	private TextField iNomeOperatore, iCognomeOperatore, iTelefonoOperatore, iUsrOperatore, iPwdOperatore, iConfPwdOp;
	@FXML
	private ComboBox<Operatore> selEliminaOp, selSbloccaOp;
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
	}
	@FXML
	private void aggiornaDati(ActionEvent event)
	{
		
	}
	@FXML
	private void aggiornaMulta(ActionEvent event)
	{
		
	}
	@FXML
	private void aggiornaRitardoCredito(ActionEvent event)
	{
		
	}
	@FXML
	private void aggiornaRitardoBlacklist(ActionEvent event)
	{
		
	}
	@FXML
	private void aggiornaFormatoCausale(ActionEvent event)
	{
		
	}
	@FXML
	private void clickCreaOp(ActionEvent event)
	{
		
	}
	@FXML
	private void clickEliminaOp(ActionEvent event)
	{
		
	}
	@FXML
	private void clickSbloccaOp(ActionEvent event)
	{
		
	}
	@FXML
	private void filtraLog(ActionEvent event)
	{
		
	}
}
