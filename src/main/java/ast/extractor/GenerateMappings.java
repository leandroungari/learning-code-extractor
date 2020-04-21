package ast.extractor;

import java.util.ArrayList;
import java.util.List;

import ast.extractor.mapping.DeleteMapping;
import ast.extractor.mapping.InsertMapping;
import ast.extractor.mapping.MoveMapping;
import ast.extractor.mapping.UpdateMapping;
import gumtree.spoon.diff.Diff;
import gumtree.spoon.diff.operations.Operation;

public class GenerateMappings {

  private Diff diff;

  GenerateMappings(Diff diff) {
    this.diff = diff;
  }

  public List<Mapping> create() {

    var result = new ArrayList<Mapping>();
    
    for (Operation<?> o : diff.getAllOperations()) {
      var operationType = o.getAction().toString();

			if (operationType.contains("INS")) {
        result.add(new InsertMapping(o.getSrcNode()));
      } else if (operationType.contains("DEL")) {
        result.add(new DeleteMapping(o.getSrcNode()));
      } else if (operationType.contains("UPD")) {
        result.add(new UpdateMapping(o.getSrcNode(), o.getDstNode()));
      } else {
        result.add(new MoveMapping(o.getSrcNode(), o.getDstNode()));
      }
    }

    return result;
  }
}