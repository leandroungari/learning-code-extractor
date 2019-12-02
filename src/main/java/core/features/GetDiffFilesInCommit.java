package core.features;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.data.Commit;
import core.data.DiffFile;
import core.resource.Resource;
import core.resource.ResourceType;

public class GetDiffFilesInCommit implements RepositoryFeature<List<DiffFile>>{

  private String repository;
  private String branch;
  private String commit;

  @Override
  public Feature<List<DiffFile>> setParams(String... params) {
    
    repository = params[0];
    branch = params[1];
    commit = params[2];

    return this;
  }

  @Override
  public List<DiffFile> action() {
    
    var resource = Resource
    .create(
      repository, 
      ResourceType.DIFF_FILES
    )
    .branch(branch);

    createResource();
    
    var listOfCommits = resource.read(Commit[].class);
    Commit commitItem = null;
    for (var i = 0; i < listOfCommits.length; i++) {
      if (commit.equals(listOfCommits[i].getId().getName())) {
        commitItem = listOfCommits[i];
      }
    }

    return commitItem.getDiffFiles();
  }

  private void createResource() {
    
    var result = getDiffsFromCommit();
    setDiffFilesInCommit(result);
  }

  private List<DiffFile> getDiffsFromCommit() {
    
    try {
      
      var repo = createRepository(repository);
      var diffList = repo
        .branch(branch)
        .commit(commit)
        .diff();

      return diffList
        .stream()
        .map(diff -> {
          return new DiffFile(diff.type(), diff.oldPath(), diff.newPath());
        })
        .collect(Collectors.toList());
        
    } catch (GitAPIException e) {
      e.printStackTrace();
    }

    return null;
  }

  private void setDiffFilesInCommit(List<DiffFile> diffs) {
    var resource = Resource
			.create(repository, ResourceType.DIFF_FILES)
			.branch(branch);
		
		var listOfCommits = resource.read(Commit[].class);
		
		for(var i = 0; i < listOfCommits.length; i++) {
			if(listOfCommits[i]
				.getId()
				.getName()
				.equals(commit)) {

				listOfCommits[i].setDiffFiles(diffs);
				break;
			}
		}
		
		resource.write(listOfCommits);
  }

}