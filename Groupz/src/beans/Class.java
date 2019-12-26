package beans;

import java.util.HashMap;

public class Class {
	
	private String name;
	private HashMap<String, Student> students;
	private HashMap<String, Group> groups;
	
	public Class() {
		name = null;
		students = new HashMap<String, Student>();
		groups = new HashMap<String, Group>();
		
	}
	
	public Class(String n) {
		name = n;
		students = new HashMap<String, Student>();
		groups = new HashMap<String, Group>();
		
	}
	
	public void setName(String val) {
		name = val;
		
	}
	
	public String getName() {
		return name;
		
	}
	
	public Student getStudent(String val) {
		return students.getOrDefault(val, null);
		
	}
	
	public Group getGroup(String val) {
		return groups.getOrDefault(val, null);
		
	}
	
	public HashMap<String, Student> getStudents() {
		return students;
		
	}
	
	public HashMap<String, Group> getGroups() {
		return groups;
		
	}
	
	public void addStudent(Student val) {
		students.put(val.getName(), val);
		
	}
	
	public void addGroup(Group val, boolean withStudents) {
		groups.put(val.getName(), val);
		if (withStudents)
			val.getMembers().forEach((studentName, student) -> students.put(studentName, student));
		
	}
	
	public void removeStudent(Student val) {
		students.remove(val.getName());
		
	}
	
	public void removeGroup(Group val, boolean withStudents) {
		groups.remove(val.getName());
		if (withStudents)
			val.getMembers().forEach((name, ignore) -> students.remove(name));
		
	}

}
