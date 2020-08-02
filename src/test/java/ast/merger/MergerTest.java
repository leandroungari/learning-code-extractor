package ast.merger;

import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Test;

public class MergerTest {

  @Test
  public void mergerRefactoring() throws GitAPIException, IOException {
    var repository = "refactoring-toy-example";
    var branch = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

    var merger = new MergeCluster(repository, branch);
    merger.merge();
  }

  @Test
  public void mergerGson() throws GitAPIException, IOException {
    var repository = "gson";
    var branch = "ceae88bd6667f4263bbe02e6b3710b8a683906a2";

    var merger = new MergeCluster(repository, branch);
    merger.merge();
  }
}