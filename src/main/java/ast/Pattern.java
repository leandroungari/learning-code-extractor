package ast;

import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

import ast.extractor.mapping.Mapping;
import core.Jsonify;
import spoon.reflect.declaration.CtElement;

public class Pattern implements Jsonify<Pattern> {

  private String input;
  private String output;

  private List<? extends CtElement> sources;
  private List<? extends CtElement> destinations;

  public Pattern(List<? extends CtElement> src, List<? extends CtElement> dst) {
    this.sources = src;
    this.destinations = dst;

    var sourceItems = sources
      .stream()
      .map(item -> item.toString())
      .collect(Collectors.toList());

    var destinationItems = destinations
      .stream()
      .map(item -> item.toString())
      .collect(Collectors.toList());

    this.input = sourceItems.toString();
    this.output = destinationItems.toString();
  }

  public Pattern() {

  }

  public List<? extends CtElement> destination() {
    return destinations;
  }

  public List<? extends CtElement> source() {
    return sources;
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