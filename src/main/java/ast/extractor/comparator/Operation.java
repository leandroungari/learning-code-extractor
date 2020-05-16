package ast.extractor.comparator;

import spoon.reflect.declaration.CtElement;

public class Operation implements Comparable<Operation> {
  
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

  public void setType(OperationType type) {
    this.type = type;
  }

  public void setSource(CtElement source) {
    this.source = source;
  }

  public void setDestination(CtElement destination) {
    this.destination = destination;
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
        return this.destination() == other.destination();
      } else if(type == OperationType.DELETE) {
        return this.source() == other.source();
      } else {
        return this.destination() == other.destination()
          && this.source() == other.source();
      }
    } catch(NullPointerException ex) {
      return false;
    }
  }

  private static Integer size(CtElement e) {
    return e.getPath().toString().length();
  }

  private Integer getPriority() {
    if(this.type == OperationType.INSERT) {
      return size(destination);
    } else if(this.type == OperationType.DELETE) {
      return size(source);
    } else {
      var a = size(source);
      var b = size(destination);
      return a < b ? a : b;
    }
  }

  @Override
  public int compareTo(Operation other) {
    if(this.getPriority() < other.getPriority()) {
      return -1;
    } else if(this.getPriority() > other.getPriority()) {
      return 1;
    }
    return 0;
  }
}