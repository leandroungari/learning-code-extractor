package core.features;

import org.eclipse.jgit.api.errors.JGitInternalException;
import org.junit.Test;

public class DownloadRepositoryTest {

  private String REPOSITORY = "gson";
  private String URL = "https://github.com/google/gson.git";


  @Test(expected = JGitInternalException.class)
  public void run() {

    var command = new DownloadRepository();
    command.setParams(REPOSITORY, URL);
    command.action();
  }
}