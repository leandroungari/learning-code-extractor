package core.features;

import java.io.File;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;

import core.features.Feature;
import core.resource.Resource;
import core.resource.ResourceType;
import core.data.Commit;
import repository.Repository;
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
      
      var resource = Resource.create(
        repository, 
        ResourceType.LIST_OF_COMMITS
      )
      .branch(branch);

      if (!resource.exists()) {
        createResource();
      }

      return resource
        .read(Commit[].class);
        
    } catch (GitAPIException | GitRepositoryException e) {
      e.printStackTrace();
    }

    return null;
  }

  private void createResource() 
    throws GitRepositoryException, 
           GitAPIException {
    
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

}