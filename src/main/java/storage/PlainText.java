package storage;

import java.io.IOException;

import util.FileUtil;

public class PlainText {

  private Storage store;

  PlainText(Storage store) {
    this.store = store;
  }

  public void write(String content) throws IOException {
    System.out.println(store.resource().getAbsolutePath());
    FileUtil.createFile(
      store.resource().getAbsolutePath(), 
      content
    );
  }

  public String read() throws IOException {

    return FileUtil.readFile(
      store.resource().getAbsolutePath()
    );
  }
}