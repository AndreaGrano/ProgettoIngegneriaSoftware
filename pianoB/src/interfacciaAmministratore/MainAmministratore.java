package interfacciaAmministratore;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;


public class MainAmministratore extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader=new FXMLLoader(this.getClass().getResource("/interfacciaAmministratore/homeAmministratore.fxml"));
			StackPane root = (StackPane) loader.load();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("Grafica.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Home Amministratore");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
