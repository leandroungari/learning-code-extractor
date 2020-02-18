package core.features;

import java.util.List;

import repository.commit.File;

public class GetListOfFiles implements RepositoryFeature<List<File>> {

  private String repo;
  private String branch;
  private String commit;

  @Override
  public Feature<List<File>> setParams(String... params) {

    repo = params[0];
    branch = params[1];
    commit = params[2];

    return this;
  }

  @Override
  public List<File> action() {

    return repository.Commit
      .fromData(repo, branch, commit).allFiles();
  }

}