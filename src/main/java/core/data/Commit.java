package core.data;

import org.eclipse.jgit.lib.ObjectId;

import core.result.List;

public class Commit {

	private ObjectId id;
	private CommitInfo info;
	private List<DiffFile> diffFiles;

	public Commit(ObjectId id) {
		this.id = id;
		this.info = null;
		this.diffFiles = null;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public CommitInfo getInfo() {
		return info;
	}

	public void setInfo(CommitInfo info) {
		this.info = info;
	}

	public List<DiffFile> getDiffFiles() {
		return diffFiles;
	}

	public void setDiffFiles(
		List<DiffFile> diffFiles) {
		this.diffFiles = diffFiles;
	}
	
	
	
	
}
