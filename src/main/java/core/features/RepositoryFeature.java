package core.features;

import java.io.File;

import core.controller.SettingsController;
import repository.Repository;
import repository.exception.GitRepositoryException;

public interface RepositoryFeature<T> extends Feature<T> {

  final SettingsController settings = SettingsController.get();

  default Repository createRepository(String nameOfRepository) {

    try {
      return Repository.create(new File(
        settings.folderOfRepositories() 
        + "/" + nameOfRepository
      ));
    } catch (GitRepositoryException e) {
      throw new Error("Repository not exists!");
    }
  }

  default Repository createRepository(String nameOfRepository, String url) {

    try {
      return Repository.create(url, new File(
        settings.folderOfRepositories() 
        + "/" + nameOfRepository  
      ));
    } catch (GitRepositoryException e) {
      throw new Error("Repository not exists!");
    }
  }
}