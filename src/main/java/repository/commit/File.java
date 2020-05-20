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
	
	/**
	 * It returns the complete path of file 
	 * in the repository
	 * @return The filepath
	 */
	public String fullPath() {
		return name;
	}

	/**
	 * It returns the path of folders to 
	 * the file
	 * @return Path of folders
	 */
	public String folder() {
		var text = "";
		var items = name.split("/");
		for(var i = 0; i < items.length-1; i++) {
			text += items[i] + "/";
		}
		return text;
	}

	/**
	 * It returns the filename with extension
	 * @return Filename with extension
	 */
	public String filename() {
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
		
		return "";
	}
}
