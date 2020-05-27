package resource;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JSON implements Storable<JsonObject> {

  @Override
  public String write(JsonObject item) throws IOException {
    return item.toString();
  }

  @Override
  public JsonObject read(String content) throws IOException {
    var gson = new Gson();
    return gson.fromJson(content, JsonObject.class);
  }
}