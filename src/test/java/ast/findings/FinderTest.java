package ast.findings;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Test;

public class FinderTest {

  @Test
  public void finderRefactoringToy() throws GitAPIException {
    var repository = "refactoring-toy-example";
    var branch = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

    var finder = new Finder(repository, branch);
    finder.setRepositoryTarget(repository, branch);
    var data = finder.compatibility();
    finder.toStore(data);
  }
}