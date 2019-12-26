package application;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;

import beans.*;
import beans.Class;

public class AddStudentController {

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<String> classField;

    @FXML
    private ComboBox<String> groupField;
    
    @FXML
    private TextField emailField;

    @FXML
    private Button submitButton;

    @FXML
    void onSubmit(ActionEvent event) {
    	String name = nameField.getText();
    	String groupName = groupField.getSelectionModel().getSelectedItem();
    	String className = classField.getSelectionModel().getSelectedItem();
    	String email = emailField.getText();
    	Groupz.createStudent(name, groupName, className, email);
    	((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    	
    }
    
    @FXML
    void onClassChange(ActionEvent evt) {
    	groupField.setItems(FXCollections.observableArrayList(Groupz.getGroupsOfClass(classField.getSelectionModel().getSelectedItem())));
    	
    }
    
    @FXML
    void initialize() {
    	classField.setItems(FXCollections.observableArrayList(Groupz.getClasses()));
    	classField.getSelectionModel().select(0);
    	groupField.setItems(FXCollections.observableArrayList(Groupz.getGroupsOfClass(classField.getSelectionModel().getSelectedItem())));
    	
    }
    
}
