package ast.extractor.comparator;

import java.util.List;

import ast.extractor.mapping.Mapping;
import gumtree.spoon.AstComparator;
import gumtree.spoon.diff.Diff;
import spoon.reflect.declaration.CtElement;

public class Comparator {

  private Diff diff;

  private OperationConverter converter;
  private HierarchyCombiner hierarchy;
  private SiblingsCombiner siblings;
  private MappingsCreator mapper;

  public Comparator(String before, String after) {
    this.converter = new OperationConverter();
    this.hierarchy = new HierarchyCombiner();
    this.mapper = new MappingsCreator();
    this.siblings = new SiblingsCombiner();

    var astComparator = new AstComparator();
    this.diff = astComparator.compare(before, after);
  }

  public Comparator(CtElement before, CtElement after) {
    this.converter = new OperationConverter();
    var astComparator = new AstComparator();
    this.diff = astComparator.compare(before, after);
  }

  /**
   * It generates the mappings based on the provided entites of source code
   * 
   * @return List of mappings
   */
  public List<Mapping> generateMappings() {
    var operations = converter.convert(diff);
    var combined = hierarchy.combine(operations);
    var mappings = mapper.create(combined);
    return siblings.combine(mappings);
  }
}