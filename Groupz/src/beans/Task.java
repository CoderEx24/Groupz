package beans;

public class Task {
	
	public enum Grade {BLUE, GREEN, YELLOW, RED, UNRATED}
	
	private String name;
	private String description;
	private Grade grade;
	private boolean isCurrent;
	
	public Task() {
		name = null;
		description = null;
		grade = null;
		isCurrent = false;
		
	}
	
	public Task(String n, String d, Grade g, boolean i) {
		name = n;
		description = d;
		grade = g;
		isCurrent = i;
		
	}
	
	public void setName(String val) {
		name = val;
		
	}
	
	public void setDescription(String val) {
		description = val;
		
	}
	
	public void setGrade(Grade val) {
		grade = val;
		
	}
	
	public void setIsCurrent(boolean val) {
		isCurrent = val;
		
	}
	
	public String getName() {
		return name;
		
	}
	
	public String getDescirption() {
		return description;
		
	}
	
	public Grade getGrade() {
		return grade;
		
	}
	
	public boolean isCurrent() {
		return isCurrent;
		
	}
	
	public String toString() {
		return name;
		
	}
	
}
