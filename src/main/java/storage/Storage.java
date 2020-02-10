package storage;

import java.io.File;

import util.FileUtil;

public class Storage {

  private String folder;
  private String filename;

  public Storage(String folder) {
    this.folder = folder;
  }

  public <T extends Storable<T>> CSV<T> csv(String filename) {
    
    this.filename = filename;
    return new CSV<T>(this);
  }

  public <T> JSON<T> json(String filename) {

    this.filename = filename;
    return new JSON<T>(this);
  }

  File resource() {

    var folder = new File(this.folder);
    folder.mkdirs();
    return new File(folder + "/" + filename);
  }

  public boolean exists() {
    
		return FileUtil.folderExists(resource());
	}
}