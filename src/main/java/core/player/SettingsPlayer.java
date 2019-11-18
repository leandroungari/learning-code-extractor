package core.player;

import java.io.File;

import core.Player;

public class SettingsPlayer extends Player {

  public SettingsPlayer() {
    super();
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