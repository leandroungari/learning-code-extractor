package ast.snapshot;

import java.util.List;

import org.junit.Test;

import core.Player;
import core.features.FeatureType;
import repository.commit.File;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.visitor.filter.TypeFilter;

public class SnapshotTest {

  private final String REPOSITORY = "refactoring-toy-example";
  private final String BRANCH = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
  private final String COMMIT = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  private static final Player player = new Player();

  @Test
  public void creatingSnapshot() {

    var snapshot = new Snapshot(COMMIT);

    var filesOfCommit = player
      .<List<File>>get(FeatureType.LIST_OF_FILES)
      .setParams(REPOSITORY,BRANCH,COMMIT)
      .action();

    filesOfCommit
    .stream()
    .forEach(file -> {
      snapshot.add(file.name(), file.get());
    });

    snapshot.clear();
  }

  @Test
  public void buildModel() {

    var snapshot = new Snapshot(COMMIT);

    var filesOfCommit = player
      .<List<File>>get(FeatureType.LIST_OF_FILES)
      .setParams(REPOSITORY,BRANCH,COMMIT)
      .action();

    filesOfCommit
    .stream()
    .forEach(file -> {
      snapshot.add(file.name(), file.get());
    });

    var result = snapshot.model()
      .getElements(new TypeFilter<>(CtPackage.class));
    
    result.forEach(item -> {
      System.out.println(item);
    });
  }

}