package ast.extractor;

import java.util.List;

import ast.ASTComparator;
import ast.CommitChanges;
import ast.OperationType;
import ast.change.Change;
import ast.change.FieldChange;
import ast.change.MethodChange;
import ast.change.StatementChange;
import ast.mapping.Mapping;
import ast.mapping.Modification;
import core.Player;
import core.data.DiffFile;
import core.features.FeatureType;
import repository.Commit;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

public class ModificationBuilder {

  private String repository;
  private String branch;
  private String commit;

  private Commit before;
  private Commit current;

  private List<DiffFile> diffFiles;

  private ASTComparator comparator;

  public ModificationBuilder(String repository, String branch, String commit) {

    this.repository = repository;
    this.branch = branch;
    this.commit = commit;

    buildExtractor();
  }

  /**
   * It defines some settings used by extractor - before and current commits -
   * diff files in current commit
   */
  private void buildExtractor() {
    identifyCommits();
    identifyDiffFiles();
  }

  private void identifyCommits() {
    current = Commit.fromData(repository, branch, commit);
    before = current.before();
  }

  private void identifyDiffFiles() {
    var player = new Player();
    this.diffFiles = player.<List<DiffFile>>get(FeatureType.DIFF_FILES_IN_COMMIT).setParams(repository, branch, commit)
        .action();
  }

  private String getContentOfFile(Commit commit, String file) {

    return commit.getFile(file).get();
  }

  private Change<?> createChange(CtElement element) {

    Change<?> change = null;

    if (element instanceof CtMethod) {
      change = new MethodChange((CtMethod<?>) element);
      System.err.println("### ELEMENTO TIPO MÉTODO");
    } else if (element instanceof CtFieldReference) {
      change = new FieldChange((CtFieldReference<?>) element);
      System.err.println("### ELEMENTO TIPO ATRIBUTO");
    } else {
      // os demais comandos
      // por enquanto os statements
      try {
        change = new StatementChange((CtStatement) element);
      } catch (ClassCastException ex) {
        if (element instanceof CtTypeReference) {
          var statement = element.getParent(new TypeFilter<>(CtStatement.class));
          change = new StatementChange(statement);
        }
      }
    }

    return change;
  }

  public CommitChanges create() {

    var commitChanges = new CommitChanges(commit);

    for (var diff : diffFiles) {
      var oldSource = getContentOfFile(before, diff.getOldPath());
      var newSource = getContentOfFile(current, diff.getNewPath());

      comparator = new ASTComparator(oldSource, newSource);
      var list = comparator.extract();
      Changes changes;
      for (var modification : list) {
        changes = processModification(modification);
        changes.setOldPath(diff.getOldPath());
        changes.setNewPath(diff.getNewPath());
        commitChanges.add(changes);
      }
    }

    return commitChanges;
  }

  private Changes processModification(Modification m) {
    var changes = new Changes();
    for (var mapping : m) {
      changes.add(processMapping(mapping));
    }
    return changes;
  }

  private ChangeItem processMapping(Mapping mapping) {
    if(mapping.type() == OperationType.INSERT) {
      return new ChangeItem()
        .type(OperationType.INSERT)
        .destination(createChange(mapping.destination()));
    }
    else if(mapping.type() == OperationType.DELETE) {
      return new ChangeItem()
        .type(OperationType.DELETE)
        .source(createChange(mapping.source()));
    }
    else {
      return new ChangeItem()
        .type(mapping.type())
        .source(createChange(mapping.source()))
        .destination(createChange(mapping.destination()));
    }
  }
}