package core.executor;

import java.io.File;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;

import core.Player;
import core.data.Branch;
import core.data.Commit;
import core.data.CommitInfo;
import core.data.DiffFile;
import core.resource.Resource;
import core.resource.ResourceType;
import repository.Repository;
import repository.exception.GitRepositoryException;

public class RepositoryExecutor {

	private Player player;
	
	public RepositoryExecutor(Player p) {
		
		player = p;
	}
	
	public void branches(String repositoryName) 
	    throws GitAPIException, 
					    GitRepositoryException {
		
		var repo = Repository.create(new File(
	    player.settings().folderOfRepositories() + "/" +
	    repositoryName
	  ));
	    
	  var list = repo
	  	.branches()
	  	.stream()
	  	.map(branch -> new Branch(
	  		branch.name(), 
	  		branch.id()
	  	))
	  	.collect(Collectors.toList());
	  
	  Resource
	  .create(
	    repositoryName, 
	    ResourceType.LIST_OF_BRANCHES
	  )
	  .write(list); 
	}
	
	public void commits(
		String repository,
		String branch) 
		throws GitAPIException, 
						GitRepositoryException {
		
		var repo = Repository.create(new File(
			player.settings().folderOfRepositories() + "/"
			+ repository
		));

		var list = repo
			.branch(ObjectId.fromString(branch))
			.commits().stream()
			.map(commit -> new Commit(
				ObjectId.fromString(
					commit.id().getName()
				)
			))
			.collect(Collectors.toList());
		
		Resource
		.create(
			repository, 
			ResourceType.LIST_OF_COMMITS
		)
		.branch(branch)
		.write(list);
	}
	
	public void commitInfo(
		String repository,
		String branch,
		String commit) 
		throws GitAPIException, 
						GitRepositoryException {
	
		var repo = Repository.create(new File(
			player.settings().folderOfRepositories() + "/"
			+ repository
		));
		
		var resource = Resource.create(
			repository, 
			ResourceType.LIST_OF_COMMITS
		)
		.branch(branch);
		
		var listOfCommits = resource
			.read(Commit[].class);
		
		for(int i = 0; i < listOfCommits.length; i++) {
			if(listOfCommits[i].getId()
				.getName().equals(commit) && 
				listOfCommits[i].getInfo() == null
				) {
				
				var info = repo
					.branch(ObjectId.fromString(branch))
					.commit(ObjectId.fromString(commit)).info();
				
				listOfCommits[i].setInfo(new CommitInfo(
					info.getAuthorName(),
					info.getEmail(),
					info.getShortMessage(),
					info.getDate()
				));
				
				break;
			}
		}
		
		resource.write(listOfCommits);
	}
	
	public void diffFiles(
		String repository,
		String branch,
		String commit) 
			throws GitRepositoryException, 
							GitAPIException {
		
		var repo = Repository.create(new File(
			player.settings().folderOfRepositories() + "/" +
			repository
		));
		
		var diffList = repo
			.branch(ObjectId.fromString(branch))
			.commit(ObjectId.fromString(commit))
			.diff();
		
    var result = diffList
      .stream()
			.map(diff -> {
				return new DiffFile(diff.type(), diff.oldPath(), diff.newPath());
      })
      .collect(Collectors.toList());
      		
		var resource = Resource
			.create(repository, ResourceType.DIFF_FILES)
			.branch(branch);
		
		var listOfCommits = resource.read(Commit[].class);
		
		for(var i = 0; i < listOfCommits.length; i++) {
			if(listOfCommits[i]
				.getId()
				.getName()
				.equals(commit)) {

				listOfCommits[i].setDiffFiles(result);
				break;
			}
		}
		
		resource.write(listOfCommits);
	}
	
	public void download(
		String repository, String url) 
			throws GitRepositoryException {
		
		Repository.create(
			url, 
			new File(
				player.settings().folderOfRepositories() + "/" +
				repository
			)
		);
		
	}
}
