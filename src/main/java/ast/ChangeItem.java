package ast;

import ast.change.Change;

public class ChangeItem {

  private Change<?> source;
  private Change<?> destination;
  private OperationType type;

  public ChangeItem() {

  }

  public ChangeItem source(Change<?> src) {
    source = src;
    return this;
  }

  public ChangeItem destination(Change<?> dst) {
    destination = dst;
    return this;
  }

  public ChangeItem type(OperationType type) {
    this.type = type;
    return this;
  }

  public OperationType type() {
    return type;
  }

  public Change<?> source() {
    return source;
  }

  public Change<?> destination() {
    return destination;
  }
}