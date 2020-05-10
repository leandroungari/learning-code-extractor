package repository.commit;

import java.io.IOException;

import org.eclipse.jgit.lib.ObjectId;

import repository.Commit;
import repository.GitFactory;

public class File {

	private Commit commit;
	private String name;
	private ObjectId id;
	
	public File(Commit commit,String name, ObjectId id) {
		this.commit = commit;
		this.name = name;
		this.id = id;
	}
	
	public String name() {
		return name;
	}

	public String folder() {
		var text = "";
		var items = name.split("/");
		for(var i = 0; i < items.length-1; i++) {
			text += items[i] + "/";
		}
		return text;
	}

	public String file() {
		var items = name.split("/");
		return items[items.length-1];
	}
	
	public ObjectId id() {
		return id;
	}
	
	public String get() {
		
		var factory = new GitFactory(commit.containedBranch().containedRepository());
		try {
			return factory.fileContent(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
