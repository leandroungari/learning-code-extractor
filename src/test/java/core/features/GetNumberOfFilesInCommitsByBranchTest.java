package core.features;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.junit.Before;
import org.junit.Test;

import core.Player;
import repository.Repository;
import repository.exception.GitRepositoryException;

public class GetNumberOfFilesInCommitsByBranchTest {

  private Player player;
  private final String REPOSITORY = "refactoring-toy-example";
  private final String BRANCH = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  @Before
  public void before() {

    player = new Player();
  }

  @Test
  public void run() 
    throws GitAPIException, GitRepositoryException {

    var repo = Repository.create(new File(
			player.settings().folderOfRepositories() + "/"
			+ REPOSITORY
		));
    
		var result = repo
		  .branch(ObjectId.fromString(BRANCH))
      .commits();
    
    var quantityOfFiles = result
      .stream()
      .map((commit) -> {
      
        return commit
          .allFilesByExtension(".java")
          .size();
      })
      .collect(Collectors.toList());
    
    assertEquals(65, quantityOfFiles.size());
  }
}