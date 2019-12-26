package application;

import beans.Groupz;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

public class LoginController {
	
	@FXML
	private TextField emailField;
	
	@FXML
	private PasswordField passwordField;
	
	@FXML
	void onSubmit(ActionEvent event) {
		String email = emailField.getText();
		String password = passwordField.getText();
		
		Groupz.login(email, password);
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
		
	}

}
