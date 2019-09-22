package repository.commit;

import java.io.IOException;

import org.eclipse.jgit.lib.ObjectId;

import repository.GitFactory;

public class File {

	private String name;
	private ObjectId id;
	
	public File(String name, ObjectId id) {
		this.name = name;
		this.id = id;
	}
	
	public String name() {
		return name;
	}
	
	public ObjectId id() {
		return id;
	}
	
	public String get() {
		
		var factory = new GitFactory();
		try {
			
			return factory.fileContent(id);
		} catch (IOException e) {
			
			System.err.println(e.getMessage());
		}
		
		return null;
	}
}
