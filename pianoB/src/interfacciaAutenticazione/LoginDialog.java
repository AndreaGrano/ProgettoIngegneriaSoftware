package interfacciaAutenticazione;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import autenticazione.AutenticazioneController;
import daoOperatori.DAOFactoryOperatori;
import daoOperatoriDB2.Db2DAOFactoryOperatori;
import gestioneOperatori.GestioneOperatoriController;
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
					final int USERNAME=0, PASSWORD=1, HASH=1;
					HashMap<String,Integer> errori = new HashMap<String,Integer>();
					MessageDigest digest = null;
					try {
						digest = MessageDigest.getInstance("SHA3-256");
					} catch (NoSuchAlgorithmException e) {
						MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Algoritmo di hashing errato", "Contattare l'amministratore");
					}
					
					while(true) {
						result[USERNAME]=iUsername.getText();
						result[PASSWORD]=iPassword.getText();					
						result[HASH] = bytesToHex(digest.digest(result[PASSWORD].getBytes()));
						
						AutenticazioneController controllerAutenticazione = new AutenticazioneController(Db2DAOFactoryOperatori.getDAOFactoryOperatori(DAOFactoryOperatori.DB2));
						try {
							if(!controllerAutenticazione.verificaCredenziali(result[USERNAME], result[HASH])) {
								errori.merge(result[USERNAME], 1, Integer::sum);
								
								if(errori.get(result[USERNAME]) == 3) {
									GestioneOperatoriController controllerOperatori = new GestioneOperatoriController(Db2DAOFactoryOperatori.getDAOFactoryOperatori(DAOFactoryOperatori.DB2));
									controllerOperatori.bloccaOperatore(result[USERNAME]);
									
									MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Operatore bloccato", "L'operatore " + result[USERNAME] + " è stato bloccato");
								}
								
							} else {
								break;
							}
						} catch(IllegalArgumentException e) {
							MsgDialog.showAndWait(AlertType.ERROR, "Errore", "Operatore inesistente", "L'operatore " + result[USERNAME] + " è inesistente");
						}
					}
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
}
