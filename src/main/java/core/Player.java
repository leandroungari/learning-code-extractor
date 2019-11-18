package core;

import core.executor.MetricsExecutor;
import core.executor.RepositoryExecutor;
import core.executor.SettingsExecutor;
import core.player.MetricsPlayer;
import core.player.RepositoryPlayer;
import core.player.SettingsPlayer;

public class Player {

  protected static RepositoryExecutor repositoryExecutor;
  protected static MetricsExecutor metricsExecutor;
  protected static SettingsExecutor settingsExecutor;

  public Player() {

    settings();
    if(!settingsExecutor.isSet()) {
      settingsExecutor
      .start(SettingsExecutor.USER_FOLDER);
    }
  }

  public RepositoryPlayer repository(String nameOfRepository) {
    if(repositoryExecutor == null) {
      repositoryExecutor = new RepositoryExecutor(this);
    }
    return new RepositoryPlayer(nameOfRepository);
  }

  public MetricsPlayer metrics(String nameOfRepository) {
    if(metricsExecutor == null) {
      metricsExecutor = new MetricsExecutor(this);
    }
    return new MetricsPlayer(nameOfRepository);
  }

  public SettingsPlayer settings() {
    if(settingsExecutor == null) {
      settingsExecutor = new SettingsExecutor();
    }
    return new SettingsPlayer();
  }

  protected Stats stats() {

    return new Stats(this);
  }
}
