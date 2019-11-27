package core.features;

import java.io.File;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;

import core.Feature;
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
		  	.branch(ObjectId.fromString(branch))
		  	.commit(ObjectId.fromString(commit))
		  	.info();
    }
    catch(GitRepositoryException | GitAPIException e) {
      e.printStackTrace();
    }
    
    return null;
  }


}