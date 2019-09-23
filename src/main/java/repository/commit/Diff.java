package repository.commit;

import org.eclipse.jgit.diff.DiffEntry;
import com.google.gson.Gson;

public class Diff {

	private String type;
	private String oldPath;
	private String newPath;
	
	public static Diff build(DiffEntry entry) {
		
		return new Diff(
			entry.getOldPath(),
			entry.getNewPath(),
			entry.getChangeType().name()
		);
	}
	
	private Diff(String oldPath, String newPath, String type) {
		this.oldPath = oldPath;
		this.newPath = newPath;
		this.type = type;
	}
	
	public String type() {
		return type;
	}
	
	public String oldPath() {
		return oldPath;
	}
	
	public String newPath() {
		return newPath;
	}
	
	@Override
	public String toString() {
		
		var gson = new Gson();
		return gson.toJson(this);
	}
}
