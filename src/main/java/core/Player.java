package core;

import java.util.HashMap;
import java.util.Map;

import core.executor.MetricsExecutor;
import core.executor.RepositoryExecutor;
import core.executor.SettingsExecutor;
import core.features.FeatureType;
import core.features.GetCommitInfo;
import core.features.GetNumberOfContributorsByBranch;
import core.player.MetricsPlayer;
import core.player.RepositoryPlayer;
import core.player.SettingsPlayer;

public class Player {

  private static Map<FeatureType,Feature<?>> features;

  protected static RepositoryExecutor repositoryExecutor;
  protected static MetricsExecutor metricsExecutor;
  protected static SettingsExecutor settingsExecutor;

  public Player() {

    if (repositoryExecutor == null) {
      repositoryExecutor = new RepositoryExecutor(this);
    }
    if (metricsExecutor == null) {
      metricsExecutor = new MetricsExecutor(this);
    }
    if (settingsExecutor == null) {
      settingsExecutor = new SettingsExecutor();
      if (!settingsExecutor.isSet()) {
        settingsExecutor.start(SettingsExecutor.USER_FOLDER);
      }
    }

    initialize();
  }

  public static void initialize() {

    initializeFeatures();
  }

  public static void initializeFeatures() {
    
    if(features == null) {

      features = new HashMap<>();
      add(FeatureType.COMMIT_INFO, new GetCommitInfo());
      add(FeatureType.NUMBER_OF_CONTRIBUTORS_BY_BRANCH, new GetNumberOfContributorsByBranch());
    }
  }

  public static void add(FeatureType name, Feature<?> feature) {
    
    features.put(name, feature);
  }

  @SuppressWarnings("unchecked")
  public <T> Feature<T> get(FeatureType name) {
    
    return (Feature<T>) features.get(name);
  }

  public RepositoryPlayer repository(final String nameOfRepository) {

    return new RepositoryPlayer(nameOfRepository);
  }

  public MetricsPlayer metrics(final String nameOfRepository) {
    
    return new MetricsPlayer(nameOfRepository);
  }

  public SettingsPlayer settings() {
    
    return new SettingsPlayer();
  }

  protected Stats stats() {

    return new Stats(this);
  }
}
