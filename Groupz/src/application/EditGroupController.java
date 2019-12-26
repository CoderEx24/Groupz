package application;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import beans.Group;
import beans.Groupz;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditGroupController {
	
	private String theGroup;
	private ObservableList<String> members;
	private ObservableList<String> tasks;

    @FXML
    private TextField groupNameField;

    @FXML
    private ListView<String> membersList;

    @FXML
    private ListView<String> tasksList;

    @FXML
    private Button addButton;

//    @FXML
//    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button saveButton;
    
    @FXML
    void initialize() {
    	members = FXCollections.observableArrayList();
    	tasks = FXCollections.observableArrayList();
    	
    	membersList.setItems(members);
    	tasksList.setItems(tasks);
    	
    }

    @FXML
    void onAdd(ActionEvent event) {
    	ChoiceDialog<String> choiceDig = new ChoiceDialog<>("Student", "Student", "Task");
    	choiceDig.setTitle("Groupz");
    	choiceDig.setHeaderText("Choose between student and group");
    	Optional<String> choice = choiceDig.showAndWait();
    	choice.ifPresent(val -> {
    		if (val.equals("Student")) {
    			ChoiceDialog<String> studentChoiceDialog = new ChoiceDialog<>("", Groupz.getStudentsOfClass(Groupz.getCurrentClass()));
    			studentChoiceDialog.setTitle("Groupz");
    			studentChoiceDialog.setHeaderText("Choose a student to add");
    			Optional<String> student = studentChoiceDialog.showAndWait();
    			student.ifPresent(val2 -> Groupz.addStudentToGroup(val2, theGroup));
    			members.clear();
    			members.addAll(Groupz.getStudentsOfGroup(theGroup));
    			
    		} else if (val.equals("Task")) {
    			try {
    				FXMLLoader loader = new FXMLLoader(getClass().getResource("assets/AddTask.fxml"));
    				Parent root = loader.load();
    				AddTaskController controller = loader.getController();
    				controller.disableStudentChoice();
    				controller.setRadioButtons(null, theGroup);
    				Scene scene = new Scene(root);
    				Stage dig = new Stage();
    				dig.setTitle("Groupz - Add a task");
    				dig.initModality(Modality.APPLICATION_MODAL);
    				dig.setScene(scene);
    				dig.show();
    				
    			} catch (Exception e) {
    				e.printStackTrace();
    			
    			}
    		}
    		
    	});
    	
    }

    @FXML
    void onDelete(ActionEvent event) {
    	String selectedStudent = membersList.getSelectionModel().getSelectedItem();
    	String selectedTask = tasksList.getSelectionModel().getSelectedItem();
    	
    	if (selectedStudent != null && selectedTask != null) {
    		membersList.getSelectionModel().clearSelection();
    		tasksList.getSelectionModel().clearSelection();
    
    	} else if (selectedStudent != null) {
    		List<String> otherGroups = Groupz.getGroupsOfClass(Groupz.getCurrentClass());
    		otherGroups.remove(theGroup);
    		if (otherGroups.size() == 0) {
    			
    			return;
    		}
    		ChoiceDialog<String> otherGroupsDialog = new ChoiceDialog<>("", otherGroups);
    		otherGroupsDialog.setTitle("Groupz");
    		otherGroupsDialog.setHeaderText("Select a group to move " + selectedStudent + " to.");
    		Optional<String> result = otherGroupsDialog.showAndWait();
    		result.ifPresent(val -> Groupz.addStudentToGroup(selectedStudent, val));
    		
    	} else if (selectedTask != null)
    		Groupz.deleteGroupTask(theGroup, selectedTask);
    	
    }

//    @FXML
//    void onEdit(ActionEvent event) {
//    	String selectedStudent = membersList.getSelectionModel().getSelectedItem();
//    	String selectedTask = tasksList.getSelectionModel().getSelectedItem();
//    	
//    	if (selectedStudent != null && selectedTask != null) {
//    		membersList.getSelectionModel().clearSelection();
//    		tasksList.getSelectionModel().clearSelection();
//    
//    	} else if (selectedStudent != null) {
//    	} else if (selectedTask != null)
//    }
//
    public void setGroup(String g) {
    	theGroup = g;
    	members.setAll(Groupz.getStudentsOfGroup(theGroup));
    	tasks.setAll(Groupz.getTasksOfGroup(theGroup));
    	
    	
    }

}
