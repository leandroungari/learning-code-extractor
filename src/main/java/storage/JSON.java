package storage;

import java.io.IOException;

import com.google.gson.Gson;

import util.FileUtil;

public class JSON<T> {

  private Storage store;

  JSON(Storage store) {

    this.store = store;
  }

  public void write(T item) throws IOException {

    var gson = new Gson();
    var data = gson.toJson(item);

    FileUtil.createFile(store.resource().getAbsolutePath(), data);
  }

  public T read(Class<T> type) throws IOException {

    var gson = new Gson();
    var data = FileUtil.readFile(store.resource().getAbsolutePath());

    return gson.fromJson(data, type);
  }
}