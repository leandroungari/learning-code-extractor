package ast;

import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.extractor.CommitMappings;
import ast.extractor.comparator.Comparator;
import ast.extractor.mapping.Mapping;
import repository.Repository;

public class MappingsExtractor {

  private String repository;
  private String branch;
  private String commit;

  private Repository instance;

  // private List<DiffFile> diffFiles;

  // private Comparator comparator;

  private List<Mapping> mappings;

  public MappingsExtractor(String repository, String branch, String commit) {

    this.repository = repository;
    this.branch = branch;
    this.commit = commit;

    this.instance = Repository.create(repository);

    // this.mappings = new ArrayList<Mapping>();

    // buildExtractor();
  }

  /**
   * It defines some settings used by extractor - before and current commits -
   * diff files in current commit
   */
  /*
   * private void buildExtractor() { identifyCommits(); identifyDiffFiles(); }
   * 
   * private void identifyCommits() { current = Commit.fromData(repository,
   * branch, commit); before = current.before(); }
   * 
   * private void identifyDiffFiles() { var player = new Player(); this.diffFiles
   * = player .<List<DiffFile>>get(FeatureType.DIFF_FILES_IN_COMMIT)
   * .setParams(repository, branch, commit) .action(); }
   * 
   * 
   * 
   * private Boolean isJavaFile(String path) {
   * 
   * return path.contains(".java"); }
   * 
   * private static String getContentOfFile(Commit commit, String file) { var
   * content = ""; if(!file.equals("/dev/null")) { content =
   * commit.getFile(file).get(); } return content; }
   */

  public CommitMappings createMappings() {

    /*
     * for (var diff : diffFiles) { if(isJavaFile(diff.getOldPath()) ||
     * isJavaFile(diff.getNewPath())) {
     * 
     * var oldSource = getContentOfFile(before, diff.getOldPath()); var newSource =
     * getContentOfFile(current, diff.getNewPath());
     * 
     * comparator = new Comparator( diff.getOldPath(), oldSource, diff.getNewPath(),
     * newSource );
     * 
     * this.mappings.add(comparator.generateMappings()); } }
     * 
     * return mappings;
     */
    try {
      var comparator = new Comparator(
        instance.branch(branch).commit(commit)
      );
      var items = comparator.generateMappings();

      var result = new CommitMappings(repository, branch, commit);
      result.add(items);

      return result;
    } catch (GitAPIException e) {
      e.printStackTrace();
    }

    return null;
  }

}