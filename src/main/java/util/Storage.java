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
	  System.out.println(path);
	  System.out.println(filename);
	  FileUtil.createFolder(new File(path));
	  FileUtil.createFile(
	    path + "/" + filename, 
	    data
	  );
	}
	
	public String get(String filename) {
	  
	  return FileUtil.readFile(path + "/" + filename);
	}
	
	public boolean exists(String fileOrFolder) {
		return FileUtil.folderExists(new File(path + "/" + fileOrFolder));
	}
}
