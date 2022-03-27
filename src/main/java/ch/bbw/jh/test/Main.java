package ch.bbw.jh.test;
/**
 * @Autor Jorin Heer
 * @Version 16.09.2020
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
/**
 * Palindrom
 * @author Jorin Heer
 * @version 2020
 */

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			//Model
			Model myModel = new Model();

			//View
			FXMLLoader myLoader = new FXMLLoader(getClass().getResource("View.fxml"));
			//anpassen
			VBox root = myLoader.load();

			//Control
			Controller controller = (Controller) myLoader.getController();
			controller.setModel(myModel);

			Scene scene = new Scene(root,1000,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Test");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.out.println("JavaFX " + System.getProperty("javafx.version") + ", running on Java " + System.getProperty("java.version") + ".");
		launch(args);
	}
}
