package ast.snapshot;

import java.io.File;
import java.util.HashMap;

import org.eclipse.jgit.api.errors.GitAPIException;

import persistence.Settings;
import repository.Commit;
import repository.Repository;
import repository.exception.GitRepositoryException;

public class Snapshot {

  private String repositoryName;
  private String branch;
  private Repository repositoryInstance;

  private static Integer LENGTH = 50;
  private static String[] availableCommits = new String[LENGTH];
  private static Integer position = 0;
  private static HashMap<String,CommitSnapshot> commits = new HashMap<>();

  private String base;

  public Snapshot(String repository, String branch) throws GitAPIException {
    this.repositoryName = repository;
    this.branch = branch;
    this.repositoryInstance = createRepository(repository);

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
    var contains = false;
    for(var item: availableCommits) {
      if(commit.equals(item)) {
        contains = true;
        break;
      }
    }

    if(contains) {
      return commits.get(commit);
    }
    else {
      verifyDeletionOfCommit();
      return createSnapshotCommit(commit);
    }
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
    position = (position + 1) % LENGTH;
    return snap;
  }

  /**
   * Libera a posição para a adição
   * de um snapshot de commit
   */
  private void verifyDeletionOfCommit() {
    if(availableCommits[position] != null) {
      var commit = availableCommits[position];
      commits.get(commit).clear();
      commits.remove(commit);
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