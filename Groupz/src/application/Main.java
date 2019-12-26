package application;
	
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import java.io.IOException;

import beans.Groupz;

public class Main extends Application {
	
	private static Parent main;
	
	@Override
	public void start(Stage primaryStage) {
		Platform.setImplicitExit(false);
		try {
			Groupz.init();
			Groupz.login("kareemhacker123321@gmail.com", "crxdharvvnozdzjw");

		} catch(IOException e) {
			Alert err = new Alert(Alert.AlertType.ERROR);
			err.setTitle("Groupz");
			err.setHeaderText("An error ouccred");
			err.setContentText("The application failed to read some files\n" +
			"It is possible that the data got courrpted");
			err.show();
			
		}
		
		try {
			Parent loginRoot = FXMLLoader.load(getClass().getResource("assets/Login.fxml"));
			Scene loginScene = new Scene(loginRoot);
			Stage dig = new Stage();
			dig.setTitle("Groupz");
			dig.initModality(Modality.APPLICATION_MODAL);
			dig.setScene(loginScene);
//			dig.showAndWait();
			
			main = FXMLLoader.load(getClass().getResource("assets/Main.fxml"));
			Scene scene = new Scene(main);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Groupz");
			primaryStage.show();
			primaryStage.setOnCloseRequest(event -> {
				try {
					Groupz.save();
					Platform.exit();
					
				} catch (IOException e) {
					event.consume();
					Alert err = new Alert(Alert.AlertType.ERROR);
					err.setTitle("Groupz");
					err.setHeaderText("An error ouccred");
					err.setContentText("The application failed to save the data");
					err.show();
					
				}
				
			});
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
		
	}
}
