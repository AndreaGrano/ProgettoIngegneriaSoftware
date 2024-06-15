package interfacciaOperatore;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;


public class MainOperatore extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader=new FXMLLoader(this.getClass().getResource("/interfacciaOperatore/homeOperatore.fxml"));
			StackPane root = (StackPane) loader.load();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("Grafica.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Home Operatore");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
