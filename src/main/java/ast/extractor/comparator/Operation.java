package ast.extractor.comparator;

import spoon.reflect.declaration.CtElement;

public class Operation {
  
  private OperationType type;
  private CtElement source;
  private CtElement destination;

  public Operation(
    OperationType type, 
    CtElement source, 
    CtElement destination) {

    this.type = type;
    this.source = source;
    this.destination = destination;
  }

  public CtElement source() {
    return source;
  }

  public CtElement destination() {
    return destination;
  }

  public OperationType type() {
    return type;
  }

  public Boolean isParentOf(Operation other) {
    try {
      if(this.type == OperationType.INSERT) {
        return other.destination().hasParent(this.destination());
      } else if(type == OperationType.DELETE) {
        return other.source().hasParent(this.source());
      } else {
        return other.destination().hasParent(this.destination())
          && other.source().hasParent(this.source());
      }
    } catch(NullPointerException ex) {
      return false;
    }
  }

  public Boolean isChildOf(Operation other) {
    try {
      if(this.type == OperationType.INSERT) {
        return this.destination().hasParent(other.destination());
      } else if(type == OperationType.DELETE) {
        return this.source().hasParent(other.source());
      } else {
        return this.destination().hasParent(other.destination())
          && this.source().hasParent(other.source());
      }
    } catch(NullPointerException ex) {
      return false;
    }
  }

  @Override
  public boolean equals(Object obj) {
    var other = (Operation) obj;
    try {
      if(this.type == OperationType.INSERT) {
        return this.destination().hasParent(other.destination());
      } else if(type == OperationType.DELETE) {
        return this.source().hasParent(other.source());
      } else {
        return this.destination().hasParent(other.destination())
          && this.source().hasParent(other.source());
      }
    } catch(NullPointerException ex) {
      return false;
    }
  }
}