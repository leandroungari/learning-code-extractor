package core;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.FileUtil;

public class CreateProjectFolder {

	private SettingsExecutor executor;
	@Before
	public void before() {
		executor = new SettingsExecutor();
	}
	
	@Test
	public void currentFolder() {
		
		executor.start(SettingsExecutor.CURRENT_FOLDER);
		
		assertTrue(FileUtil.folderExists(
			executor.baseFolder()
		));
		
	}
	
	@Test
	public void userFolder() {
		
		executor.start(SettingsExecutor.USER_FOLDER);
		assertTrue(
			FileUtil.folderExists(executor.baseFolder()
		));
	}
	
	@After
	public void after() {
		
		FileUtil.deleteFolder(executor.baseFolder());
		assertTrue(!FileUtil.folderExists(
			executor.baseFolder()
		));
	}
}
