package ast.snapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import core.controller.SettingsController;
import storage.Storage;

public class Snapshot {

  private List<String> files;
  private Storage storage;

  public Snapshot() {

    var settings = SettingsController.get();

    files = new ArrayList<>();
    storage = new Storage(settings.folderOfMetadata() + "/.temp");
  }

  public void add(String name, String content) {

    try {
      if(!files.contains(name)) {
        storage.plainText(name).write(content);
        files.add(name);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String get(String name) {

    try {
      return storage.plainText(name).read();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void clear() {
    
    storage.remove();
  }
}