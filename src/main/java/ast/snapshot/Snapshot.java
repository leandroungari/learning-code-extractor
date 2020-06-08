package ast.snapshot;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;

import persistence.Settings;
import repository.Commit;
import repository.Repository;
import repository.exception.GitRepositoryException;

public class Snapshot {

  private String repositoryName;
  private String branch;
  private Repository repositoryInstance;

  private String[] availableCommits;
  private Integer position;
  private Map<String,CommitSnapshot> commits;

  private String base;

  public Snapshot(String repository, String branch) throws GitAPIException {
    this.repositoryName = repository;
    this.branch = branch;
    this.repositoryInstance = createRepository(repository);
    
    this.availableCommits = new String[10];
    this.position = 0;

    this.commits = new HashMap<>();

    this.base = Settings.get()
      .folderOfMetadata() + "/" + getRepositoryName();
  }

  public String getRepositoryName() {
    return this.repositoryName;
  }

  String base() {
    return this.base;
  }

  public CommitSnapshot commit(String commit) {
    verifyDeletionOfCommit();
    return createSnapshotCommit(commit);
  }

  private CommitSnapshot createSnapshotCommit(String commit) {
    Commit instance = null;
    try {
      instance = repositoryInstance.branch(branch).commit(commit);
    } catch (GitAPIException e) {
      e.printStackTrace();
    }
    var snap = new CommitSnapshot(instance, this);
    commits.put(commit, snap);
    availableCommits[position] = commit;
    position = (position + 1) % 10;
    return snap;
  }

  /**
   * Libera a posição para a adição
   * de um snapshot de commit
   */
  private void verifyDeletionOfCommit() {
    if(availableCommits[position] != null) {
      var commit = availableCommits[position];
      this.commits.remove(commit);
      availableCommits[position] = null;
    }
  }

  private Repository createRepository(String name) {
    try {
      return Repository
        .create(new File(Settings.get().folderOfRepositories() + "/" + name));
    } catch (GitRepositoryException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void clear() {

  }
}