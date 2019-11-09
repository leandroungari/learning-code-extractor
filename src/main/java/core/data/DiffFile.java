package core.data;

public class DiffFile {

	private String type;
	private String oldPath;
	private String newPath;
	
	/**
   * This describes the data of a diff file
   * @param type The type of change file (INSERT,MODIFY,DELETE)
   * @param oldPath The path of file in previous version
   * @param newPath The path of file in forward version
   */
	public DiffFile(
    String type, 
    String oldPath,
		String newPath) {

		this.type = type;
		this.oldPath = oldPath;
		this.newPath = newPath;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOldPath() {
		return oldPath;
	}

	public void setOldPath(String oldPath) {
		this.oldPath = oldPath;
	}

	public String getNewPath() {
		return newPath;
	}

	public void setNewPath(String newPath) {
		this.newPath = newPath;
	}
	
}
