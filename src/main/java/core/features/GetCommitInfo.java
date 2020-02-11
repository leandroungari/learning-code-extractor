package core.features;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.resource.Resource;
import core.resource.ResourceTrack;
import core.resource.ResourceType;
import core.data.CommitInfo;

public class GetCommitInfo implements RepositoryFeature<CommitInfo> {

  private String repository;
  private String branch;
  private String commit;

  private Resource<CommitInfo[]> resource;
  private ResourceTrack track = new ResourceTrack();

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
      
      resource = track
        .<CommitInfo[]>get(ResourceType.COMMITS_INFO)
        .setParams(repository, branch);

      if(!resource.exists()){
        createResource();
      }
      
    }
    catch(GitAPIException e) {
      e.printStackTrace();
    }
    
    var data = resource.read(CommitInfo[].class);

    return List.of(data).stream()
      .filter(a -> a.getId().equals(commit))
      .collect(Collectors.toList()).get(0);
  }

  public void createResource() throws GitAPIException {

    var repo = createRepository(repository);
      
    var result = new ArrayList<CommitInfo>();

    for(var commit: repo.branch(branch).commits()) {
      
      result.add(commit.info().data());
    }

		resource.write(result.toArray(new CommitInfo[result.size()]));
  }
}