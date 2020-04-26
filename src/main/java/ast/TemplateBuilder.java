package ast;

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
}