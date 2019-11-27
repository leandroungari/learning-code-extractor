package core.features;

import org.eclipse.jgit.api.errors.JGitInternalException;
import org.junit.Before;
import org.junit.Test;

import core.Player;
import repository.exception.GitRepositoryException;

public class DownloadRepositoryTest {

  private String REPOSITORY = "gson";
  private String URL = "https://github.com/google/gson.git";

  private Player player;

  @Before
  public void before() {
    player = new Player();
  }

  @Test(expected = JGitInternalException.class)
  public void run() {

    try {
      
      player
      .repository(REPOSITORY)
      .download(URL);

    } catch (GitRepositoryException e) {
      e.printStackTrace();
    }
  }
}