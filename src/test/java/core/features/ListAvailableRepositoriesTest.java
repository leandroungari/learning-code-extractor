package core.features;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import core.controller.SettingsController;
import util.FileUtil;

public class ListAvailableRepositoriesTest {

  private SettingsController settings;

  @Before
  public void before() {
    settings = SettingsController.get();
  }

  @Test
  public void run() {

    var result = FileUtil.listOfFolders(
      settings.folderOfRepositories()
    );

    assertTrue(result != null);
  }
}