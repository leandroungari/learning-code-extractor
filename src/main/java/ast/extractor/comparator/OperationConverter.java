package ast.extractor.comparator;

import java.util.PriorityQueue;

import gumtree.spoon.diff.Diff;

public class OperationConverter {

  /**
   * It transform the operations from Gumtree diff
   * to internal operations
   */
  public PriorityQueue<Operation> convert(Diff diff) {
    var result = new PriorityQueue<Operation>();
    for(var o: diff.getAllOperations()) {
      var operationType = o.getAction().toString();
      if (operationType.contains("INS")) {
        result.add(new Operation(OperationType.INSERT, null, o.getSrcNode()));
      } else if (operationType.contains("DEL")) {
        result.add(new Operation(OperationType.DELETE, o.getSrcNode(), null));
      } else if (operationType.contains("UPD")) {
        result.add(new Operation(OperationType.UPDATE, o.getSrcNode(), o.getDstNode()));
      } else {
        result.add(new Operation(OperationType.MOVE, o.getSrcNode(), o.getDstNode()));
      }
    }
    return result;
  }
}