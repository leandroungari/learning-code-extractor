package ast.extractor;

import com.google.gson.JsonElement;

import ast.CommitModifications;
import core.resource.Resource;
import core.resource.ResourceTrack;
import core.resource.ResourceType;

public class ModificationsReader {

  private String repository;
  private String commit;

  private Resource<JsonElement> resource;
  private ResourceTrack track;

  public ModificationsReader(
    String repository,
    String commit) {

    this.repository = repository;
    this.commit = commit;

    this.track = new ResourceTrack();
  }

  public CommitModifications read() {

    resource = track
        .<JsonElement>get(ResourceType.MODIFICATIONS)
        .setParams(repository, commit);

    var json = resource.read(JsonElement.class);
    return CommitModifications.create(json.getAsJsonObject());
  }
}