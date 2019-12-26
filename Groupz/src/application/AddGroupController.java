package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import beans.Groupz;

public class AddGroupController {

    @FXML
    private Button submitButton;

    @FXML
    private ComboBox<String> classField;

    @FXML
    private TextField groupNameField;

    @FXML
    void onSubmit(ActionEvent event) {
    	String groupName = groupNameField.getText();
    	String className = classField.getSelectionModel().getSelectedItem();
    	Groupz.createGroup(groupName, className);
    	((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    	
    }
    
    @FXML
    void initialize() {
    	classField.setItems(FXCollections.observableArrayList(Groupz.getClasses()));
    	
    }

}
