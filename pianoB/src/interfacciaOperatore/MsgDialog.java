package interfacciaOperatore;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MsgDialog {
	public static void showAndWait(AlertType type, String title, String header, String body)
	{
		Alert alert=new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(body);
		alert.showAndWait();
	}
}
