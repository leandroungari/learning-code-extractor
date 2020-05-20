package ast;

import java.util.List;

import ast.extractor.CommitModifications;
import ast.extractor.Modification;
import ast.extractor.comparator.Comparator;
import core.Player;
import core.data.DiffFile;
import core.features.FeatureType;
import repository.Commit;

public class ModificationsBuilder {

  private String repository;
  private String branch;
  private String commit;

  private Commit before;
  private Commit current;

  private List<DiffFile> diffFiles;

  private Comparator comparator;

  private CommitModifications modifications;

  public ModificationsBuilder(
    String repository, 
    String branch, 
    String commit) {

    this.repository = repository;
    this.branch = branch;
    this.commit = commit;

    this.modifications = new CommitModifications(commit);

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
    this.diffFiles = player
      .<List<DiffFile>>get(FeatureType.DIFF_FILES_IN_COMMIT)
      .setParams(repository, branch, commit)
      .action();
  }

  

  private Boolean isJavaFile(String path) {

    return path.contains(".java");
  }

  private static String getContentOfFile(Commit commit, String file) {
    var content = "";
    if(!file.equals("/dev/null")) {
      content = commit.getFile(file).get();
    }
    return content;
  }

  public CommitModifications createModifications() {

    for (var diff : diffFiles) {
      if(isJavaFile(diff.getOldPath()) ||
         isJavaFile(diff.getNewPath())) {

        var oldSource = getContentOfFile(before, diff.getOldPath());
        var newSource = getContentOfFile(current, diff.getNewPath());
          
        var modification = createNewModification(
          diff.getOldPath(),
          diff.getNewPath()
        );

        comparator = new Comparator(
          diff.getOldPath(), 
          oldSource, 
          diff.getNewPath(), 
          newSource
        );
        modification.setMappings(comparator.generateMappings());
        this.modifications.add(modification);
      }
    }

    return modifications;
  }

  private Modification createNewModification(
    String oldPath,
    String newPath) {

    var modification = new Modification();
    modification.setNewPath(newPath);
    modification.setOldPath(oldPath);
    return modification;
  }

}