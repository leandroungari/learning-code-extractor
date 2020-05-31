package ast;

import com.google.gson.JsonObject;

import ast.extractor.mapping.Mapping;
import core.Jsonify;

public class Pattern implements Jsonify<Pattern> {

  private String input;
  private String output;

  public Pattern(String in, String out) {
    this.input = in;
    this.output = out;
  }

  public Pattern() {

  }

  public String input() {
    return input;
  }

  public String output() {
    return output;
  }

  public Boolean matches(String before, String after) {
    return input.equals(before) && output.equals(after);
  }

  public Boolean matches(Mapping mapping) {
    var template = new TemplateBuilder<>(mapping);
    var otherPattern = template.model();
    return this.equals(otherPattern);
  }

  @Override
  public boolean equals(Object o) {
    var other = (Pattern) o;
    return this.input().equals(other.input()) 
      && this.output().equals(other.output());
  }

  @Override
  public JsonObject toJson() {
    var result = new JsonObject();
    result.addProperty("input", input);
    result.addProperty("output", output);
    return result;
  }

  @Override
  public Pattern fromJson(JsonObject content) {
    this.input = content.get("input").getAsString();
    this.output = content.get("output").getAsString();
    return this;
  }
}