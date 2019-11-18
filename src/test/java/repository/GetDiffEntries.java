package repository;

import java.io.File;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.junit.Before;
import org.junit.Test;

import core.Player;
import repository.exception.GitRepositoryException;

public class GetDiffEntries {

  private Repository repo;
  private Player player;
  private static final String BRANCH_MASTER = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
  private static final String FIRST_COMMIT = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  @Before
  public void before() throws GitRepositoryException {

    player = new Player();

    repo = Repository
      .create(new File(
        player.settings().folderOfRepositories() + 
        "/refactoring-toy-example"
      ));
  }

  @Test
  public void run() throws GitAPIException {
    
    var commit = repo
      .branch(ObjectId.fromString(BRANCH_MASTER))
      .commit(ObjectId.fromString(FIRST_COMMIT));

    var files = commit
      .diff()
      .stream()
      .map(diff -> diff.newPath())
      .collect(Collectors.toList());

    var list = commit
      .diffEntriesInFiles(files);

    System.out.println(list);
  }
  
}