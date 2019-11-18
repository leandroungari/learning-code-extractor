package core.player;

import java.io.File;

import core.Player;
import core.executor.SettingsExecutor;

public class SettingsPlayer extends Player {

  public SettingsPlayer() {
    super();

    if(!settingsExecutor.isSet()) {
      settingsExecutor
      .start(SettingsExecutor.USER_FOLDER);
    }
  }

  public File folderOfMetadata() {
    return settingsExecutor
      .folderOfMetadata();
  }

  public File folderOfMetrics() {
    return settingsExecutor
      .folderOfMetrics();
  }

  public File folderOfRepositories() {
    return settingsExecutor
      .folderOfRepositories();
  }
}