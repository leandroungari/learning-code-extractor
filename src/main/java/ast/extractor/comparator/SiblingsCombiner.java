package ast.extractor.comparator;

import java.util.List;

import ast.extractor.comparator.siblings.ModelMapping;
import ast.extractor.mapping.DeleteMapping;
import ast.extractor.mapping.InsertMapping;
import ast.extractor.mapping.Mapping;
import ast.extractor.mapping.MoveMapping;
import ast.extractor.mapping.UpdateMapping;

public class SiblingsCombiner {

  private List<Mapping> items;

  SiblingsCombiner() {

  }
  
  public List<Mapping> combine(List<Mapping> items) {
    this.items = items;
    for (var i = 0; i < items.size() - 1; i++) {
      var a = items.get(i);
      for (var j = i + 1; j < items.size(); j++) {
        var b = items.get(j);
        if(verifyInsertAndDeleteElement(a, b)) {
          var map = identifyElements(a, b);

          var pathParentSource = map.source().getParent().getPath().toString();
          var pathParentDestination = map.destination().getParent().getPath().toString();

          Mapping result = null;
          if(pathParentDestination.equals(pathParentSource)) {
            result = createUpdateMapping(map);
          } else {
            var sourceItems = map.source().dependencies().toString();
            var destinationItems = map.destination().dependencies().toString();
            if(sourceItems.equals(destinationItems)) {
              result = createMoveMapping(map);
            } 
          }
          
          if(result != null) {
            this.items.set(i, result);
            this.items.remove(j);
            break;
          }
        }
        
      }
    }
    return items;
  }

  private static Boolean verifyInsertAndDeleteElement(Mapping a, Mapping b) {
    return (a instanceof InsertMapping && b instanceof DeleteMapping) ||
           (b instanceof DeleteMapping && a instanceof InsertMapping);    
  }

  private static ModelMapping identifyElements(Mapping a, Mapping b) {
    if (a instanceof InsertMapping && b instanceof DeleteMapping) {
      return new ModelMapping(
        b.getOldFile(),
        b.source(),
        a.getNewFile(),
        a.destination()
      );
    } else {
      return new ModelMapping(
        a.getOldFile(),
        a.source(),
        b.getNewFile(),
        b.destination()
      );
    }
  }

  private UpdateMapping createUpdateMapping(ModelMapping map) {
    var newMapping = new UpdateMapping(map.source().get(), map.destination().get());
    newMapping.setOldFile(map.oldFile());
    newMapping.setNewFile(map.newFile());
    
    return newMapping;
  }

  private MoveMapping createMoveMapping(ModelMapping map) {
    var newMapping = new MoveMapping(map.source().get(), map.destination().get());
    newMapping.setOldFile(map.oldFile());
    newMapping.setNewFile(map.newFile());
    
    return newMapping;
  }
}