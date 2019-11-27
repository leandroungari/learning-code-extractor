package core.features;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import core.Player;
import util.FileUtil;

public class ListAvailableRepositoriesTest {

  private Player player;

  @Before
  public void before() {
    player = new Player();
  }

  @Test
  public void run() {

    var result = FileUtil.listOfFolders(
      player
      .settings()
      .folderOfRepositories()
    );

    assertTrue(result != null);
  }
}