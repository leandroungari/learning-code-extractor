package core.features;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;
import core.features.Feature;
import repository.Commit;
import repository.Repository;
import repository.commit.CommitInfo;
import repository.exception.GitRepositoryException;

public class GetNumberOfContributorsByBranch extends Feature<List<Integer>> {

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
      var repo = Repository.create(new File(
        player.settings().folderOfRepositories() 
        + "/" + repository
      ));

      var result = repo
		    .branch(branch)
        .commits();
      
      return result
        .stream()
        .map(this::getNumberOfContributors)
        .collect(Collectors.toList());

    } catch (GitRepositoryException | GitAPIException e) {
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