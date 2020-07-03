package metrics;

import java.util.ArrayList;
import java.util.List;

import com.github.mauricioaniche.ck.CKClassResult;

import org.eclipse.jgit.api.errors.GitAPIException;

import persistence.Settings;
import persistence.resource.Resource;
import repository.Commit;
import repository.Repository;
import repository.metrics.MetricsAnalyzer;

public class QualityMetricsExtractor {

  private String repository;
  private String branch;
  private Integer from;
  private Integer to;

  private QualityMetricsPersistor persistor;

  private static MetricsAnalyzer analyzer;

  private String base;
  
  public QualityMetricsExtractor(String repository, String branch) {
    this.repository = repository;
    this.branch = branch;

    this.persistor = new QualityMetricsPersistor(repository, branch);

    analyzer = new MetricsAnalyzer();
    this.base = Settings.get().folderOfMetadata().getAbsolutePath();
  }

  public QualityMetricsExtractor(String repository, String branch, Integer from, Integer to) {
    this.repository = repository;
    this.branch = branch;
    this.from = from;
    this.to = to;

    this.persistor = new QualityMetricsPersistor(repository, branch);

    analyzer = new MetricsAnalyzer();
    this.base = Settings.get().folderOfMetadata().getAbsolutePath();
  }

  public void extract() {
    var counter = 0;
    for (var commit : getCommits()) {

      System.err.println("> " + counter + " - " + commit.id().getName());
      counter++;
      var resource = new Resource(base + "/" + repository + "/metrics", commit.id().getName() + ".csv");
      if(!resource.exists()) {
        var metrics = this.analyze(repository, commit);
        var item = new CommitMetrics(metrics);
        persistor.write(item, commit.id().getName());
      }
    }
  }

  private List<FileMetrics> analyze(String repository, Commit commit) {
    
    var data = new ArrayList<FileMetrics>();
    
    try {
      analyzer.repository(this.createRepository())
      .enableLogs(false)
      .process(commit, (file, result) -> {
        var metrics = this.createFile(file, result);        
	      data.add(metrics);
      });
    } catch(Exception ex) {
      System.err.println("> falha na leitura do repositório");
    }
    
    return data;
  }

  private FileMetrics createFile(String file, CKClassResult result) {

    var metrics = new FileMetrics();
		metrics.setFilename(file);
		metrics.setCbo((double) result.getCbo());
		metrics.setDit((double) result.getDit());
		metrics.setLcom((double) result.getLcom());
		metrics.setLoc((double) result.getLoc());
		metrics.setNosi((double) result.getNosi());
		metrics.setNumberOfFields((double) result.getNumberOfFields());
		metrics.setNumberOfMethods((double) result.getNumberOfMethods());
		metrics.setRfc((double) result.getRfc());
    metrics.setWmc((double) result.getWmc());
    
    return metrics;
  }

  private Repository createRepository() {
    return Repository.create(repository);
  }

  private List<Commit> getCommits() {
    var instance = createRepository();
    try {
      if(from == null) {
        return instance
          .branch(branch)
          .commits();
      } else {
        return instance
          .branch(branch)
          .commits(from, to);
      }
    } catch (GitAPIException e) {
      e.printStackTrace();
    }
    return null;
  }
}