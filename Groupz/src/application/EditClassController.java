package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

import java.util.LinkedList;
import java.util.List;

import beans.Class;
import beans.Group;
import beans.Groupz;
import beans.Student;

public class EditClassController {
	
	private String theClass;
	private ObservableList<String> students;
	private ObservableList<String> groups;
	
    @FXML
    private TextField classNameField;

    @FXML
    private ListView<String> studentsList;

    @FXML
    private ListView<String> groupsList;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;
    
    @FXML
    void initialize() {
    	students = FXCollections.observableArrayList();
    	groups = FXCollections.observableArrayList();
    	studentsList.setItems(students);
    	groupsList.setItems(groups);
    	
    }

    @FXML
    void onAdd(ActionEvent event) {
    
    }

    @FXML
    void onDelete(ActionEvent event) {
    	
    	
    }

    @FXML
    void onEdit(ActionEvent event) {
    }

    public void setClass(String s) {
    	theClass = s;
    	students.addAll(Groupz.getStudentsOfClass(theClass));
    	groups.addAll(Groupz.getGroupsOfClass(theClass));
    	
    }
}
