package ast;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ast.extractor.Modification;

public class CommitModifications implements Mappable {

  private String commit;
  private List<Modification> modifications;

  public CommitModifications(String commitId) {
    this.commit = commitId;
    this.modifications = new ArrayList<>();
  }

  public void add(Modification modification) {
    modifications.add(modification);
  }

  public List<Modification> getModifications() {
    return modifications;
  }

  @Override
  public String toString() {
    return toMap().toString();
  }

  public JsonElement toMap() {
    var json = new JsonObject();
    json.addProperty("id", commit);
    var list = new JsonArray();
    for(var change: this.modifications) {
      list.add(change.toMap());
    }
    json.add("result", list);
    return json;
  }
}