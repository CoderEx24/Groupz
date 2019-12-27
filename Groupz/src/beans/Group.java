package beans;

import java.util.HashMap;
import java.util.Optional;

public class Group {
	
	private String name;
	private String className;
	private HashMap<String, Student> memebers;
	private HashMap<String, Task> tasks;
	private Optional<Task> currentTask;
	
	public Group() {
		name = null;
		className = null;
		memebers = new HashMap<String, Student>();
		tasks = new HashMap<String, Task>();
		currentTask = Optional.empty();
		
	}

	public Group(String n, String c) {
		name = n;
		className = c;
		memebers = new HashMap<String, Student>();
		tasks = new HashMap<String, Task>();
		currentTask = Optional.empty();
		
	}
	
	public void setName(String val) {
		name = val;
		
	}
	
	public void setClassName(String val) {
		className = val;
		
	}
	
	public void setCurrentTask(Task t) {
		tasks.put(t.getName(), t);
		currentTask = Optional.ofNullable(t);
		
	}
	
	public String getName() {
		return name;
		
	}
	
	public String getClassName() {
		return className;
		
	}
	
	public Student getMember(String val) {
		return memebers.getOrDefault(val, null);
		
	}
	
	public Task getTask(String val) {
		return tasks.getOrDefault(val, null);		
	}
	
	public Optional<Task> getCurrentTask() {
		return currentTask;
		
	}
	
	public boolean haveCurrentTask() {
		return currentTask.isPresent();
		
	}
	
	public HashMap<String, Student> getMembers() {
		return memebers;
		
	}
	
	public HashMap<String, Task> getTasks() {
		return tasks;
		
	}
	
	public void addMember(Student s) {
		memebers.put(s.getName(), s);
		
	}
	
	public void addTask(Task t) {
		tasks.put(t.getName(), t);
		if (t.isCurrent() && !currentTask.isPresent())
			currentTask = Optional.of(t);
		else if (t.isCurrent())
			t.setIsCurrent(false);
		
	}
	
	public void removeMember(Student val) {
		memebers.remove(val.getName());
		
	}
	
	public void removeTask(Task val) {
		currentTask.ifPresent(task -> {
			if (val == task) 
				currentTask = Optional.empty();
			
		});
		tasks.remove(val.getName());
		
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
	
}
