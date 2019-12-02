package core.features;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;

import repository.Commit;
import repository.commit.CommitInfo;

public class GetNumberOfContributorsByBranch implements RepositoryFeature<List<Integer>> {

  private String repository;
  private String branch;

  private ArrayList<String> authors;

  public GetNumberOfContributorsByBranch() {
    super();
    authors = new ArrayList<>();
  }

  @Override
  public Feature<List<Integer>> setParams(String... params) {

    repository = params[0];
    branch = params[1];

    return this;
  }

  @Override
  public List<Integer> action() {

    try {
      var repo = createRepository(repository);

      var result = repo
		    .branch(branch)
        .commits();
      
      return result
        .stream()
        .map(this::getNumberOfContributors)
        .collect(Collectors.toList());

    } catch (GitAPIException e) {
      e.printStackTrace();
    }
    
    return null;
  }

  private Integer getNumberOfContributors(Commit commit) {
    
    var identifier = getIdentifierOfCommit(commit);
    if(!authors.contains(identifier)) {
      authors.add(identifier);
    }

    return authors.size();
  }

  private String getIdentifierOfCommit(Commit commit) {

    return player
      .<CommitInfo>get(FeatureType.COMMIT_INFO)
      .setParams(repository, branch, commit.id().getName())
      .action()
      .getEmail();
  }

}