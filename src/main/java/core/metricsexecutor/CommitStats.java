package core.metricsexecutor;

import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.Player;
import core.data.MetricsData;
import repository.exception.GitRepositoryException;

public class CommitStats {

  private String repository;
  private String branch;
  private Player player;

  public CommitStats(Player player, String repository, String branch) {
    this.player = player;
    this.repository = repository;
    this.branch = branch;
  }

  public Map<String, Map<String, Double>> derivateMetrics() {

    try {
      
      var commits = player.commits(repository, branch);
      var commitPrev = player.metricsFromAllFilesInACommit(
        repository, branch, commits[commits.length-1].getId().getName()
      );
      var commitForw = player.metricsFromDiffFilesInACommit(
        repository, branch, commits[commits.length-1].getId().getName()
      );

      for(var i = commits.length-2; i >= 0; i--) {

      }

    } catch (GitAPIException | GitRepositoryException e) {
      e.printStackTrace();
    }

    return null;
  }
  //preencher os arquivos não deletados no commit anterior e verificar a implementação na api

  //private MetricsData
}
