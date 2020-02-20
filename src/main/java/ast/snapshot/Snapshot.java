package ast.snapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import core.controller.SettingsController;
import spoon.Launcher;
import spoon.SpoonAPI;
import spoon.reflect.CtModel;
import storage.Storage;

public class Snapshot {

  private List<String> files;
  private Storage storage;
  private SpoonAPI api;

  private static SettingsController settings = SettingsController.get();

  public Snapshot() {

    files = new ArrayList<>();
    storage = new Storage(settings.folderOfMetadata() + "/.temp");

    api = new Launcher();
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
    api.addInputResource(settings.folderOfMetadata() + "/.temp/src/");
    System.out.println(settings.folderOfMetadata() + "/.temp/src/");
    return api.getModel();
  }
}