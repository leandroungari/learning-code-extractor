package ast.extractor.comparator;

import java.util.ArrayList;
import java.util.List;

public class HierarchyCombiner {

  HierarchyCombiner() {
    
  }

  public List<Operation> combine(
    List<Operation> operations) {

    var result = new ArrayList<Operation>();
    for(var unsetted: operations) {
      var assigned = false;
      for (var setted : result) {
        if (setted.isParentOf(unsetted) || setted.equals(unsetted)) {
          assigned = true;
          break;
        } else if (setted.isChildOf(unsetted)) {
          replaceElements(result, setted, unsetted);
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

  private static void replaceElements(
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
}