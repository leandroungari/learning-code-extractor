package core.features;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.junit.Before;
import org.junit.Test;

import core.Player;
import repository.Repository;
import repository.commit.CommitInfo;
import repository.exception.GitRepositoryException;

public class GetNumberOfContributorsByBranchTest {

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
      
    var authors = new ArrayList<String>();
    
    var quantityOfContributors = result
      .stream()
      .map((commit) -> {
      
        var info = player
          .<CommitInfo>get(FeatureType.COMMIT_INFO)
          .setParams(REPOSITORY, BRANCH, commit.id().getName())
          .action();

        if(!authors.contains(info.getEmail())) {
          authors.add(info.getEmail());
        }

        return authors.size();
      })
      .collect(Collectors.toList());
    
    assertEquals(65, quantityOfContributors.size());
  }
}