package ast.snapshot;

import java.io.IOException;

import repository.Commit;
import persistence.resource.PlainText;
import persistence.resource.Resource;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.path.CtPathStringBuilder;

public class CommitSnapshot {

  private Snapshot parent;
  private Commit current;
  private CtModel model;
  private String base;

  CommitSnapshot(Commit commit, Snapshot parent) {
    this.parent = parent;
    this.current = commit;
    
    this.base = parent.base() +
      "/snapshots/" + current.id().getName();
    
    this.model = createModel();
  }

  public String getCommitId() {
    return current.id().getName();
  }

  public Commit commit() {
    return current;
  }

  private CtModel createModel() {

    for (var file : current.allFilesByExtension(".java")) {
      createFileInTempCommit(
        base + "/" + file.folder(), 
        file.filename(), 
        file.get()
      );
    }

    var launcher = new Launcher();
    launcher.addInputResource(this.base);
    launcher.buildModel();
    return launcher.getModel();
  }

  private void createFileInTempCommit(
    String folder, 
    String name, 
    String content) {

    var resource = new Resource(folder, name);
    try {
      resource.write(new PlainText(content));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public CtElement model(String path) {
    if("#undefined".equals(path)) return null;

    var builder = new CtPathStringBuilder()
      .fromString(path);

    var packages = this.model.getAllPackages();

    var items = builder
      .evaluateOn(packages.toArray(new CtPackage[packages.size()]));

    if(items.size() > 0) {
      return items.get(0);
    } else {
      return null;
    }
  }

  public CtPackage root() {
    return this.model.getRootPackage();
  }

  public CommitSnapshot previous() {
    return parent.commit(
      current
      .before()
      .id().getName()
    );
  }

  public void clear() {
    var resource = new Resource(this.base);
    resource.delete();
  }
}