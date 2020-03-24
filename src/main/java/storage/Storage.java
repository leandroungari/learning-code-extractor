package storage;

import java.io.File;
import java.io.IOException;

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

  public PlainText plainText(String filename) {

    this.filename = filename;
    return new PlainText(this);
  }

  File resource() {

    var filename = extractFilename(this.folder + "/" + this.filename);
    var folder = extractFolder(this.folder + "/" + this.filename);

    var instance = new File(folder);
    instance.mkdirs();
    return new File(folder + "/" + filename);
  }

  private String extractFilename(String fullPath) {

    var list = fullPath.split("/");
    return list[list.length - 1];
  }

  private String extractFolder(String fullPath) {

    var list = fullPath.split("/");
    var result = "";
    var i = 0;
    while (i < list.length - 1) {
      result += "/" + list[i];
      i++;
    }
    return result.substring(1);
  }

  public boolean exists() {

    return FileUtil.folderExists(resource());
  }

  public void remove() {

    try {
      FileUtil.deleteFolder(new File(folder));
    } catch (IOException e) {
      e.printStackTrace();
      throw new Error("Unable to delete folder!");
    }
  }
}