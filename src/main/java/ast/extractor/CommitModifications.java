package ast.extractor;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.snapshot.Snapshot;
import core.Jsonify;

public class CommitModifications implements Jsonify<CommitModifications> {

  private String repository;
  private String branch;
  private String commit;
  private List<Modification> modifications;

  public CommitModifications(String repository, String branch, String commitId) {
    this.commit = commitId;
    this.branch = branch;
    this.repository = repository;
    this.modifications = new ArrayList<>();
  }

  public CommitModifications() {
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
    return toJson().toString();
  }

  @Override
  public JsonObject toJson() {
    var json = new JsonObject();
    json.addProperty("repository", repository);
    json.addProperty("branch", branch);
    json.addProperty("commit", commit);
    var list = new JsonArray();
    for (var change : this.modifications) {
      list.add(change.toJson());
    }
    json.add("result", list);
    return json;
  }

  @Override
  public CommitModifications fromJson(JsonObject content) {
    try {
      var snapshot = new Snapshot(repository, branch);
      this.branch = content.get("branch").getAsString();
      this.commit = content.get("commit").getAsString();
      this.modifications = new ArrayList<>();
      for (var item : content.get("result").getAsJsonArray()) {
        var modification = new Modification();
        modification.setSnapshot(snapshot.commit(commit));
        modifications.add(modification.fromJson(item.getAsJsonObject()));
      }
      snapshot.clear();
    } catch (GitAPIException e) {
      e.printStackTrace();
    }
    return this;
  }

}