package ast;

import java.util.List;
import java.util.Map;

import ast.extractor.Mapping;

public class TemplateBuilder<T extends Mapping> {

  private final T mapping;

  private ProcessorTemplate before;
  private ProcessorTemplate after;

  public TemplateBuilder(T mapping) {
    this.mapping = mapping;

    this.before = new ProcessorTemplate(
      this.mapping.source()
    );
    this.after = new ProcessorTemplate(
      this.mapping.destination()
    );
  }

  public ProcessorTemplate before() {
    return before;
  }

  public ProcessorTemplate after() {
    return after;
  }

  public Map<List<String>,List<String>> model() {
    var before = this.before().model();
    var after = this.after().model();
    
    return Map.of(before,after);
  }
}