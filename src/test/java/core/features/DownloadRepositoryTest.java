package core.features;

import java.io.File;

import org.eclipse.jgit.api.errors.JGitInternalException;
import org.junit.Before;
import org.junit.Test;

import core.Player;
import repository.Repository;
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
      
      Repository.create(
		  	URL, 
		  	new File(
		  		player.settings().folderOfRepositories() + "/" +
		  		REPOSITORY
		  	)
		  );

    } catch (GitRepositoryException e) {
      e.printStackTrace();
    }
  }
}