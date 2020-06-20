package ast.snapshot.exception;

public class NotFoundElementInSnapshotException extends RuntimeException {

	/**
	 *
	 */
  private static final long serialVersionUID = 1L;
  
  private String path;
  private String commit;

  public NotFoundElementInSnapshotException(String commit, String path) {
    this.commit = commit;
    this.path = path;
  }

  @Override
  public String getMessage() {
    return "The element was not found in commit snapshot: " + path + " in " + commit;
  }
}
