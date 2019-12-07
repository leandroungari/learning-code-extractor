package core.features;

import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;

import core.data.Commit;
import core.resource.Resource;
import core.resource.ResourceTrack;
import core.resource.ResourceType;
import repository.exception.GitRepositoryException;

public class GetCommitsByBranch implements RepositoryFeature<Commit[]> {

  private String repository;
  private String branch;

  private Resource resource;

  private ResourceTrack track = new ResourceTrack();

  @Override
  public Feature<Commit[]> setParams(String... params) {

    repository = params[0];
    branch = params[1];

    return this;
  }

  @Override
  public Commit[] action() {

    try {
      
      resource = track
        .get(ResourceType.LIST_OF_COMMITS)
        .setParams(repository, branch);

      if (!resource.exists()) {
        createResource();
      }

      return resource.read(Commit[].class);
        
    } catch (GitAPIException | GitRepositoryException e) {
      e.printStackTrace();
    }

    return null;
  }

  private void createResource() 
    throws GitRepositoryException, 
           GitAPIException {
    
    var repo = createRepository(repository);

		var list = repo
			.branch(branch)
			.commits().stream()
			.map(commit -> new Commit(
				ObjectId.fromString(
					commit.id().getName()
				)
			))
			.collect(Collectors.toList());
		
		resource.write(list);
  }

}