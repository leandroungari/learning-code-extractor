package ast.extractor.comparator.siblings;

import ast.extractor.elements.Element;

public class ModelMapping {
  
  private Element<?> source;
  private Element<?> destination;

  private String oldFile;
  private String newFile;

  public ModelMapping(
    String oldFile, 
    Element<?> source, 
    String newFile,
    Element<?> destination) {

    this.oldFile = oldFile;
    this.newFile = newFile;

    this.source = source;
    this.destination = destination;
  }

  public String oldFile() {
    return oldFile;
  }

  public String newFile() {
    return newFile;
  }

  public Element<?> source() {
    return source;
  }

  public Element<?> destination() {
    return destination;
  }
}