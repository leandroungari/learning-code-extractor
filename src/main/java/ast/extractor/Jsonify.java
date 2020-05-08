package ast.extractor;

import com.google.gson.JsonObject;

public interface Jsonify<T> {
  
  JsonObject toJson();
  T fromJson(JsonObject content);
}