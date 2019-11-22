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
    if(repositoryExecutor == null) {
      repositoryExecutor = new RepositoryExecutor(this);
    }
    if(metricsExecutor == null) {
      metricsExecutor = new MetricsExecutor(this);
    }
    if(settingsExecutor == null) {
      settingsExecutor = new SettingsExecutor();
      if(!settingsExecutor.isSet()) {
        settingsExecutor
        .start(SettingsExecutor.USER_FOLDER);
      }
    }
  }

  public RepositoryPlayer repository(String nameOfRepository) {
    
    return new RepositoryPlayer(nameOfRepository);
  }

  public MetricsPlayer metrics(String nameOfRepository) {
    
    return new MetricsPlayer(nameOfRepository);
  }

  public SettingsPlayer settings() {
    
    return new SettingsPlayer();
  }

  protected Stats stats() {

    return new Stats(this);
  }
}
