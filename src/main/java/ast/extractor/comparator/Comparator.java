package ast.extractor.comparator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.extractor.mapping.Mapping;
import ast.snapshot.CommitSnapshot;
import ast.snapshot.Snapshot;
import gumtree.spoon.AstComparator;
import persistence.Settings;
import persistence.resource.PlainText;
import persistence.resource.Resource;
import repository.Branch;
import repository.Commit;
import repository.Repository;
import spoon.reflect.declaration.CtElement;

public class Comparator {

  private OperationConverter converter;
  private HierarchyCombiner hierarchy;
  private SiblingsCombiner siblings;
  private MappingsCreator mapper;

  private List<Operation> operations;

  private String repository;
  private Branch instanceBranch;
  private CommitSnapshot currentSnap;
  
  private CommitSnapshot beforeSnap;

  public Comparator(String oldFile, String oldContent, String newFile, String newContent) {
    converter = new OperationConverter(oldFile, newFile);
    hierarchy = new HierarchyCombiner();
    mapper = new MappingsCreator();
    siblings = new SiblingsCombiner();

    var astComparator = new AstComparator();
    operations = converter.convert(astComparator.compare(oldContent, newContent));
  }

  public Comparator(String oldFile, CtElement before, String newFile, CtElement after) {
    converter = new OperationConverter(oldFile, newFile);
    var astComparator = new AstComparator();
    operations = converter.convert(astComparator.compare(oldFile, newFile));
  }

  public Comparator(String repository, String branch, String commit) 
    throws NullPointerException {
      
    hierarchy = new HierarchyCombiner();
    mapper = new MappingsCreator();
    siblings = new SiblingsCombiner();
    operations = new ArrayList<Operation>();

    this.repository = repository;

    try {
      instanceBranch = Repository.create(repository).branch(branch);
    } catch (GitAPIException e) {
      e.printStackTrace();
    }

    try {
      var snapshot = new Snapshot(repository, branch);
      currentSnap = snapshot.commit(commit);
      beforeSnap = currentSnap.previous();
    } catch (GitAPIException e) {
      e.printStackTrace();
    }

    extractOperationFromCommit(commit);
  }

  /*private String transformFileToPath(String filename) {
    var text = filename.replace(".java", "").replace("src/", "").replace("test/", "");

    var items = text.split("/");
    var result = "";
    for (var i = 0; i < items.length - 1; i++) {
      result += "#subPackage[name=" + items[i] + "]";
    }

    return result + "#containedType[name=" + items[items.length - 1] + "]";
  }*/

  private String readFile(String commit, String filename) {
    var items = filename.split("/");
    var folder = String.join("/", List.of(items).subList(0, items.length - 1));

    var name = items[items.length - 1];
    var path =  Settings.get().folderOfMetadata() + "/" + 
      repository + "/snapshots/" + commit + "/" + folder;
    
    System.out.println("> read snapshot " + path + "/" + name);
    var resource = new Resource(path, name);

    try {
      return resource.read(new PlainText(new String()));
    } catch (IOException e) {
      //e.printStackTrace();
      return "";
    }
  }

  private String getContentOfFile(Commit commit, String filename) {
    /*var file = commit.getFile(filename);
    if(file != null) {
      return file.get();
    }
    return "";*/
    if("/dev/null".equals(filename)) {
      return "";
    }

    return readFile(commit.id().getName(), filename);
    
    /*var path = transformFileToPath(filename);
    CtElement element = null;
    if(commit.id().getName().equals(currentSnap.getCommitId())){
      element = currentSnap.model(path);
    } else {
      element = beforeSnap.model(path);
    }

    if(element != null) {
      return element.toString();
    } else {
      System.err.println("> File not found " + filename);
      return "";
    }*/
  }

  private void extractOperationFromCommit(String commit) {
    var current = instanceBranch.commit(commit);
    var before = current.before();

    var diffFiles = current.getDiffEntries(".java");
    for(var file: diffFiles) {
      var oldContent = getContentOfFile(before, file.get("source"));
      var newContent = getContentOfFile(current, file.get("destination"));
      
      var comparator = new Comparator(
        file.get("source"), 
        oldContent, 
        file.get("destination"),
        newContent
      );

      this.operations.addAll(comparator.operations);
    }
  }

  /**
   * It generates the mappings based on the provided entites of source code
   * 
   * @return List of mappings
   */
  public List<Mapping> generateMappings() {
    var combined = hierarchy.combine(operations);
    var mappings = mapper.create(combined);
    return siblings.combine(mappings);
  }
}