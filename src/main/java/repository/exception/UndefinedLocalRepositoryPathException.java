package repository.exception;

public class UndefinedLocalRepositoryPathException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "You must define the url of remote repository!";
	}
}
