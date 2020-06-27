package ast.snapshot;

import java.io.File;
import java.util.HashMap;

import org.eclipse.jgit.api.errors.GitAPIException;

import persistence.Settings;
import repository.Branch;
import repository.Repository;
import repository.exception.GitRepositoryException;

public class Snapshot {

  private String repository;
  private String branch;
  private Branch instance;

  private Integer LENGTH = 5;
  private String[] availableCommits = new String[LENGTH];
  private Integer position = 0;
  private HashMap<String,CommitSnapshot> commits = new HashMap<>();

  private static HashMap<String,Snapshot> snap = new HashMap<>();

  public static Snapshot create(String repo, String branch) throws GitAPIException {
    var novo = new Snapshot(repo, branch);
    snap.put(repo, novo);
    return novo;
  }

  public static Snapshot get(String repo) {
    return snap.get(repo);
  }

  public Branch getBranch() {
    return instance;
  }

  private Snapshot(String repository, String branch) throws GitAPIException {
    this.repository = repository;
    this.branch = branch;
    this.instance = createRepository(this.repository)
      .branch(this.branch);
  }

  String base() {
    return Settings.get().folderOfMetadata() + "/" + repository;
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
      try {
        return createSnapshotCommit(commit);
      } catch(NullPointerException ex) {
        return null;
      }
    }
  }

  private CommitSnapshot createSnapshotCommit(String commit) 
    throws NullPointerException {
      
    var snapCommit = this.instance.commit(commit);
    var snap = new CommitSnapshot(snapCommit, this);
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