package repository;

import org.eclipse.jgit.lib.ObjectId;

public class Branch {

	private String name;
	private ObjectId id;
	
	public static Branch create(String name, ObjectId id) {
		
		return new Branch(name, id);
	}
	
	private Branch(String name, ObjectId id) {
		this.name = name;
		this.id = id;
	}
	
	public ObjectId id() {
		return id;
	}
	
	public String name() {
		return name;
	}
}
