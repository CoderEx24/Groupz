package beans;

import java.util.HashMap;
import java.util.Optional;

public class Student {
	
	private String name;
	private String className;
	private String groupName;
	private HashMap<String, Task> tasks;
	private Optional<Task> currentTask;
	
	public Student() {
		name = null;
		className = null;
		groupName = null;
		tasks = new HashMap<String, Task>();
		currentTask = Optional.empty();
		
	}
	
	public Student(String n, String c, String g) {
		name = n;
		className = c;
		groupName = g;
		tasks = new HashMap<String, Task>();
		currentTask = Optional.empty();
		
	}
	
	public void setName(String val) {
		name = val;
		
	}
	
	public void setClassName(String val) {
		className = val;
		
	}
	
	public void setGroupName(String val) {
		groupName = val;
		
	}
	
	public void setCurrentTask(Task t) {
		currentTask = Optional.ofNullable(t);
		
	}
	
	public String getName() {
		return name;
		
	}
	
	public String getClassName() {
		return className;
		
	}
	
	public String getGroupName() {
		return groupName;
		
	}
	
	public Task getTask(String t) {
		return tasks.getOrDefault(t, null);
		
	}
	
	public Optional<Task> getCurrentTask() {
		return currentTask;
		
	}
	
	public boolean haveCurrentTask() {
		return currentTask.isPresent();
		
	}
	
	public HashMap<String, Task> getTasks() {
		return tasks;
		
	}
	
	public void addTask(Task t) {
		tasks.put(t.getName(), t);
		
	}
	
	public void deleteTask(Task t) {
		if (tasks.containsValue(t))
			tasks.remove(t.getName());
		
	}

}
