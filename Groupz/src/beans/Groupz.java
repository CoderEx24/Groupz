package beans;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import javax.mail.Transport;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Session;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;

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
	private static ObservableList<String> classesList;
	private static String email;
	private static String password;
	
	@SuppressWarnings("unchecked")
	public static boolean init() throws IOException, ParseException {
		dataFolder = new File(System.getenv("APPDATA") + "/Groupz");
		dataFile = new File(dataFolder.getAbsolutePath() + "/data.json");
		students = new HashMap<String, Student>();
		groups = new HashMap<String, Group>();
		classes = new HashMap<String, Class>();
		studentsTableList = null;
		classesList = FXCollections.observableArrayList();
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
						JSONObject groupJsonObject = (JSONObject) groupJson;
						String groupName = (String) groupJsonObject.get("name");
						JSONArray studentsJsonArray = (JSONArray) groupJsonObject.get("members");
						JSONArray tasksJsonArray = (JSONArray) groupJsonObject.get("tasks");
						createGroup(groupName, className);
						
						studentsJsonArray.forEach(studentJson -> {
							JSONObject studentJsonObject = (JSONObject) studentJson;
							String studentName = (String) studentJsonObject.get("name");
							String email = (String) studentJsonObject.get("email");
							JSONArray studentTasksJsonArray = (JSONArray) studentJsonObject.get("tasks");
							createStudent(studentName, groupName, className, email);
							
							studentTasksJsonArray.forEach(taskJson -> {
								JSONObject taskJsonObject = (JSONObject) taskJson;
								String taskName = (String) taskJsonObject.get("name");
								String taskDescription = (String) taskJsonObject.get("description");
								String taskGrade = (String) taskJsonObject.get("grade");
								boolean isCurrent = (boolean) taskJsonObject.get("is_current");
								createTaskForStudent(studentName, taskName, taskDescription, Task.stringToGrade(taskGrade), isCurrent, false);
								
							});
							
						});
						
						tasksJsonArray.forEach(taskJson -> {
							JSONObject taskJsonObject = (JSONObject) taskJson;
							String taskName = (String) taskJsonObject.get("name");
							String taskDescription = (String) taskJsonObject.get("description");
							String taskGrade = (String) taskJsonObject.get("grade");
							boolean isCurrent = (boolean) taskJsonObject.get("is_current");
							createTaskForGroup(groupName, taskName, taskDescription, Task.stringToGrade(taskGrade), isCurrent, false);
								
						});
						
					});
					
					
				});
				
			} catch (Exception e) {
				e.printStackTrace();
				dataFile.delete();
				dataFile.createNewFile();
				throw e;
				
			}
			
		}
		
		return true;
	}
	
	public static ObservableList<String> getClasses() {
		return classesList;
		
	}
	
	public static List<String> getGroupsOfClass(String className) {
		Class theClass = classes.get(className);
		return new LinkedList<String>(theClass.getGroups().keySet());
		
	}
	
	public static List<String> getStudentsOfClass(String className) {
		Class theClass = classes.get(className);
		return new LinkedList<String>(theClass.getStudents().keySet());
		
	}
	
	public static List<String> getMembersOfGroup(String groupName) {
		Group theGroup = groups.get(groupName);
		return new LinkedList<String>(theGroup.getMembers().keySet());
		
	}
	
	public static List<String> getTasksOfStudent(String studentName) {
		Student theStudent = students.get(studentName);
		return new LinkedList<String>(theStudent.getTasks().keySet());
		
	}
	
	public static List<String> getTasksOfGroup(String groupName) {
		Group theGroup = groups.get(groupName);
		return new LinkedList<String>(theGroup.getTasks().keySet());
		
	}
	
	public static void setCurrentClass(String className) {
		Class theClass = classes.get(className);
		currentClass = Optional.ofNullable(theClass);
		studentsTableList.clear();
		theClass.getStudents().values().forEach(student -> studentsTableList.add("" + student));
		
	}
	
	public static void setStudentsTableList(ObservableList<String> list) {
		studentsTableList = list;
		
	}
	
	public static void login(String e, String p) {
		email = p;
		password = p;
		
	}
	
	public static boolean createStudent(String studentName, String groupName, String className, String email) {
		if (students.containsKey(studentName))
			return false;
		
		Student newStudent = new Student(studentName, groupName, className, email);
		classes.get(className).addStudent(newStudent);
		groups.get(groupName).addMember(newStudent);
		students.put(studentName, newStudent);
		currentClass.ifPresent(val -> {
			System.out.println("Has a current class");
			if (val.getName().equals(className))
				studentsTableList.add("" + newStudent);
			
		});
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
		classesList.add(className);
		return true;
		
	}
	
	public static void createTaskForStudent(String studentName, String taskName, String taskDescription, Task.Grade taskGrade , boolean isCurrent, boolean mailToStudent) {
		Student theStudent = students.get(studentName);
		Task newTask = new Task(taskName, taskDescription, taskGrade, isCurrent);
		theStudent.addTask(newTask);
		if (mailToStudent)
			mailTaskToStudent(studentName, taskName);
		
	}
	
	public static void createTaskForGroup(String groupName, String taskName, String taskDescription, Task.Grade taskGrade ,boolean isCurrent, boolean mailToStudents) {
		Group theGroup = groups.get(groupName);
		Task newTask = new Task(taskName, taskDescription, taskGrade, isCurrent);
		theGroup.addTask(newTask);
		if (mailToStudents)
			mailTaskToGroup(groupName, taskName);
		
	}
	
	public static void mailTaskToStudent(String studentName, String taskName) {
		Student theStudent = students.get(studentName);
		Task theTask = theStudent.getTask(taskName);
		
		try {
			Properties props = System.getProperties();
			props.put("mail.smtp.auth", "true");
		    props.put("mail.smtp.starttls.enable", "true");
		    props.put("mail.smtp.host", "smtp.gmail.com");
		    props.put("mail.smtp.port", "587");
		    Session sess = Session.getInstance(props, new Authenticator() {
		    	protected PasswordAuthentication getPasswordAuthentication() {
		    		return new PasswordAuthentication(email, password);
		    		
		    	}
		    });
		    Message message = new MimeMessage(sess);
		    message.setFrom(new InternetAddress(email));
		    message.setRecipient(Message.RecipientType.TO, new InternetAddress(theStudent.getEmail()));
		    message.setSubject(theTask.getName() + " - A new task assigned to you");
		    message.setText(theTask.getDescirption());
		    Transport.send(message);
	
		} catch(MessagingException e) {
			e.printStackTrace();
			
		}
		
	}
	
	public static void mailTaskToGroup(String groupName, String taskName) {
		Group theGroup = groups.get(groupName);
		Task theTask = theGroup.getTask(taskName);
		
		theGroup.getMembers().entrySet().forEach(studentEntry -> {
			try {
				Properties props = System.getProperties();
				props.put("mail.smtp.auth", "true");
			    props.put("mail.smtp.starttls.enable", "true");
			    props.put("mail.smtp.host", "smtp.gmail.com");
			    props.put("mail.smtp.port", "587");
			    Session sess = Session.getInstance(props, new Authenticator() {
			    	protected PasswordAuthentication getPasswordAuthentication() {
			    		return new PasswordAuthentication(email, password);
			    		
			    	}
			    });
			    Message message = new MimeMessage(sess);
			    message.setFrom(new InternetAddress(email));
			    message.setRecipient(Message.RecipientType.TO, new InternetAddress(studentEntry.getValue().getEmail()));
			    message.setSubject(theTask.getName() + " - A new task assigned to you");
			    message.setText(theTask.getDescirption());
			    Transport.send(message);
		
			} catch(MessagingException e) {
				e.printStackTrace();
				
			}
			
		});
		
	}
	
	public static void addStudentToGroup(String studentName, String groupName) {
		Student theStudent = students.get(studentName);
		Group oldGroup = groups.get(theStudent.getGroupName());
		Group newGroup = groups.get(groupName);
		oldGroup.removeMember(theStudent);
		newGroup.addMember(theStudent);
		theStudent.setGroupName(groupName);
		
	}
	
	public static void editStudent(String studentName, String newName, String newGroupName, String newClassName, String newEmail) {
		Student theStudent = students.get(studentName);
		Group newGroup = groups.get(newGroupName);
		Class newClass = classes.get(newClassName);
		classes.get(theStudent.getClassName()).removeStudent(theStudent);
		groups.get(theStudent.getGroupName()).removeMember(theStudent);
		newGroup.addMember(theStudent);
		newClass.addStudent(theStudent);
		theStudent.setName(newName);
		theStudent.setGroupName(newGroupName);
		theStudent.setClassName(newClassName);
		theStudent.setEmail(newEmail);
		
	}
	
	public static void deleteStudent(String studentName) {
		Student removedStudent = students.get(studentName);
		classes.get(removedStudent.getClassName()).removeStudent(removedStudent);
		groups.get(removedStudent.getGroupName()).removeMember(removedStudent);
		students.remove(studentName);
		studentsTableList.remove("" + removedStudent);
		
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
	
	public static void deleteTaskFromStudent(String studentName, String taskName) {
		Student theStudent = students.get(studentName);
		theStudent.deleteTask(theStudent.getTask(taskName));
		
	}
	
	public static void deleteTaskFromGroup(String groupName, String taskName) {
		Group theGroup = groups.get(groupName);
		theGroup.removeTask(theGroup.getTask(taskName));
		
	}
	
	@SuppressWarnings("unchecked")
	public static void save() throws IOException {
		JSONObject data = new JSONObject();
		JSONArray classesJsonArray = new JSONArray();
		classes.forEach((className, theClass) -> {
			JSONObject classJsonObject = new JSONObject();
			JSONArray groupsJsonArray = new JSONArray();
			
			theClass.getGroups().forEach((groupName, group) -> {
				JSONObject groupJsonObject = new JSONObject();
				JSONArray membersJsonArray = new JSONArray();
				JSONArray tasksJsonArray = new JSONArray();
				
				group.getMembers().forEach((studentName, student) -> {
					JSONObject studentJsonObject = new JSONObject();
					JSONArray studentTasksJsonArray = new JSONArray();
					
					student.getTasks().forEach((taskName, task) -> {
						JSONObject taskJsonObject = new JSONObject();
						taskJsonObject.put("name", taskName);
						taskJsonObject.put("description", task.getDescirption());
						taskJsonObject.put("is_current", task.isCurrent());
						taskJsonObject.put("grade", task.gradeToString());
						studentTasksJsonArray.add(taskJsonObject);
						
					});
					
					studentJsonObject.put("name", studentName);
					studentJsonObject.put("tasks", studentTasksJsonArray);
					membersJsonArray.add(studentJsonObject);
					
				});
				
				group.getTasks().forEach((taskName, task) -> {
					JSONObject taskJsonObject = new JSONObject();
					taskJsonObject.put("name", taskName);
					taskJsonObject.put("description", task.getDescirption());
					taskJsonObject.put("is_current", task.isCurrent());
					taskJsonObject.put("grade", task.gradeToString());
					tasksJsonArray.add(taskJsonObject);
					
				});
				
				groupJsonObject.put("name", groupName);
				groupJsonObject.put("members", membersJsonArray);
				groupJsonObject.put("tasks", tasksJsonArray);
				groupsJsonArray.add(groupJsonObject);
			
			});
			
			classJsonObject.put("name", className);
			classJsonObject.put("groups", groupsJsonArray);
			classesJsonArray.add(classJsonObject);
			
		});
		
		data.put("classes", classesJsonArray);
		FileWriter writer = new FileWriter(dataFile);
		data.writeJSONString(writer);
		System.out.println(data.toJSONString());
		writer.flush();
		writer.close();
		
	}

	public static String getCurrentClass() {
		if (currentClass.isPresent())
			return currentClass.get().getName();
		return null;
		
	}

}
