package core.features;

import ast.CommitModifications;
import ast.Extractor;
import core.data.Commit;
import core.resource.Resource;
import core.resource.ResourceTrack;
import core.resource.ResourceType;


public class ExtractModifications implements RepositoryFeature<Void> {

  private String repository;
  private String branch;

  private Extractor extractor = null;

  private Resource<CommitModifications> resource;

  private ResourceTrack track = new ResourceTrack();

  @Override
  public Feature<Void> setParams(String... params) {
    this.repository = params[0];
    this.branch = params[1];
    return this;
  }

  @Override
  public Void action() {
    
    var commits = commitsFromBranch();
    for(var i = 1; i < commits.length; i++) {
      var commit = commits[i].getId();

      this.extractor = new Extractor(repository, branch, commit);
      var result = this.extractor.transformModifications(); 
      
      resource = track
        .<CommitModifications>get(ResourceType.MODIFICATIONS)
        .setParams(repository, commit);

      resource.write(result);
    }

    return null;
  }

  private Commit[] commitsFromBranch() {
    return player
      .<Commit[]>get(FeatureType.COMMITS_OF_BRANCH)
      .setParams(repository, branch)
      .action();
  }
}