package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

	public static void createFolder(File file) {
	
		if(!folderExists(file)) file.mkdirs(); 
	}
	
	public static boolean folderExists(File file) {
		
		return file.exists();
	}
	
	public static void createFile(String path, String data) {
		
		try {
      var out = new FileOutputStream(new File(path));
      out.write(data.getBytes());
      out.close();
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
		
	}
	
}
