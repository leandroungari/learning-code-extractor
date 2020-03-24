package ast.mapping;

import java.util.ArrayList;
import java.util.List;

public class Modification {
  
  private List<Mapping> mappings;

  public Modification() {
    mappings = new ArrayList<>();
  }

  public Modification(Mapping mapping) {
    mappings = new ArrayList<>();
    mappings.add(mapping);
  }

  public List<Mapping> getMappings() {
    return mappings;
  }

  @Override
  public String toString() {

    return "{\n'mappings':" + mappings.toString() + "\n}";
  }
}