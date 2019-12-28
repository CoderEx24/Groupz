package application;

// ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.transformation.FilteredList;
import beans.Groupz;


public class MainController {
	
	private ObservableList<String> students;
	private FilteredList<String> filteredStudentsList;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addStudentButton;

    @FXML
    private Button addGroupButton;

    @FXML
    private Button addClassButton;

    @FXML
    private Button addTaskButton;

    @FXML
    private Button gradeTaskButton;
    
    @FXML
    private TextField searchQueryField;

    @FXML
    private ComboBox<String> classesShowComboBox;
    
    @FXML
    private TableColumn<String, String> studentNameColumn;
    
    @FXML
    private TableColumn<String, String> studentGroupColumn;
    
    @FXML
    private TableColumn<String, String> taskColumn;
    
    @FXML
    private TableView<String> mainTable;
    
    @FXML
    void initialize() {
    	students = FXCollections.observableArrayList();
    	filteredStudentsList = new FilteredList<String>(students);
    	Groupz.setStudentsTableList(students);
    	mainTable.setItems(filteredStudentsList);
    	
    	studentNameColumn.setCellValueFactory(obj -> {
    		String studentString = (String) obj.getValue();
    		return new SimpleStringProperty(studentString.split("-")[0]);
    		
    	});
    	
    	studentGroupColumn.setCellValueFactory(obj -> {
    		String studentString = (String) obj.getValue();
    		return new SimpleStringProperty(studentString.split("-")[1]);
    		
    	});
    	
    	taskColumn.setCellValueFactory(obj -> {
    		String studentString = (String) obj.getValue();
    		return new SimpleStringProperty(Groupz.getStudentCurrentTaskName(studentString.split("-")[0]));
    		
    	});
    	
    	classesShowComboBox.setItems(Groupz.getClasses());
    	if (classesShowComboBox.getItems().size() > 0) {
    		classesShowComboBox.getSelectionModel().select(0);
    		onClassChange(null);
    		
    	}
    	
    }
    
    @FXML
    void onClassChange(ActionEvent evt) {
    	Groupz.setCurrentClass(classesShowComboBox.getSelectionModel().getSelectedItem());
    	
    }
    
    @FXML
    void onSearch(KeyEvent evt) {
    	for (int i = 0; i < 1; i++)  {
    		final String query = searchQueryField.getText();
    		if (query.isEmpty())
    			filteredStudentsList.setPredicate(null);
    		else
    			filteredStudentsList.setPredicate(student -> {
    				String[] tokens = student.split("-");
    				String name = tokens[0];
    				String groupName = tokens[1];
    				String email = tokens[3];
    				return name.contains(query) || groupName.contains(query) || email.contains(query);
    				
    			});
    	
    	}
    }
    
    @FXML
    void addStudent(ActionEvent evt) {
    	try {
			Parent root = FXMLLoader.load(getClass().getResource("assets/AddStudent.fxml"));
			Scene scene = new Scene(root);
			Stage dialog = new Stage();
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.setTitle("Groupz - Add a student");
			dialog.setScene(scene);
			dialog.showAndWait();
			mainTable.refresh();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
    
    @FXML
    void addGroup(ActionEvent evt) {
    	try {
    		Parent root = FXMLLoader.load(getClass().getResource("assets/AddGroup.fxml"));
    		Scene scene = new Scene(root);
    		Stage dig = new Stage();
    		dig.setScene(scene);
    		dig.initModality(Modality.APPLICATION_MODAL);
    		dig.setTitle("Groupz - add a group");
    		dig.show();
    		mainTable.refresh();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    @FXML 
    void addClass(ActionEvent evt) {
    	try {
    		TextInputDialog classDialog = new TextInputDialog();
    		classDialog.setTitle("Groupz");
    		classDialog.setHeaderText("Add a Class");
    		classDialog.setContentText("Enter the name of the class");
    		String className = classDialog.showAndWait().orElse("");
    		if (!className.isEmpty())
    			Groupz.createClass(className);
    		mainTable.refresh();
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		
    	}
    	
    }
    
    @FXML
    void addTask(ActionEvent evt) {
    	try {
    		String selectedStudent = mainTable.getSelectionModel().getSelectedItem();
    		String studentName = selectedStudent.split("-")[0];
    		String groupName = selectedStudent.split("-")[1];
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("assets/AddTask.fxml"));
    		Parent root = loader.load();
    		AddTaskController controller = loader.getController();
    		controller.setRadioButtons(studentName, groupName);
    		Scene scene = new Scene(root);
    		Stage dig = new Stage();
    		dig.initModality(Modality.APPLICATION_MODAL);
    		dig.setScene(scene);
    		dig.setTitle("Groupz - add a task");
    		dig.show();
    		mainTable.refresh();

    	} catch (Exception e) {
    		e.printStackTrace();
    		
    	}
    	
    }
    
    @FXML
    void onGradeTask(ActionEvent evt) {
    	String selected = mainTable.getSelectionModel().getSelectedItem();
    	Optional.<String>ofNullable(selected).ifPresent(val -> {
    		final String name = val.split("-")[0];
    		final String groupName = val.split("-")[1];
    		
    		ChoiceDialog<String> whichChoice = new ChoiceDialog<String>(name, name, groupName);
    		whichChoice.setTitle("Groupz");
    		whichChoice.setHeaderText("Choose which one you will grade his current task");
    		whichChoice.showAndWait().ifPresent(choice -> {
    			ChoiceDialog<String> gradeDialog = new ChoiceDialog<String>("", "blue", "green", "yellow", "red");
    			gradeDialog.setTitle("Groupz");
    			gradeDialog.setHeaderText("Choose the grade");
    			gradeDialog.showAndWait().ifPresent(grade -> {
    				if (choice.equals(name))
    					Groupz.gradeStudentCurrentTask(name, beans.Task.stringToGrade(grade));
    				else
    					Groupz.gradeGroupCurrentTask(groupName, beans.Task.stringToGrade(grade));
    				
    			});
    			
    		});
    		
    	});
    	mainTable.refresh();
    }
    
    @FXML
    void onStudentEdit() {
    	try {
    		String selectedStudent = mainTable.getSelectionModel().getSelectedItem();
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("assets/EditStudent.fxml"));
    		Parent root = loader.load();
    		EditStudentController controller = loader.getController();
    		controller.setTheStudent(selectedStudent);
    		Scene scene = new Scene(root);
    		Stage dig = new Stage();
    		dig.initModality(Modality.APPLICATION_MODAL);
    		dig.setTitle("Groupz - Edit a student");
    		dig.setScene(scene);
    		dig.show();
    		mainTable.refresh();    		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
    
    @FXML
    void onGroupEdit() {
    	try {
    		String selectedStudent = mainTable.getSelectionModel().getSelectedItem();
    		String selectedGroup = selectedStudent.split("-")[1];
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("assets/EditGroup.fxml"));
    		Parent root = loader.load();
    		EditGroupController controller = loader.getController();
    		controller.setGroup(selectedGroup);
    		Scene scene = new Scene(root);
    		Stage dig = new Stage();
    		dig.initModality(Modality.APPLICATION_MODAL);
    		dig.setTitle("Groupz - Edit a group");
    		dig.setScene(scene);
    		dig.show();
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		
    	}
    	
    }

    @FXML
    void onStudentDelete() {
    	String selectedStudent = mainTable.getSelectionModel().getSelectedItem();
    	if (selectedStudent == null)
    		return;
    	String selectedStudentName = selectedStudent.split("-")[0];
    	Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
    	confirm.setTitle("Groupz");
    	confirm.setHeaderText("Delete a student");
    	confirm.setContentText("Are you sure that you want to delete " + selectedStudentName + "?");
    	Optional<ButtonType> result = confirm.showAndWait();
    	result.ifPresent(val -> { 
    		if (val.equals(ButtonType.OK))
    			Groupz.deleteStudent(selectedStudentName);
    	});
    }
    
    @FXML
    void onGroupDelete() {
    	ChoiceDialog<String> groupsDialog = new ChoiceDialog<>("", Groupz.getGroupsOfClass(Groupz.getCurrentClass()));
    	groupsDialog.setTitle("Groupz");
    	groupsDialog.setHeaderText("Select a group to remove");
    	Optional<String> result = groupsDialog.showAndWait();
    	result.ifPresent(val -> {
    		String selectedGroupName = val;
        	Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        	confirm.setTitle("Groupz");
        	confirm.setHeaderText("Delete a group");
        	confirm.setContentText("Are you sure that you want to delete " + selectedGroupName + "?");
        	ButtonType deleteResult = confirm.showAndWait().orElse(new ButtonType(""));
        	confirm.setHeaderText("Delete the group members");
        	confirm.setContentText("Do you also want to delete all the members of the group?");
        	ButtonType deleteStudentResult = confirm.showAndWait().orElse(null);
        	if (deleteResult.equals(ButtonType.OK))
        		Groupz.deleteGroup(selectedGroupName, deleteStudentResult.equals(ButtonType.OK));

    		
    	});
    	mainTable.refresh();
    	
    }
    
    @FXML
    void onClassDelete() {
    	Alert confirm = new Alert(AlertType.CONFIRMATION);
    	confirm.setTitle("Groupz");
    	confirm.setHeaderText("Delete a class");
    	confirm.setContentText("Are you sure that you want to delete class " + Groupz.getCurrentClass() + " entirely?");
    	ButtonType result = confirm.showAndWait().orElse(null);
    	if (result.equals(ButtonType.OK)) {
    		Groupz.deleteClass(Groupz.getCurrentClass());
    		classesShowComboBox.getSelectionModel().select(0);
    	
    	}
    }
    
}
