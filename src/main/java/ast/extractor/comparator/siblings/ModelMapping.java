package ast.extractor.comparator.siblings;

import ast.extractor.elements.Element;

public class ModelMapping {
  
  private Element<?> source;
  private Element<?> destination;

  private String oldFile;
  private String newFile;

  private String oldCommit;
  private String newCommit;

  public ModelMapping(
    String oldFile, 
    Element<?> source, 
    String newFile,
    Element<?> destination,
    String oldCommit,
    String newCommit) {

    this.oldFile = oldFile;
    this.newFile = newFile;

    this.source = source;
    this.destination = destination;

    this.oldCommit = oldCommit;
    this.newCommit = newCommit;
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

  public String getOldCommit() {
    return oldCommit;
  }

  public String getNewCommit() {
    return newCommit;
  }
}