package core.data;

import java.util.List;

import org.eclipse.jgit.lib.ObjectId;

public class Commit {

	private ObjectId id;
	private List<DiffFile> diffFiles;

  /**
   * It creates a reference to a commit by ID
   * @param id Commit ID (hash)
   */
	public Commit(ObjectId id) {
		this.id = id;
		this.diffFiles = null;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

  /**
   * Get the list of diff files in a commit
   * @return List of diffs
   */
	public List<DiffFile> getDiffFiles() {
		return diffFiles;
	}

	public void setDiffFiles(
		List<DiffFile> diffFiles) {
		this.diffFiles = diffFiles;
	}
}
