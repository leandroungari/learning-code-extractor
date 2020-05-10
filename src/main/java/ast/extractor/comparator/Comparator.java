package ast.extractor.comparator;

import java.util.ArrayList;
import java.util.List;

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

  public Comparator(String before, String after) {
    var astComparator = new AstComparator();
    this.diff = astComparator.compare(before, after);
  }

  public Comparator(CtElement before, CtElement after) {
    var astComparator = new AstComparator();
    this.diff = astComparator.compare(before, after);
  }

  /**
   * It generates the mappings based on the provided
   * entites of source code
   * @return List of mappings
   */
  public List<Mapping> generateMappings() {
    var operations = this.extractOperations();
    operations = this.combineHierarchyOperations(operations);

    var mappings = this.createMappings(operations);
    return this.combineSiblingsMappings(mappings);
  }

  private List<Operation> extractOperations() {
    var result = new ArrayList<Operation>();
    for(var o: this.diff.getAllOperations()) {
      var operationType = o.getAction().toString();
      if (operationType.contains("INS")) {
        result.add(new Operation(INSERT, null, o.getSrcNode()));
      } else if (operationType.contains("DEL")) {
        result.add(new Operation(DELETE, o.getSrcNode(), null));
      } else if (operationType.contains("UPD")) {
        result.add(new Operation(UPDATE, o.getSrcNode(), o.getDstNode()));
      } else {
        result.add(new Operation(MOVE, o.getSrcNode(), o.getDstNode()));
      }
    }
    return result;
  }

  private List<Operation> combineHierarchyOperations(
    List<Operation> operations) {
  
    var result = new ArrayList<Operation>();
    for(var unsetted: operations) {
      var assigned = false;
      for(var setted: result) {
        if(setted.isParentOf(unsetted) || setted.equals(unsetted)) {
          assigned = true;
          break;
        } else if(setted.isChildOf(unsetted)) {
          result.set(result.indexOf(setted), unsetted);
          assigned = true;
          break;
        }
      }
      if(!assigned) {
        result.add(unsetted);
      }
    }
    return result;
  }

  private List<Mapping> combineSiblingsMappings(List<Mapping> items) {
    for(var i = 0; i < items.size()-1; i++) {
      var a = items.get(i);
      for(var j = i + 1; j < items.size(); j++) {
        var b = items.get(j);
        if(a instanceof InsertMapping && b instanceof DeleteMapping) {
          var pathParentA = a.destination().getParent().getPath().toString();
          var pathParentB = b.source().getParent().getPath().toString();
          if(pathParentA.equals(pathParentB)) {
            var newMapping = new UpdateMapping(
              b.source().get(), 
              a.destination().get()
            );
            items.set(i, newMapping);
            items.remove(j);
            break;
          }
        } else if(b instanceof InsertMapping && a instanceof DeleteMapping) {
          var pathParentA = a.source().getParent().getPath().toString();
          var pathParentB = b.destination().getParent().getPath().toString();
          if(pathParentB.equals(pathParentA)) {
            var newMapping = new UpdateMapping(
              a.source().get(), 
              b.destination().get()
            );
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
    for(var op: operations) {
      if(op.type() == INSERT) {
        result.add(new InsertMapping(op.destination()));
      } else if(op.type() == DELETE) {
        result.add(new DeleteMapping(op.source()));
      } else if(op.type() == UPDATE) {
        result.add(new UpdateMapping(op.source(), op.destination()));
      } else {
        result.add(new MoveMapping(op.source(), op.destination()));
      }
    }
    return result;
  }
}