package ast.snapshot;

import java.io.IOException;

import ast.snapshot.exception.NotFoundElementInSnapshotException;
import repository.Commit;
import repository.commit.File;
import persistence.resource.PlainText;
import persistence.resource.Resource;
import spoon.Launcher;
import spoon.MavenLauncher;
//import spoon.MavenLauncher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.path.CtPathStringBuilder;

public class CommitSnapshot {

  private Snapshot parent;
  private Commit current;
  private CtModel model;
  private String base;

  private String currentPath;

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

  private void writeTempFile(File file) {
    createFileInTempCommit(
      base + "/" + file.folder(), 
      file.filename(), 
      file.get()
    );
  }

  private void generateFiles() {
    //for (var file : current.allFilesByExtension(".java")) {
    for (var file : current.allFiles()) {
      writeTempFile(file);
    }
  }

  private CtModel createMavenProject() {
    var launcher = new MavenLauncher(this.base, MavenLauncher.SOURCE_TYPE.APP_SOURCE);
    launcher.buildModel();
    System.out.println("> It's a Maven Project");
    return launcher.getModel();
  }

  private CtModel createSimpleProject() {
    var launcher = new Launcher();
    launcher.addInputResource(this.base);
    //Para permitir arquivos duplicados esse método é interessante
    //launcher.getEnvironment().isIgnoreDuplicateDeclarations()
    launcher.getEnvironment().setIgnoreDuplicateDeclarations(true);
    launcher.getEnvironment().setComplianceLevel(11);
    launcher.buildModel();
    System.out.println("> It's a Simple Project");
    return launcher.getModel();
  }

  private CtModel createModel() {

    generateFiles();
    CtModel model = null;
    try {
      model = createMavenProject();
    } catch(Exception ex) {
      model = createSimpleProject();
    }
    return model;
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
    
    this.currentPath = path;

    var builder = new CtPathStringBuilder()
      .fromString(path);

    var packages = this.model.getAllPackages();
    System.out.println("> getting element " + path + " in " + getCommitId());
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
    
    try {
      return parent.commit(
        current
        .before()
        .id().getName()
      ); 
    } catch(Exception ex) {
      throw new NotFoundElementInSnapshotException(getCommitId(), currentPath);
    }
  }

  public void clear() {
    var resource = new Resource(this.base);
    resource.delete();
  }
}