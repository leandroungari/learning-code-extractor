package ast.extractor;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.extractor.mapping.DeleteMapping;
import ast.extractor.mapping.InsertMapping;
import ast.extractor.mapping.Mapping;
import ast.extractor.mapping.MoveMapping;
import ast.extractor.mapping.UpdateMapping;
import ast.snapshot.Snapshot;
import core.Jsonify;

public class CommitMappings implements Jsonify<CommitMappings> {

  private String repository;
  private String branch;
  private String commit;
  private List<Mapping> mappings;

  public CommitMappings(String repository, String branch, String commitId) {
    this.commit = commitId;
    this.branch = branch;
    this.repository = repository;
    this.mappings = new ArrayList<>();
  }

  public String getCommit() {
    return commit;
  }

  public CommitMappings() {
    this.mappings = new ArrayList<>();
  }

  public void add(Mapping mapping) {
    mappings.add(mapping);
  }

  public void add(List<Mapping> mappings) {
    this.mappings.addAll(mappings);
  }

  public List<Mapping> get() {
    return mappings;
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
    var array = new JsonArray();
    for (var mapping : mappings) {
      array.add(mapping.toJson());
    }
    json.add("mappings", array);
    return json;
  }

  @Override
  public CommitMappings fromJson(JsonObject content) {
    try {
      var snapshot = new Snapshot(repository, branch);
      var snapCommit = snapshot.commit(commit);
      this.repository = content.get("repository").getAsString();
      this.branch = content.get("branch").getAsString();
      this.commit = content.get("commit").getAsString();
      this.mappings = new ArrayList<>();
      for(var item: content.get("mappings").getAsJsonArray()) {
        var mapping = item.getAsJsonObject();
        var type = mapping.get("type").getAsString();
        if("insert".equals(type)) {
          var insert = new InsertMapping();
          insert.setSnapshot(snapCommit);
          this.mappings.add(insert.fromJson(mapping));
        } else if("delete".equals(type)) {
          var delete = new DeleteMapping();
          delete.setSnapshot(snapCommit);
          this.mappings.add(delete.fromJson(mapping));
        } else if("move".equals(type)) {
          var move = new MoveMapping();
          move.setSnapshot(snapCommit);
          this.mappings.add(move.fromJson(mapping));
        } else {
          var update = new UpdateMapping();
          update.setSnapshot(snapCommit);
          this.mappings.add(update.fromJson(mapping));
        }
      }
      snapshot.clear();
    } catch (GitAPIException e) {
      e.printStackTrace();
    }
    return this;
  }  

}