package beans;

import java.util.HashMap;
import java.util.Optional;

public class Student {
	
	private String name;
	private String className;
	private String groupName;
	private String email;
	private HashMap<String, Task> tasks;
	private Optional<Task> currentTask;
	
	public Student() {
		name = null;
		className = null;
		groupName = null;
		email = null;
		tasks = new HashMap<String, Task>();
		currentTask = Optional.empty();
		
	}
	
	public Student(String n, String g, String c, String e) {
		name = n;
		className = c;
		groupName = g;
		email = e;
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
	
	public String getEmail() {
		return email;
		
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
	
	public void addTask(Task val) {
		tasks.put(val.getName(), val);
		if (val.isCurrent())
			currentTask = Optional.of(val);
		
	}
	
	public void deleteTask(Task val) {
		if (tasks.containsValue(val)) {
			currentTask.ifPresent(task -> {
				if (val == task) {
					currentTask = Optional.empty();
					
				}
				
			});
			tasks.remove(val.getName());
		}
		
	}
	
	public boolean gradeCurrentTask(Task.Grade grade) {
		boolean didHave = currentTask.isPresent();
		currentTask.ifPresent(task -> {
			task.setGrade(grade);
			task.setIsCurrent(false);
			
		});
		currentTask = Optional.empty();
		return didHave;
		
	}
	
	public String toString() {
		return name + "-" + groupName + "-" + className + "-" + email;
		
	}

	public void setEmail(String newEmail) {
		email = newEmail;
		
	}

}
