package ast;

import java.util.List;

import ast.extractor.CommitMappings;
import ast.extractor.comparator.Comparator;
import core.Player;
import core.data.DiffFile;
import core.features.FeatureType;
import repository.Commit;

public class MappingsExtractor {

  private String repository;
  private String branch;
  private String commit;

  private Commit before;
  private Commit current;

  private List<DiffFile> diffFiles;

  private Comparator comparator;

  private CommitMappings mappings;

  public MappingsExtractor(
    String repository, 
    String branch, 
    String commit) {

    this.repository = repository;
    this.branch = branch;
    this.commit = commit;

    this.mappings = new CommitMappings(repository, branch, commit);

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

  public CommitMappings createMappings() {

    for (var diff : diffFiles) {
      if(isJavaFile(diff.getOldPath()) ||
         isJavaFile(diff.getNewPath())) {

        var oldSource = getContentOfFile(before, diff.getOldPath());
        var newSource = getContentOfFile(current, diff.getNewPath());

        comparator = new Comparator(
          diff.getOldPath(), 
          oldSource, 
          diff.getNewPath(), 
          newSource
        );
        
        this.mappings.add(comparator.generateMappings());
      }
    }

    return mappings;
  }

}