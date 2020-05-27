package ast.clustering;

public class Pattern {
  
  private String input;
  private String output;

  public Pattern(String in, String out) {
    this.input = in;
    this.output = out;
  }


  public Boolean match(String before, String after) {
    return input.equals(before) 
        && output.equals(after);
  }
}