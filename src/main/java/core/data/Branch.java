package core.data;

import storage.Storable;

public class Branch implements Storable<Branch> {

	private String name;
	private String id;
  
  public Branch() {

  }

  /**
   * This method creates a reference to a branch
   * @param name Name of branch ex.: "refs/origin/master"
   * @param id ID of branch (hash)
   */
	public Branch(String name, String id) {
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
	public String getId() {
		return id;
	}

  /**
   * It sets the ID of the branch
   */
	public void setId(String id) {
		this.id = id;
	}

  @Override
  public String[] attributes() {
    
    return new String[]{"name","id"};
  }

  @Override
  public String[] write() {
    
    return new String[]{
      getName(), 
      getId()
    };
  }

  @Override
  public Branch read(String[] values) {
    setName(values[0]);
    setId(values[1]);
    return this;
  }
}
