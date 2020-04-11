package ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ast.extractor.Changes;

public class CommitChanges implements Mappable {

  private String commitId;
  private List<Changes> result;

  public CommitChanges(String commitId) {
    this.commitId = commitId;
    this.result = new ArrayList<>();
  }

  public void add(Changes changes) {
    result.add(changes);
  }

  @Override
  public String toString() {

    return toMap().toString();
  }

  public JsonElement toMap() {
    var json = new JsonObject();
    json.addProperty("id", commitId);
    var list = new JsonArray();
    for(var change: this.result) {
      list.add(change.toMap());
    }
    json.add("result", list);
    return json;
  }
}