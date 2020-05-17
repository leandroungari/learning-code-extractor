package ast.extractor.comparator;

import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import gumtree.spoon.diff.Diff;

public class OperationConverter {

  private String oldFile;
  private String newFile;

  OperationConverter(String oldFile, String newFile) {
    this.oldFile = oldFile;
    this.newFile = newFile;
  }

  /**
   * It transform the operations from Gumtree diff
   * to internal operations
   */
  public List<Operation> convert(Diff diff) {
    var result = new PriorityQueue<Operation>();
    
    for(var o: diff.getAllOperations()) {
      var operationType = o.getAction().toString();
      Operation operation = null;
      
      if (operationType.contains("INS")) {
        operation = new Operation(OperationType.INSERT, null, o.getSrcNode());
      } else if (operationType.contains("DEL")) {
        operation = new Operation(OperationType.DELETE, o.getSrcNode(), null);
      } else if (operationType.contains("UPD")) {
        operation = new Operation(OperationType.UPDATE, o.getSrcNode(), o.getDstNode());
      } else {
        operation = new Operation(OperationType.MOVE, o.getSrcNode(), o.getDstNode());
      }
      
      operation.setOldFile(this.oldFile);
      operation.setNewFile(this.newFile);

      result.add(operation);
    }
    return result.stream().collect(Collectors.toList());
  }
}