package storage;

import java.io.File;

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

  File resource() {

    var folder = new File(this.folder);
    folder.mkdirs();
    return new File(folder + "/" + filename);
  }

  public String file() {
    return filename;
  }
}