package core.features;

import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.Feature;
import core.data.DiffFile;
import repository.exception.GitRepositoryException;

public class GetDiffFilesInCommit extends Feature<List<DiffFile>>{

  private String repository;
  private String branch;
  private String commit;

  @Override
  public Feature<List<DiffFile>> setParams(String... params) {
    
    repository = params[0];
    branch = params[1];
    commit = params[2];

    return null;
  }

  @Override
  public List<DiffFile> action() {
    
    try {
      return player
        .repository(repository)
        .diffFilesFromCommit(
          branch, 
          commit
        );
    }
    catch(GitAPIException | GitRepositoryException e) {
      e.printStackTrace();
    }

    return null;
  }

}