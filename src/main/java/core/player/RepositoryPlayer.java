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
   * It lists all branches in the specified repository
   * @param repositoryName The name of repository
   * @return List of branches
   * @throws GitAPIException
   * @throws GitRepositoryException
   */
  public Branch[] branches() 
    throws GitAPIException, 
           GitRepositoryException {

    var resource = Resource
    .create(
      nameOfRepository, 
      ResourceType.LIST_OF_BRANCHES
    );

    if (!resource.exists()) {
      repositoryExecutor
      .branches(nameOfRepository);
    }

    return resource.read(Branch[].class);
  }

  /**
   * This method lists the commits in a specified branch
   * @param repository Name of repository
   * @param branch  The ID (hash) of branch
   * @return List of commits
   * @throws GitAPIException
   * @throws GitRepositoryException
   */
  public Commit[] commits(String branch) 
    throws GitAPIException, 
           GitRepositoryException {

    var resource = Resource
    .create(
      nameOfRepository, 
      ResourceType.LIST_OF_COMMITS
    )
    .branch(branch);

    if (!resource.exists()) {
      repositoryExecutor
      .commits(nameOfRepository, branch);
    }

    return resource
      .read(Commit[].class);
  }

  /**
   * It gets some description information of a particular commit
   * @param repository Name of repository
   * @param branch Branch ID
   * @param commit Commit ID
   * @return
   * @throws GitAPIException
   * @throws GitRepositoryException
   */
  public CommitInfo commitInfo(
    String branch, 
    String commit)
      throws GitAPIException, 
             GitRepositoryException {
        
    return repositoryExecutor
      .commitInfo(nameOfRepository, branch, commit);   
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

    if (!resource.exists()) {
      repositoryExecutor
      .commits(nameOfRepository, branch);
    }

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