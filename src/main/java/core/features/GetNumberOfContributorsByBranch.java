package core.features;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;

import core.Feature;
import core.Player;
import repository.Commit;
import repository.Repository;
import repository.commit.CommitInfo;
import repository.exception.GitRepositoryException;

public class GetNumberOfContributorsByBranch implements Feature<List<Integer>> {

  private Player player;

  private String repository;
  private String branch;

  private ArrayList<String> authors;

  public GetNumberOfContributorsByBranch() {
    player = new Player();
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
		    .branch(ObjectId.fromString(branch))
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