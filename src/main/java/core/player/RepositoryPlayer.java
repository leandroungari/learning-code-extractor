package core.player;

import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.Player;
import core.data.Branch;
import core.data.Commit;
import core.data.DiffFile;
import core.resource.Resource;
import core.resource.ResourceType;
import repository.commit.CommitInfo;
import repository.exception.GitRepositoryException;

public class RepositoryPlayer extends Player {

  private String nameOfRepository;

  public RepositoryPlayer(String nameOfRepository) {
    super();
    this.nameOfRepository = nameOfRepository;
  }

  /**
   * It lists the modified files in a particular commit
   * @param repository Name of repository
   * @param branch Branch ID
   * @param commit Commit ID
   * @return List of modified files
   * @throws GitRepositoryException
   * @throws GitAPIException
   */
  public List<DiffFile> diffFilesFromCommit(
    String branch, 
    String commit)
      throws GitRepositoryException, 
             GitAPIException {

    var resource = Resource
    .create(
      nameOfRepository, 
      ResourceType.DIFF_FILES
    )
    .branch(branch);

    repositoryExecutor
    .diffFiles(nameOfRepository, branch, commit);
    
    var listOfCommits = resource.read(Commit[].class);
    Commit commitItem = null;
    for (var i = 0; i < listOfCommits.length; i++) {
      if (commit.equals(listOfCommits[i].getId().getName())) {
        commitItem = listOfCommits[i];
      }
    }

    return commitItem.getDiffFiles();
  }

  public List<Map<String,String>> diffInCommit(
    String branch, 
    String commit) 
      throws GitRepositoryException, 
             GitAPIException {
    
    return repositoryExecutor
      .diffsInCommit(
        nameOfRepository, 
        branch, 
        commit
      );
  }
}