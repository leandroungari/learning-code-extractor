package persistence.system;

public class OperationSystem {
  
	private Directory directory;
	
	public OperationSystem() {
		this.directory = new Directory();
	}

	public Directory directory() {
		return this.directory;
	}
}