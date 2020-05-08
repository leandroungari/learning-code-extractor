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

  public List<? extends Mapping> generateMappings() {
    var operations = this.extractOperations();
    operations = this.combineHierarchyOperations(operations);
    //TODO
    /**
     * Agrupar as operações e 
     * gerar os respectivos mapeamentos
     */
    return this.createMappings(operations);
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

  private List<Operation> combineHierarchyOperations(List<Operation> operations) {
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