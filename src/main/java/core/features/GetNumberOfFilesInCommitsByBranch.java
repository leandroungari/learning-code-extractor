package core.features;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;

import repository.Commit;

public class GetNumberOfFilesInCommitsByBranch implements RepositoryFeature<List<Integer>>{

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
      var repo = createRepository(repository);
    
		  var result = repo
		    .branch(branch)
        .commits();
      
      return result
        .stream()
        .map(this::getNumberOfJavaFilesInCommit)
        .collect(Collectors.toList());
    }
    catch(GitAPIException e) {
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