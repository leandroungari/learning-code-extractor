package core.features;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;

import core.Feature;
import repository.Commit;
import repository.Repository;
import repository.exception.GitRepositoryException;

public class GetNumberOfFilesInCommitsByBranch extends Feature<List<Integer>>{

  private String repository;
  private String branch;

  public GetNumberOfFilesInCommitsByBranch() {
    super();
  }

  @Override
  public Feature<List<Integer>> setParams(String... params) {
    
    repository = params[0];
    branch = params[1];

    return this;
  }

  @Override
  public List<Integer> action() {

    try {
      var repo = Repository.create(new File(
			  player.settings().folderOfRepositories() + "/"
			  + repository
		  ));
    
		var result = repo
		  .branch(ObjectId.fromString(branch))
      .commits();
    
    return result
      .stream()
      .map(this::getNumberOfJavaFilesInCommit)
      .collect(Collectors.toList());
    }
    catch(GitAPIException | GitRepositoryException e) {
      e.printStackTrace();
    }

    return null;
  }

  private Integer getNumberOfJavaFilesInCommit(Commit commit) {
    return commit
      .allFilesByExtension(".java")
      .size();
  }

}