package core.features;

import java.util.List;

import util.FileUtil;

public class ListAvailableRepositories extends Feature<List<String>> {

  @Override
  public Feature<List<String>> setParams(String... params) {
    
    return this;
  }

  @Override
  public List<String> action() {

    return FileUtil.listOfFolders(
      player
      .settings()
      .folderOfRepositories()
    );
  }

}