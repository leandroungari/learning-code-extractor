package ast.extractor.comparator;

import java.util.ArrayList;
import java.util.List;

import ast.extractor.mapping.Mapping;
import gumtree.spoon.AstComparator;
import repository.Commit;
import spoon.reflect.declaration.CtElement;

public class Comparator {

  private OperationConverter converter;
  private HierarchyCombiner hierarchy;
  private SiblingsCombiner siblings;
  private MappingsCreator mapper;

  private List<Operation> operations;

  public Comparator(String oldFile, String oldContent, String newFile, String newContent) {
    converter = new OperationConverter(oldFile, newFile);
    hierarchy = new HierarchyCombiner();
    mapper = new MappingsCreator();
    siblings = new SiblingsCombiner();

    var astComparator = new AstComparator();
    operations = converter.convert(
      astComparator.compare(oldContent, newContent)
    );
  }

  public Comparator(String oldFile, CtElement before, String newFile, CtElement after) {
    converter = new OperationConverter(oldFile, newFile);
    var astComparator = new AstComparator();
    operations = converter.convert(
      astComparator.compare(oldFile, newFile)
    );
  }

  public Comparator(Commit commit) {
    hierarchy = new HierarchyCombiner();
    mapper = new MappingsCreator();
    siblings = new SiblingsCombiner();
    operations = new ArrayList<Operation>();

    extractOperationFromCommit(commit);
  }

  private void extractOperationFromCommit(Commit commit) {
    var diffFiles = commit.getDiffEntries(".java");
    for(var file: diffFiles) {
      var oldContent = commit.before().getFile(file.get("source")).get();
      var newContent = commit.getFile(file.get("destination")).get();

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