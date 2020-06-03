package ast.template;

import spoon.pattern.Pattern;

public class Template {
  
  private Pattern input;
  private Pattern output;

  public Template(Pattern in, Pattern out) {
    this.input = in;
    this.output = out;
  }

  public Pattern getInput() {
    return input;
  }

  public Pattern getOutput() {
    return output;
  }
}