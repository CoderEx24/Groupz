package application;

import beans.Groupz;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditStudentController {

	private String theStudent;
	
    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;
    
    @FXML
    private ListView<String> tasksList;
    
    @FXML
    private ComboBox<String> classField;

    @FXML
    private ComboBox<String> groupField;

    @FXML
    void onSave(ActionEvent event) {
    	String student = theStudent.split("-")[0];
    	String newName = nameField.getText();
    	String newEmail = emailField.getText();
    	String newGroup = groupField.getSelectionModel().getSelectedItem();
    	String newClass = classField.getSelectionModel().getSelectedItem();
    	Groupz.editStudent(student, newName, newGroup, newClass, newEmail);
    	((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    	
    }
    
    @FXML
    void onClassChange(ActionEvent evt) {
    	groupField.setItems(FXCollections.observableArrayList(Groupz.getGroupsOfClass(classField.getSelectionModel().getSelectedItem())));
    	
    }
    
    @FXML
    void initialize() {
    	classField.setItems(Groupz.getClasses());
    	
    }
    
    public void setTheStudent(String s) {
    	theStudent = s;
    	String[] tokens = theStudent.split("-");
    	String studentClass = Groupz.getCurrentClass();
    	tasksList.setItems(FXCollections.observableArrayList(Groupz.getTasksOfStudent(tokens[0])));
    	nameField.setText(tokens[0]);
    	groupField.setItems(FXCollections.observableArrayList(Groupz.getGroupsOfClass(studentClass)));
    	groupField.getSelectionModel().select(tokens[1]);
    	classField.getSelectionModel().select(studentClass);
    	emailField.setText(tokens[3]);

    }

}
