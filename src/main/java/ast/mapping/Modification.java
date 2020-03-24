package ast.mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import spoon.reflect.declaration.CtElement;

public class Modification implements Iterable<Mapping> {

  private List<Mapping> mappings;

  public Modification() {
    mappings = new ArrayList<>();
  }

  public Modification(Mapping mapping) {
    mappings = new ArrayList<>();
    mappings.add(mapping);
  }

  public Mapping get(Integer position) {
    return mappings.get(position);
  }

  public void add(Mapping mapping) {
    mappings.add(mapping);
  }

  public Mapping first() {
    return mappings.get(0);
  }

  public Mapping last() {
    return mappings.get(mappings.size() - 1);
  }

  public Integer size() {
    return mappings.size();
  }

  public List<CtElement> sources() {
    return mappings
      .stream()
      .map(item -> item.source())
      .collect(Collectors.toList());
  }

  public List<CtElement> destinations() {
    return mappings
      .stream()
      .map(item -> item.destination())
      .collect(Collectors.toList());
  }

  @Override
  public String toString() {

    return "{\n'mappings':" + mappings.toString() + "\n}";
  }

  @Override
  public Iterator<Mapping> iterator() {
    return mappings.iterator();
  }
}