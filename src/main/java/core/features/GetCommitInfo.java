package core.features;

import org.eclipse.jgit.api.errors.GitAPIException;

import repository.commit.CommitInfo;

public class GetCommitInfo implements RepositoryFeature<CommitInfo> {

  private String repository;
  private String branch;
  private String commit;

  @Override
  public Feature<CommitInfo> setParams(String... params) {
    
    repository = params[0];
    branch = params[1];
    commit = params[2];

    return this;
  }

  @Override
  public CommitInfo action() {
    
    try {
      
      var repo = createRepository(repository);
      
		  return repo
		  	.branch(branch)
		  	.commit(commit)
		  	.info();
    }
    catch(GitAPIException e) {
      e.printStackTrace();
    }
    
    return null;
  }


}