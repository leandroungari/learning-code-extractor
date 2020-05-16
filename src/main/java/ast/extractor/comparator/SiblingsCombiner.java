package ast.extractor.comparator;

import java.util.List;

import ast.extractor.mapping.DeleteMapping;
import ast.extractor.mapping.InsertMapping;
import ast.extractor.mapping.Mapping;
import ast.extractor.mapping.UpdateMapping;

public class SiblingsCombiner {

  SiblingsCombiner() {
    
  }
  
  public List<Mapping> combine(List<Mapping> items) {
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
}