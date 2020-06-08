package persistence.system;

import java.io.File;

public class Directory {
  
  public String getCurrentFolder() {
		return System.getProperty("user.dir");
	}
	
	public String getUserFolder() {
		
		return System.getProperty("user.home");
  }
  
  public Boolean exists(File file) {
    return file.exists();
  }

  public Boolean mkdir(File file) {
    if(!exists(file)) {
      return file.mkdirs(); 
    }
    return false;
  }
}