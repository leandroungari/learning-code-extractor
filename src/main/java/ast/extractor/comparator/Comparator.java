package ast.extractor.comparator;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import ast.extractor.mapping.DeleteMapping;
import ast.extractor.mapping.InsertMapping;
import ast.extractor.mapping.Mapping;
import ast.extractor.mapping.MoveMapping;
import ast.extractor.mapping.UpdateMapping;
import gumtree.spoon.AstComparator;
import gumtree.spoon.diff.Diff;
import spoon.reflect.declaration.CtElement;

import static ast.extractor.comparator.OperationType.*;

public class Comparator {

  private Diff diff;

  private OperationConverter converter;

  public Comparator(String before, String after) {
    this.converter = new OperationConverter();

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
    var combined = this.combineHierarchyOperations(operations);

    var mappings = this.createMappings(combined);
    return this.combineSiblingsMappings(mappings);
  }

  private List<Operation> combineHierarchyOperations(PriorityQueue<Operation> operations) {
    var result = new ArrayList<Operation>();
    while (!operations.isEmpty()) {
      var unsetted = operations.poll();
      var assigned = false;
      for (var setted : result) {
        if (setted.isParentOf(unsetted) || setted.equals(unsetted)) {
          assigned = true;
          break;
        } else if (setted.isChildOf(unsetted)) {
          replaceChildElement(result, setted, unsetted);
          assigned = true;
          break;
        }
      }
      if (!assigned) {
        result.add(unsetted);
      }
    }
    return result;
  }

  private static void replaceChildElement(
    List<Operation> result,
    Operation setted,
    Operation unsetted) {

    var current = result.indexOf(setted);
    result.set(current, unsetted);
    for (var i = current + 1; i < result.size();) {
      var item = result.get(i);
      if (item.isChildOf(unsetted)) {
        result.remove(i);
      } else {
        i++;
      }
    }
  }

  private List<Mapping> combineSiblingsMappings(List<Mapping> items) {
    for (var i = 0; i < items.size() - 1; i++) {
      var a = items.get(i);
      for (var j = i + 1; j < items.size(); j++) {
        var b = items.get(j);
        if (a instanceof InsertMapping && b instanceof DeleteMapping) {
          var pathParentA = a.destination().getParent().getPath().toString();
          var pathParentB = b.source().getParent().getPath().toString();
          if (pathParentA.equals(pathParentB)) {
            var newMapping = new UpdateMapping(b.source().get(), a.destination().get());
            items.set(i, newMapping);
            items.remove(j);
            break;
          }
        } else if (b instanceof InsertMapping && a instanceof DeleteMapping) {
          var pathParentA = a.source().getParent().getPath().toString();
          var pathParentB = b.destination().getParent().getPath().toString();
          if (pathParentB.equals(pathParentA)) {
            var newMapping = new UpdateMapping(a.source().get(), b.destination().get());
            items.set(j, newMapping);
            items.remove(i);
            break;
          }
        }
      }
    }
    return items;
  }

  private List<Mapping> createMappings(List<Operation> operations) {
    var result = new ArrayList<Mapping>();
    for (var op : operations) {
      if (op.type() == INSERT) {
        result.add(new InsertMapping(op.destination()));
      } else if (op.type() == DELETE) {
        result.add(new DeleteMapping(op.source()));
      } else if (op.type() == UPDATE) {
        result.add(new UpdateMapping(op.source(), op.destination()));
      } else {
        result.add(new MoveMapping(op.source(), op.destination()));
      }
    }
    return result.stream().collect(Collectors.toList());
  }
}