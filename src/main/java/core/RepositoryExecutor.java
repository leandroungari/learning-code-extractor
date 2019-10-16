package core;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.data.Branch;
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
	    
	  return repo
	  	.branches()
	  	.stream()
	  	.map(branch -> new Branch(
	  		branch.name(), 
	  		branch.id()
	  	))
	  	.collect(Collectors.toList());
	}
}
