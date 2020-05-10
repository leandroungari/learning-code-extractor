package ast.snapshot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import core.controller.SettingsController;
import spoon.Launcher;
import spoon.SpoonAPI;
import spoon.reflect.CtModel;
import storage.Storage;
import util.FileUtil;

public class Snapshot {

  private String commit;
  private List<String> files;
  private Storage storage;
  private SpoonAPI api;

  private static SettingsController settings = SettingsController.get();

  public static Snapshot create(String repository, String commit) {
    throw new Error("Not implemented resource!");
  }

  public Snapshot(String commit) {

    this.commit = commit;
    files = new ArrayList<>();
    storage = new Storage(settings.folderOfMetadata() + "/.temp/" + this.commit);

    api = new Launcher();

    var parentFolder = new File(settings.folderOfMetadata() + "/.temp");
    if(parentFolder.exists()) {
      var listOfFolder = FileUtil.listOfFolders(parentFolder);
      if(listOfFolder.size() > 3) {
        var position =  Math.round((float)Math.random()*10) % listOfFolder.size();
        var oldResource = new Storage(settings.folderOfMetadata() + "/.temp/" + listOfFolder.get(position));
        oldResource.remove();
      }
    }
  }

  public void add(String name, String content) {

    try {
      if (!files.contains(name)) {
        storage.plainText(name).write(content);
        files.add(name);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean contains(String filename) {
    
    return files.contains(filename);
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

  public CtModel model() {
    api.addInputResource(settings.folderOfMetadata() + "/.temp/"+ this.commit +"/");
    api.buildModel();
    return api.getModel();
  }
}