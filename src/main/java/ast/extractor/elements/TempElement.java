package ast.extractor.elements;

import java.util.ArrayList;
import java.util.List;

import spoon.reflect.declaration.CtElement;

public class TempElement extends Element<CtElement> {

  private String commit;

  public TempElement(String path, String commit) {
    super(null);
    this.path = path;
    this.commit = commit;
  }

  public String getCommit() {
    return commit;
  }

  @Override
  public List<? extends CtElement> dependencies() {
    return new ArrayList<>();
  }

  @Override
  public List<String> extractTokens() {
    return new ArrayList<>();
  }
}