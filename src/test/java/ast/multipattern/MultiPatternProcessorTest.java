package ast.multipattern;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Test;

public class MultiPatternProcessorTest {

  @Test
  public void repoRefactoring() throws GitAPIException {
    var repository = "refactoring-toy-example";
    var branch = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

    var processor = new MultiPatternProcessor(repository, branch);
    processor.identify();
  }

  @Test
  public void repoGson() throws GitAPIException {
    var repository = "gson";
    var branch = "ceae88bd6667f4263bbe02e6b3710b8a683906a2";

    var processor = new MultiPatternProcessor(repository, branch);
    processor.identify();
  }
}