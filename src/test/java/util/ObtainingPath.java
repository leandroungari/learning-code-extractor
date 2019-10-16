package util;

import org.junit.Assert;
import org.junit.Test;

import util.FileUtil;

public class ObtainingPath {

	@Test
	public void obtainingPath() {
		
		Assert.assertEquals(FileUtil.userFolder(), "/home/leandroungari");
	}
}
