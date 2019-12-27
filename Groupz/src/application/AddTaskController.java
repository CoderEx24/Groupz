package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import beans.Groupz;

public class AddTaskController {

    @FXML
    private Button submitButton;

    @FXML
    private TextField nameField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private CheckBox isCurrentField;

    @FXML
    private RadioButton studentRadioButton;

    @FXML
    private RadioButton groupRadioButton;
    
    @FXML
    void onSubmit(ActionEvent event) {
    	String choice = (String) studentRadioButton.getToggleGroup().getSelectedToggle().getUserData();
    	String taskName = nameField.getText();
    	String taskDescription = descriptionField.getText();
    	boolean isCurrent = isCurrentField.isSelected();
    	Platform.runLater(() -> {
    		if (choice.equals("student"))
        		Groupz.createTaskForStudent(studentRadioButton.getText(), taskName, taskDescription, beans.Task.Grade.UNRATED, isCurrent, true);
        	else
        		Groupz.createTaskForGroup(groupRadioButton.getText(), taskName, taskDescription, beans.Task.Grade.UNRATED, isCurrent, true);
        	
    	});
    	
    	((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    	
    }
    
    @FXML
    void initialize() {
    	ToggleGroup group = new ToggleGroup();
    	studentRadioButton.setToggleGroup(group);
    	groupRadioButton.setToggleGroup(group);
    	
    }
    
    void setRadioButtons(String studentName, String groupName) {
    	studentRadioButton.setText(studentName);
    	studentRadioButton.setUserData("student");
    	
    	groupRadioButton.setText(groupName);
    	groupRadioButton.setUserData("group");
    	
    }
    
    public void setTask(String taskName) {
    	
    	
    }
    
    public void disableStudentChoice() {
    	groupRadioButton.setSelected(true);
    	studentRadioButton.setDisable(true);
    	
    }    

}
