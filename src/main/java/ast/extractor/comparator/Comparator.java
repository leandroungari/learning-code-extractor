package ast.extractor.comparator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.extractor.mapping.Mapping;
import ast.snapshot.CommitSnapshot;
import ast.snapshot.Snapshot;
import gumtree.spoon.AstComparator;
import repository.Repository;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.path.CtPathBuilder;

public class Comparator {

  private OperationConverter converter;
  private HierarchyCombiner hierarchy;
  private SiblingsCombiner siblings;
  private MappingsCreator mapper;

  private List<Operation> operations;

  public Comparator(String oldFile, String oldContent, String oldCommit, String newFile, String newContent,
      String newCommit) {

    converter = new OperationConverter(oldFile, oldCommit, newFile, newCommit);
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

  public Comparator(String repository, String branch, String commit) {
    hierarchy = new HierarchyCombiner();
    mapper = new MappingsCreator();
    siblings = new SiblingsCombiner();
    operations = new ArrayList<Operation>();

    extractOperationFromCommit(repository, branch, commit);
  }

  private String getContentOfFile(CommitSnapshot snap, String filename) {
    CtElement element = null;
    try {
      var path = transformFilenameToPath(filename);
      element = snap.model(path);
    } catch(Exception ex) {
      //elemento não identificado
    }
    
    if (element != null) {
      return element.toString();
    }
    return "";
  }

  private void extractOperationFromCommit(
    String repository, 
    String branch, 
    String commit) {

    try {
      var snapshot = new Snapshot(repository, branch);
      var currentSnap = snapshot.commit(commit);
      var beforeSnap = currentSnap.previous();

      var currentCommit = Repository
        .create(repository)
        .branch(branch)
        .commit(commit);

      var diffFiles = currentCommit.getDiffEntries(".java");
      for(var file: diffFiles) {
        
        var oldContent = getContentOfFile(beforeSnap, file.get("source"));
        var newContent = getContentOfFile(currentSnap, file.get("destination"));

        var comparator = new Comparator(
          file.get("source"), 
          oldContent, 
          beforeSnap.getCommitId(),
          file.get("destination"),
          newContent,
          currentSnap.getCommitId()
        );

        this.operations.addAll(comparator.operations);
      }
    } catch (GitAPIException e) {
      e.printStackTrace();
    }
  }

  private String transformFilenameToPath(String filename) {
    var items = filename.replace(".java", "").trim().split("/");
    var builder = new CtPathBuilder();
    for(var i = 0; i < items.length; i++) {
      builder.name(items[i]);
    }
    
    //builder.ty-(CtRole.TYPE, items);
    var result = builder.build().toString();
    System.out.println(result);
    return result;
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