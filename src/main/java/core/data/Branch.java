package core.data;

import org.eclipse.jgit.lib.ObjectId;

public class Branch {

	private String name;
	private ObjectId id;
  
  /**
   * This method creates a reference to a branch
   * @param name Name of branch ex.: "refs/origin/master"
   * @param id ID of branch (hash)
   */
	public Branch(String name, ObjectId id) {
		this.name = name;
		this.id = id;
	}

  /**
   * It gets the name of the branch
   */
	public String getName() {
		return name;
	}

  /**
   * This sets the name of branch
   * @param name Name of the branch
   */
	public void setName(String name) {
		this.name = name;
	}

  /**
   * It gets the ID of the branch
   */
	public ObjectId getId() {
		return id;
	}

  /**
   * It sets the ID of the branch
   */
	public void setId(ObjectId id) {
		this.id = id;
	}
}
