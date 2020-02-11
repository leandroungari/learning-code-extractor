package core.features;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.resource.Resource;
import core.resource.ResourceTrack;
import core.resource.ResourceType;
import repository.Commit;
import core.data.CommitInfo;

public class GetNumberOfContributorsByBranch implements RepositoryFeature<List<Integer>> {

  private String repository;
  private String branch;

  private ArrayList<String> authors;

  private Resource<CommitInfo[]> resource;
  private ResourceTrack track = new ResourceTrack();

  public GetNumberOfContributorsByBranch() {
    super();
    authors = null;
  }

  @Override
  public Feature<List<Integer>> setParams(String... params) {

    repository = params[0];
    branch = params[1];

    return this;
  }

  @Override
  public List<Integer> action() {

    var result = new ArrayList<Integer>();
    authors = new ArrayList<>();

    try {

      resource = track
        .<CommitInfo[]>get(ResourceType.COMMITS_INFO)
        .setParams(repository, branch);

      if(!resource.exists()){
        createResource();
      }

      var data = resource.read(CommitInfo[].class);
      for(var info: data) {
        if(!authors.contains(info.getEmail())) {
          authors.add(info.getEmail());
        }
        result.add(authors.size());
      }
    
      return result;

    } catch (GitAPIException e) {
      e.printStackTrace();
    }
    
    return null;
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