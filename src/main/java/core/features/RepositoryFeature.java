package core.features;

import repository.Repository;

public abstract class RepositoryFeature<T> extends Feature<T> {

  protected Repository createRepository(String nameOfRepository) {

  }
}