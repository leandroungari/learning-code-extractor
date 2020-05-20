package ast.extractor;

import java.util.List;

import ast.extractor.elements.Element;

public class SimilarityComparator {

  private static final Double MIN_THRESHOLD = 0.75d;
  private static final Double MAX_THRESHOLD = 0.85d;
  
  private Element<?> source;
  private Element<?> destination;

  public SimilarityComparator() {

  }

  public void setTargets(Element<?> a, Element<?> b) {
    this.source = a;
    this.destination = b;
  }

  public Boolean verify() {
    if(!compareTypes()) return false;

    var sources = source.extractTokens();
    var dests = destination.extractTokens();
    var value = calculate(sources, dests);
    if(Math.max(sources.size(), dests.size()) < 10) {
      return value > MIN_THRESHOLD;
    }
    return value > MAX_THRESHOLD;
  }

  private Boolean compareTypes() {
    return source.getClass() == destination.getClass();
  }

  private Double calculate(List<String> sources, List<String> dests) {
    var minSize = (double) Math.min(sources.size(), dests.size());
    var maxSize = (double) Math.max(sources.size(), dests.size());
    var sizeRatio = minSize/maxSize;
    if(maxSize < 10 && sizeRatio < MIN_THRESHOLD) {
      return sizeRatio;
    } else if(sizeRatio < MAX_THRESHOLD) {
      return sizeRatio;
    }

    var score = 0;
    var previousPosition = -1;
    for(var i = 0; i < sources.size(); i++) {
      var currentPosition = dests.indexOf(sources.get(i));
      if(previousPosition < currentPosition) {
        dests.remove(currentPosition);
        score++;
        previousPosition = currentPosition - 1;
      }
    }

    return score/maxSize;
  }

}