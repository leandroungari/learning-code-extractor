package ast.extractor;

import java.util.List;

import ast.CommitModifications;
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

  private ASTComparator comparator;

  private CommitModifications modifications;

  public ModificationsBuilder(String repository, String branch, String commit) {

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

  private String getContentOfFile(Commit commit, String file) {
    var content = "";
    if(!file.equals("/dev/null")) {
      content = commit.getFile(file).get();
    }
    return content;
  }

  private Boolean isJavaFile(String path) {

    return path.contains(".java");
  }

  public CommitModifications createModifications() {

    for (var diff : diffFiles) {
      

      if(isJavaFile(diff.getOldPath()) ||
         isJavaFile(diff.getNewPath())) {

        var oldSource = getContentOfFile(before, diff.getOldPath());
        var newSource = getContentOfFile(current, diff.getNewPath());

        comparator = new ASTComparator(oldSource, newSource);
        var list = comparator.mappings();
          
        var modification = createNewModification(
          diff.getOldPath(),
          diff.getNewPath()
        );
          
        if(!list.isEmpty()) {
          groupingMappings(
            list,
            modification
          );
        }
      
        this.modifications.add(modification);
      }
    }

    return modifications;
  }

  private void groupingMappings(
    List<Mapping> mappings,
    Modification modification) {

    for(var unsetted: mappings) {
      var result = verifyAggregationOfMappings(
        modification, 
        unsetted
      );
      
      if(result == AssignMappingAction.CREATE_MAPPING) {
        modification.getMappings().add(unsetted);
      }
    }
  }

  private AssignMappingAction verifyAggregationOfMappings(
    Modification modification, 
    Mapping mapping) {
    
    var settedMappings = modification.getMappings();
    for(var setted: settedMappings) {
      if(setted.isParentOf(mapping) || 
         setted.equals(mapping)) {
        return AssignMappingAction.NEXT_MAPPING;
      } else if(setted.isChildOf(mapping)) {
        settedMappings.set(
          settedMappings.indexOf(setted), 
          mapping
        );
        return AssignMappingAction.REPLACED_MAPPING;
      }
    }
    return AssignMappingAction.CREATE_MAPPING;
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