package interfacciaAutenticazione;

import java.io.IOException;

import interfacciaAmministratore.MsgDialog;
import javafx.fxml.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginDialog extends Dialog<String[]> {
	@FXML
	private TextField iUsername;
	@FXML
	private PasswordField iPassword;
	@FXML
	private ButtonType btAutentica;
	public LoginDialog()
	{
		FXMLLoader loader=new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/interfacciaAutenticazione/ViewAutenticazione.fxml"));
		loader.setController(this);
		try {
			DialogPane dialogPane=loader.load();
			setResizable(true);
			setTitle("Autenticazione");
			setDialogPane(dialogPane);
			setResultConverter(btAutentica -> {
				String result[]=null;
				if(btAutentica!=null)
				{
					result=new String[2];
					final int USERNAME=0, PASSWORD=1;
					result[USERNAME]=iUsername.getText();
					result[PASSWORD]=iPassword.getText();
				}
				return result;
			});
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			MsgDialog.showAndWait(AlertType.ERROR,"", "Errore apertura finestra autenticazione", e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
