package core;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.data.Branch;
import core.data.Commit;
import core.data.CommitInfo;
import repository.exception.GitRepositoryException;

public class Player {
  
  private RepositoryExecutor exec;
  
  public Player() {
    exec = new RepositoryExecutor();
  }
	
	public Branch[] branches(
	  String repositoryName
	) throws GitAPIException, 
	          GitRepositoryException {
		
		var resource = Resource.create(
		  repositoryName, 
		  ResourceType.LIST_OF_BRANCHES
		);
		
		if(!resource.exists()) {
		  exec.branches(repositoryName);
		}
		
		return resource.read(Branch[].class);
	}
	
	public Commit[] commits(
		String repository,
		String branch) 
		throws GitAPIException, 
						GitRepositoryException {
		
		var resource = Resource.create(
			repository, 
			ResourceType.LIST_OF_COMMITS
		)
		.branch(branch);
		
		if(!resource.exists()) {
			exec.commits(repository, branch);
		}
		
		return resource.read(Commit[].class);
	}
	
	public CommitInfo commitInfo(
		String repository,
		String branch,
		String commit) 
		throws GitAPIException, 
						GitRepositoryException {
		
		var resource = Resource.create(
			repository, 
			ResourceType.COMMIT_INFO
		)
		.branch(branch);
		
		if(!resource.exists()) {
			exec.commits(repository, branch);
		}
		
		var listOfCommits = resource
			.read(Commit[].class);
		
		for(int i = 0; i < listOfCommits.length; i++) {
			if(commit.equals(
				listOfCommits[i]
				.getId()
				.getName())) {
				
				return listOfCommits[i].getInfo();
			}
		}
		
		return null;
	}
}
