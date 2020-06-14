package ast.extractor.comparator;

import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import ast.extractor.mapping.DeleteMapping;
import ast.extractor.mapping.InsertMapping;
import ast.extractor.mapping.Mapping;
import ast.extractor.mapping.MoveMapping;
import ast.extractor.mapping.UpdateMapping;

public class MappingsCreator {
  
  public List<Mapping> create(List<Operation> operations) {
    var result = new PriorityQueue<Mapping>();
    for (var op : operations) {
      Mapping mapping = null;
      
      if (op.type() == OperationType.INSERT) {
        mapping = new InsertMapping(op.destination());
      } else if (op.type() == OperationType.DELETE) {
        mapping = new DeleteMapping(op.source());
      } else if (op.type() == OperationType.UPDATE) {
        mapping = new UpdateMapping(op.source(), op.destination());
      } else {
        mapping = new MoveMapping(op.source(), op.destination());
      }
      
      mapping.setOldFile(op.getOldFile());
      mapping.setNewFile(op.getNewFile());

      mapping.setOldCommit(op.getOldCommit());
      mapping.setNewCommit(op.getNewCommit());

      result.add(mapping);
    }
    return result.stream().collect(Collectors.toList());
  }
}