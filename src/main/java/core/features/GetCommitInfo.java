package core.features;

import java.io.File;

import org.eclipse.jgit.api.errors.GitAPIException;
import core.features.Feature;
import repository.Repository;
import repository.commit.CommitInfo;
import repository.exception.GitRepositoryException;

public class GetCommitInfo extends Feature<CommitInfo> {

  private String repository;
  private String branch;
  private String commit;

  public GetCommitInfo() {
    super();
  }

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
      
      var repo = Repository.create(new File(
		  	player.settings().folderOfRepositories() + "/"
		  	+ repository
		  ));
      
		  return repo
		  	.branch(branch)
		  	.commit(commit)
		  	.info();
    }
    catch(GitRepositoryException | GitAPIException e) {
      e.printStackTrace();
    }
    
    return null;
  }


}