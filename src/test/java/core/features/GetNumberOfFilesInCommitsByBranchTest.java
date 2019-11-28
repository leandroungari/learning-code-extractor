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

  private final String REPOSITORY = "refactoring-toy-example";
  private final String BRANCH = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  @Test
  public void run() {

    var command = new GetNumberOfFilesInCommitsByBranch();
    command.setParams(REPOSITORY,BRANCH);

    var quantityOfFiles = command.action();
    
    System.out.println(quantityOfFiles);

    assertEquals(65, quantityOfFiles.size());
  }
}