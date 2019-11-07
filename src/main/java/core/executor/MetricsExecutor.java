package core.executor;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.data.MetricsData;
import core.resource.Resource;
import core.resource.ResourceType;
import core.result.Element;
import core.result.List;
import core.result.Map;
import core.Player;
import core.Stats;
import core.data.Commit;
import repository.Repository;
import repository.exception.GitRepositoryException;
import repository.metrics.MetricsAnalyzer;

public class MetricsExecutor {

  private Player player;
  private MetricsAnalyzer analyzer;

  public MetricsExecutor(Player p) {

    analyzer = new MetricsAnalyzer();
    player = p;
  }

  public void analyzeAllFilesInACommit(String repository, String branch, String commit) throws GitRepositoryException {

    Repository.create(new File(player.settings().folderOfRepositories() + "/" + repository));

    var data = new MetricsData();

    analyzer.repositoryName(repository).enableLogs(false).process(commit, (file, result) -> {

      data.addFiles(file, result);
    });

    Resource.create(repository, ResourceType.METRICS_ALL_FILES).folder("commits").name(commit).write(data);

  }

  public void analyzeDiffFilesInACommit(String repository, String branch, String commit)
      throws GitRepositoryException, GitAPIException {

    Repository.create(new File(player.settings().folderOfRepositories() + "/" + repository));

    var data = new MetricsData();
    var diffFiles = player.diffFilesFromCommit(repository, branch, commit).stream().map(diff -> diff.getNewPath())
        .collect(Collectors.toList());

    analyzer.repositoryName(repository).enableLogs(false).process(commit, (file, result) -> {

      if (diffFiles.contains(file)) {
        data.addFiles(file, result);
      }
    });

    Resource.create(repository, ResourceType.METRICS_DIFF_FILES).folder("commits").name(commit).write(data);
  }

  public void analyzeAllFilesInARangeOfCommits(String repository, String branch, Commit[] rangeOfCommits)
      throws GitAPIException, GitRepositoryException {

    var listOfCommits = Arrays.asList(player.commits(repository, branch));

    parallelTasks(listOfCommits, commit -> {
      try {
        analyzeAllFilesInACommit(repository, branch, commit.getId().getName());
      } catch (GitRepositoryException e) {

        e.printStackTrace();
      }
    }, 4);
  }

  public void analyzeDiffFilesInARangeOfCommits(String repository, String branch, Commit[] rangeOfCommits)
      throws GitAPIException, GitRepositoryException {

    var listOfCommits = Arrays.asList(player.commits(repository, branch));

    parallelTasks(listOfCommits, commit -> {
      try {
        analyzeDiffFilesInACommit(repository, branch, commit.getId().getName());
      } catch (GitRepositoryException | GitAPIException e) {
        e.printStackTrace();
      }
    }, 4);
  }

  private void parallelTasks(List<Commit> list, Consumer<Commit> consumer, int numExecutions) {

    for (Integer i = 0; i < list.size(); i += numExecutions) {

      var resultSet = list.subList(i, (i + numExecutions < list.size() ? i + numExecutions : list.size())).stream()
          .map(commit -> CompletableFuture.runAsync(() -> consumer.accept(commit))).collect(Collectors.toList());

      CompletableFuture.allOf(resultSet.toArray()).join();
    }

  }

  public Map<String, Element<Double>> derivateRepository(
    String repository)
      throws GitAPIException, 
      GitRepositoryException {

    var stats = new Stats(player);
    return stats
      .repository(repository)
      .derivate();
  }

  public Map<String, Element<Double>> derivateBranch(String repository, String branchId) {

    var stats = new Stats(player);
    return stats
      .repository(repository)
      .branch(branchId)
      .derivate();
  }


	
}
