package util;

import java.time.Duration;
import java.time.Instant;

public class Timer {

  private Instant start;
  private Instant finish;

  public Timer() {

  }

  public void start() {
    this.start = Instant.now();
  }

  public void finish() {
    this.finish = Instant.now();
  }

  public void show() {
    if (this.start != null && this.finish != null) {
      var timeElapsed = Duration.between(start, finish);
      var builder = new StringBuilder("Executed in: ");
      builder.append(timeElapsed.toDaysPart());
      builder.append("d ");
      builder.append(timeElapsed.toHoursPart());
      builder.append("h ");
      builder.append(timeElapsed.toMinutesPart());
      builder.append("m ");
      builder.append(timeElapsed.toSecondsPart());
      builder.append("s");
      var result = builder.toString();
      System.out.println(result);
      System.err.println(result);
    }
  }

}