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
      if (op.type() == OperationType.INSERT) {
        result.add(new InsertMapping(op.destination()));
      } else if (op.type() == OperationType.DELETE) {
        result.add(new DeleteMapping(op.source()));
      } else if (op.type() == OperationType.UPDATE) {
        result.add(new UpdateMapping(op.source(), op.destination()));
      } else {
        result.add(new MoveMapping(op.source(), op.destination()));
      }
    }
    return result.stream().collect(Collectors.toList());
  }
}