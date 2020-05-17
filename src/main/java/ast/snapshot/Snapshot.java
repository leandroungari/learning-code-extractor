package ast.snapshot;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.errors.GitAPIException;

import core.controller.SettingsController;
import repository.Repository;
import repository.exception.GitRepositoryException;
import resource.Resource;
import spoon.Launcher;
import spoon.reflect.CtModel;

import resource.PlainText;

public class Snapshot {

  private String commit;
  private String base;

  private static SettingsController settings = SettingsController.get();

  private Snapshot(
    String repository,
    String commit, 
    List<repository.commit.File> diffFiles) {

    this.commit = commit;
    this.base = settings.folderOfMetadata() 
      + "/" + repository
      + "/.temp/" + this.commit + "/";
    this.createFiles(diffFiles);
  }

  public static Snapshot create(String repo, String branch, String commit) 
    throws GitAPIException {

    var repository = createRepository(repo);
    var diffFiles = repository
      .branch(branch)
      .commit(commit)
      .getDiffFiles(".java");

    return new Snapshot(repo, commit, diffFiles);
  }

  private void createFiles(List<repository.commit.File> diffFiles) {
    for (var file : diffFiles) {
      var resource = new Resource<String>(
        this.base + file.folder(), 
        file.filename()
      );
      resource.setContent(file.get());
      try {
        resource.write(new PlainText());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static Repository createRepository(String name) {
    try {
      return Repository
        .create(new File(settings.folderOfRepositories() + "/" + name));
    } catch (GitRepositoryException e) {
      e.printStackTrace();
    }
    return null;
  }

  public CtModel model() {
    var api = new Launcher();
    api.addInputResource(this.base);
    api.buildModel();
    return api.getModel();
  }

  public Boolean clear() {
    try {
      FileUtils.deleteDirectory(new File(this.base));
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }
}