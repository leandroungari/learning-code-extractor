package ast.extractor;

import java.util.List;

import ast.CommitModifications;
import ast.extractor.mapping.Mapping;
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

    return commit.getFile(file).get();
  }

  public CommitModifications create() {

    for (var diff : diffFiles) {
      var oldSource = getContentOfFile(before, diff.getOldPath());
      var newSource = getContentOfFile(current, diff.getNewPath());

      comparator = new ASTComparator(oldSource, newSource);
      var list = comparator.mappings();

      if(!list.isEmpty()) {
        groupingMappings(
          list,
          diff.getOldPath(),
          diff.getNewPath()
        );
      }
    }

    return modifications;
  }

  private void groupingMappings(
    List<Mapping> mappings,
    String oldPath,
    String newPath) {

    for(var unsetted: mappings) {
      AssignModificationAction result = AssignModificationAction.CREATE_MODIFICATION;
      for(var modification: modifications.getModifications()) {
        result = assignMappingToModification(modification, unsetted);
        if(result == AssignModificationAction.NEXT_MAPPING) {
          break;
        }
      }
      if(result == AssignModificationAction.CREATE_MODIFICATION) {
        var modification = new Modification(unsetted);
        modification.setNewPath(newPath);
        modification.setOldPath(oldPath);
        modifications.add(modification);
      }
    }
  }

  private AssignModificationAction assignMappingToModification(
    Modification modification, Mapping mapping) {
    
    var settedMappings = modification.getMappings();
    for(var setted: settedMappings) {
      if(setted.isParentOf(mapping) || 
         setted.equals(mapping)) {
        return AssignModificationAction.NEXT_MAPPING;
      } else if(setted.isChildOf(mapping)) {
        settedMappings.set(
          settedMappings.indexOf(setted), 
          mapping
        );
        return AssignModificationAction.REPLACED_MAPPING;
      }
    }
    
    return AssignModificationAction.CREATE_MODIFICATION;
  }

}