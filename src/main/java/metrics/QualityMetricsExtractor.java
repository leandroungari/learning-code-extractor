package metrics;

import java.util.ArrayList;
import java.util.List;

import com.github.mauricioaniche.ck.CKClassResult;

import org.eclipse.jgit.api.errors.GitAPIException;

import repository.Commit;
import repository.Repository;
import repository.metrics.MetricsAnalyzer;

public class QualityMetricsExtractor {

  private String repository;
  private String branch;

  private QualityMetricsPersistor persistor;

  private static MetricsAnalyzer analyzer;
  
  public QualityMetricsExtractor(String repository, String branch) {
    this.repository = repository;
    this.branch = branch;

    this.persistor = new QualityMetricsPersistor(repository, branch);

    analyzer = new MetricsAnalyzer();
  }

  public void extract() {
    for (var commit : getCommits()) {
      
    }
  }

  private List<FileMetrics> analyze(String repository, Commit commit) {
    
    var data = new ArrayList<FileMetrics>();
    
    analyzer.repository(this.createRepository())
    .enableLogs(false)
    .process(commit, (file, result) -> {
      var metrics = this.createFile(file, result);        
	    data.add(metrics);
    });
    
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
      var items = instance
        .branch(branch)
        .commits();

      items.remove(0);
      return items;
    } catch (GitAPIException e) {
      e.printStackTrace();
    }
    return null;
  }
}