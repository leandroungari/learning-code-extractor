package util;

import java.io.File;

public class Storage {

	private String path;
	
	public Storage(String path) {
		this.path = path;
	}
	
	public Storage folder(String folderName) {
		
		return new Storage(this.path + "/" + folderName);
	}
	
	public void file(String filename, String data) {
	  
	  FileUtil.createFolder(new File(filename));
	  FileUtil.createFile(
	    path + "/" + filename, 
	    data
	  );
	}
}
