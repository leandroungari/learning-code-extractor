package core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.executor.SettingsExecutor;
import util.FileUtil;

public class CreateProjectFolder {

	private Player player;
	@Before
	public void before() {
		player = new Player();
	}
	
	@Test
	public void currentFolder() {
		
    /*player
    .settings()
    .start(SettingsExecutor.CURRENT_FOLDER);
		
		assertTrue(FileUtil.folderExists(
			player.settings().base()
		));
		*/
	}
	
	@Test
	public void userFolder() {
		
		/*player.settings().start(SettingsExecutor.USER_FOLDER);
		assertTrue(FileUtil.folderExists(
      player.settings().base()
		));*/
	}
	
	@After
	public void after() {
		
		/*FileUtil.deleteFolder(player.settings().base());
		assertFalse(FileUtil.folderExists(
      player.settings().base()
		));*/
	}
}
