package core.data;

import org.eclipse.jgit.lib.ObjectId;

public class Commit {

	private ObjectId id;
	private CommitInfo info;

	public Commit(ObjectId id) {
		this.id = id;
		this.info = null;
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
	
	
}
