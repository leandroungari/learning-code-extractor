package core.features;

import java.util.List;

import repository.commit.File;

public class GetFileContent implements RepositoryFeature<String> {

  private String repository;
  private String branch;
  private String commit;
  private String file;

  @Override
  public Feature<String> setParams(String... params) {
    
    repository = params[0];
    branch = params[1];
    commit = params[2];
    file = params[3];

    return this;
  }

  @Override
  public String action() {
    
    var result = player
      .<List<File>>get(FeatureType.LIST_OF_FILES)
      .setParams(repository,branch,commit)
      .action();

    return result
      .stream()
      .filter(item -> item.fullPath().contains(file))
      .findFirst()
      .get()
      .get();
  }

}