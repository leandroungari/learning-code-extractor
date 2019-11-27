package core.features;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.features.Feature;
import core.data.Commit;
import repository.exception.GitRepositoryException;

public class GetCommitsByBranch extends Feature<Commit[]> {

  private String repository;
  private String branch;

  public GetCommitsByBranch() {
    super();
  }

  @Override
  public Feature<Commit[]> setParams(String... params) {

    repository = params[0];
    branch = params[1];

    return this;
  }

  @Override
  public Commit[] action() {

    try {
      
      return player
        .repository(repository)
        .commits(branch);
        
    } catch (GitAPIException | GitRepositoryException e) {
      e.printStackTrace();
    }

    return null;
  }



}