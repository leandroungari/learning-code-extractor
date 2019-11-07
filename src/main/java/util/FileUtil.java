package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import core.result.List;

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
	
	public static String readFile(String path) {
	  
	  try {
      var in = new FileInputStream(new File(path));
      var data = new String(in.readAllBytes());
      in.close();
      return data;
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
	  return null;
	}
	
	public static List<String> listOfFolders(File parentFolder) {
	  
	  var list = new List<String>();
	  if(parentFolder.exists() && 
	     parentFolder.isDirectory()) {
	    
	    for(File f: parentFolder.listFiles()) {
	      
	      if(f.isDirectory()) list.add(f.getName());
	    }
	  }
	  
	  return list;
	}
	
	public static String currentFolder() {
		
		return System.getProperty("user.dir");
	}
	
	public static String userFolder() {
		
		return System.getProperty("user.home");
	}
	
	public static void deleteFolder(File file) {
		
		file.delete();
	}
	
}
