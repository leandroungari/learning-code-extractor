package persistence.resource;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import persistence.Jsonify;

public class JSON<T extends Jsonify<T>> implements Storable<T> {

  private T target;

  public JSON(T target) {
    this.target = target;
  }

  @Override
  public String write() {
    return target.toJson().toString();
  }

  @Override
  public T read(String content) {
    var gson = new Gson();
    var data = gson.fromJson(content, JsonObject.class);
    target = target.fromJson(data);
    return target;
  }
}