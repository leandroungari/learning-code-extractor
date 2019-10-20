package core.data;

import org.eclipse.jgit.lib.ObjectId;

public class Branch {

	private String name;
	private ObjectId id;
	
	public Branch(String name, ObjectId id) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
	
	
	
}
