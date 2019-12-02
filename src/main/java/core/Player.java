package core;

import java.util.HashMap;
import java.util.Map;

import core.executor.MetricsExecutor;
import core.features.DownloadRepository;
import core.features.Feature;
import core.features.FeatureType;
import core.features.GetAverageOfMetricsOfFiles;
import core.features.GetBranches;
import core.features.GetCommitInfo;
import core.features.GetCommitsByBranch;
import core.features.GetDiffFilesInCommit;
import core.features.GetMetricsInACommit;
import core.features.GetMetricsInARangeOfCommits;
import core.features.GetNumberOfContributorsByBranch;
import core.features.GetNumberOfFilesInCommitsByBranch;
import core.features.GetRelevantCommitsOfBranchByMetrics;
import core.features.GetSumOfMetricsOfFiles;
import core.features.ListAvailableRepositories;

public class Player {

  private static Map<FeatureType,Feature<?>> features;

  protected static MetricsExecutor metricsExecutor;

  public Player() {

    initialize();
  }

  public static void initialize() {

    initializeFeatures();
  }

  public static void initializeFeatures() {
    
    if(features == null) {

      features = new HashMap<>();
      add(FeatureType.COMMIT_INFO, new GetCommitInfo());
      add(FeatureType.NUMBER_OF_CONTRIBUTORS_BY_BRANCH, new GetNumberOfContributorsByBranch());
      add(FeatureType.NUMBER_OF_FILES_IN_COMMITS_BY_BRANCH, new GetNumberOfFilesInCommitsByBranch());
      add(FeatureType.BRANCHES_OF_REPOSITORY, new GetBranches());
      add(FeatureType.COMMITS_OF_BRANCH, new GetCommitsByBranch());
      add(FeatureType.DOWNLOAD_REPOSITORY, new DownloadRepository());
      add(FeatureType.DIFF_FILES_IN_COMMIT, new GetDiffFilesInCommit());

      add(FeatureType.LIST_AVAILABLE_REPOSITORIES, new ListAvailableRepositories());
      add(FeatureType.METRICS_IN_COMMIT, new GetMetricsInACommit());
      add(FeatureType.METRICS_IN_RANGE_COMMITS, new GetMetricsInARangeOfCommits());
      add(FeatureType.RELEVANT_COMMITS_BY_METRIC, new GetRelevantCommitsOfBranchByMetrics());

      add(FeatureType.AVERAGE_OF_METRICS_OF_FILES, new GetAverageOfMetricsOfFiles());
      add(FeatureType.SUM_OF_METRICS_OF_FILES, new GetSumOfMetricsOfFiles());
    }
  }

  public static void add(FeatureType name, Feature<?> feature) {
    
    features.put(name, feature);
  }

  @SuppressWarnings("unchecked")
  public <T> Feature<T> get(FeatureType name) {
    
    return (Feature<T>) features.get(name);
  }
}
