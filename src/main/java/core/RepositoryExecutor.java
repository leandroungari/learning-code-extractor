package core;

import java.io.File;
import java.util.List;
import org.eclipse.jgit.api.errors.GitAPIException;

import repository.Branch;
import repository.Repository;
import repository.exception.GitRepositoryException;

public class RepositoryExecutor {

	private SettingsExecutor settings;
	
	public RepositoryExecutor() {
		
		settings = new SettingsExecutor();
	}
	
	public List<Branch> branches(String repositoryName) 
		throws GitAPIException, 
						GitRepositoryException {
		
		var repo = Repository.create(new File(
	    settings.repositoriesFolder() + "/" +
	    repositoryName
	  ));
	    
	  return repo.branches();
	}
}
