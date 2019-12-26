package beans;

import java.util.HashMap;
import java.util.Optional;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Groupz {
	
	private static File dataFolder;
	private static File dataFile;
	private static HashMap<String, Student> students;
	private static HashMap<String, Group> groups;
	private static HashMap<String, Class> classes;
	private static Optional<Class> currentClass;
	private static ObservableList<String> studentsTableList;
	
	@SuppressWarnings("unchecked")
	public static boolean init() throws IOException, ParseException {
		dataFolder = new File(System.getenv("APPDATA") + "/Groupz");
		dataFile = new File(dataFolder.getAbsolutePath() + "/data.json");
		students = new HashMap<String, Student>();
		groups = new HashMap<String, Group>();
		classes = new HashMap<String, Class>();
		studentsTableList = null;
		currentClass = Optional.empty();
		
		if (!dataFolder.exists()) {
			dataFolder.mkdir();
			dataFile.createNewFile();
			return true;
			
		} else if (!dataFile.exists()) {
			dataFile.createNewFile();
			return true;
			
		} else {
			FileReader reader = new FileReader(dataFile);
			JSONParser parser = new JSONParser();
			try {
				JSONObject object = (JSONObject) parser.parse(reader);
				JSONArray classesJsonArray = (JSONArray) object.get("classes");
				classesJsonArray.forEach(classJson -> {
					JSONObject classJsonObject = (JSONObject) classJson;
					String className = (String) classJsonObject.get("name");
					JSONArray groupsJsonArray = (JSONArray) classJsonObject.get("groups");
					createClass(className);
					groupsJsonArray.forEach(groupJson -> {
						
						
					});
					
					
				});
				
			} catch (IOException e) {
				e.printStackTrace();
				
			} catch (ParseException e) {
				e.printStackTrace();
				throw e;
				
			}
			
		}
		
		return true;
	}
	
	public static void setCurrentClass(String className) {
		Class theClass = classes.getOrDefault(className, null);
		currentClass = Optional.ofNullable(theClass);
		
	}
	
	public static void setStudentsTableList(ObservableList<String> list) {
		studentsTableList = list;
		
	}
	
	public static boolean createStudent(String studentName, String groupName, String className, String email) {
		if (students.containsKey(studentName))
			return false;
		
		Student newStudent = new Student(studentName, groupName, className, email);
		classes.get(className).addStudent(newStudent);
		groups.get(groupName).addMember(newStudent);
		students.put(studentName, newStudent);
		if (currentClass.isPresent())
			studentsTableList.add("" + newStudent);		
		return true;
		
	}
	
	public static boolean createGroup(String groupName, String className) {
		if (groups.containsKey(groupName))
			return false;
		
		Group newGroup = new Group(groupName, className);
		classes.get(className).addGroup(newGroup, false);
		groups.put(groupName, newGroup);
		return true;
		
	}
	
	public static boolean createClass(String className) {
		if (classes.containsKey(className))
			return false;
		
		Class newClass = new Class(className);
		classes.put(className, newClass);
		return true;
		
	}
	
	public static void createTaskForStudent(String studentName, String taskName, String taskDescription, boolean isCurrent, boolean mailToStudent) {
		Student theStudent = students.get(studentName);
		Task newTask = new Task(taskName, taskDescription, isCurrent);
		theStudent.addTask(newTask);
		
	}
	
	public static void createTaskForGroup(String groupName, String taskName, String taskDescription, boolean isCurrent, boolean mailToStudent) {
		Group theGroup = groups.get(groupName);
		Task newTask = new Task(taskName, taskDescription, isCurrent);
		theGroup.addTask(newTask);
		
	}
	
	public static void deleteStudent(String studentName) {
		Student removedStudent = students.get(studentName);
		classes.get(removedStudent.getClassName()).removeStudent(removedStudent);
		groups.get(removedStudent.getGroupName()).removeMember(removedStudent);
		students.remove(studentName);
		
	}
	
	public static void deleteGroup(String groupName, boolean withStudents) {
		Group removedGroup = groups.get(groupName);
		if (withStudents)
			removedGroup.getMembers().forEach((studentName, ignore) -> deleteStudent(studentName));
		classes.get(removedGroup.getClassName()).removeGroup(removedGroup, withStudents);
		groups.remove(groupName);
		
	}
	
	public static void deleteClass(String className) {
		Class removedClass = classes.get(className);
		removedClass.getGroups().forEach((groupName, ignore) -> deleteGroup(groupName, true));
		classes.remove(className);
		
	}

}
