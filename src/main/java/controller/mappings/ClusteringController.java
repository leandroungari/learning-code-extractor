package controller.mappings;

import java.time.Duration;
import java.time.Instant;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.ClusteringEngine;
import ast.snapshot.Snapshot;

public class ClusteringController {

  private String repository;
  private String branch;
  private Integer from;
  private Integer to;

  public ClusteringController(String repository, String branch) throws GitAPIException {
    this.repository = repository;
    this.branch = branch;

    Snapshot.create(repository, branch);
  }

  public ClusteringController(String repository, String branch, Integer from, Integer to) throws GitAPIException {
    this.repository = repository;
    this.branch = branch;
    this.from = from;
    this.to = to;

    Snapshot.create(repository, branch);
  }

  public Boolean clusterToStore() {
    ClusteringEngine engine = null;
    if(from != null && to != null) {
      engine = new ClusteringEngine(repository, branch, from, to);
    } else {
      engine = new ClusteringEngine(repository, branch);
    }
    var start = Instant.now();
    engine.generate();
    var finish = Instant.now();

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
    System.out.println(builder.toString());

    return true;
  }
}