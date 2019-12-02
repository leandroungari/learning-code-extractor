package core.executor;

public class MetricsExecutor {

  /*public void analyzeAllFilesInARangeOfCommits(
    String repository, 
    String branch, 
    Commit[] rangeOfCommits)
      throws GitAPIException, GitRepositoryException {

    var listOfCommits = List.of(
      player
      .<Commit[]>get(FeatureType.COMMITS_OF_BRANCH)
      .setParams(repository, branch)
      .action()
    );

    parallelTasks(listOfCommits, commit -> {
      try {
        analyzeAllFilesInACommit(repository, branch, commit.getId().getName());
      } catch (GitRepositoryException e) {

        e.printStackTrace();
      }
    }, 4);
  }

  public void analyzeDiffFilesInARangeOfCommits(
    String repository, 
    String branch, 
    Commit[] rangeOfCommits)
      throws GitAPIException, GitRepositoryException {

    var listOfCommits = List.of(
      player
      .<Commit[]>get(FeatureType.COMMITS_OF_BRANCH)
      .setParams(repository, branch)
      .action()
    );

    parallelTasks(listOfCommits, commit -> {
      try {
        analyzeDiffFilesInACommit(repository, branch, commit.getId().getName());
      } catch (GitRepositoryException | GitAPIException e) {
        e.printStackTrace();
      }
    }, 4);
  }*/

  /*private void parallelTasks(
		List<Commit> list,
		Consumer<Commit> consumer, 
		int numExecutions) {
		
		for(Integer i = 0; i < list.size(); i+= numExecutions) {
	    
      var resultSet = list
        .subList(i,(i+numExecutions < list.size() ? i+numExecutions : list.size()))
	      .stream()
	      .map(commit ->
	        CompletableFuture
	          .runAsync(() -> consumer.accept(commit)) 
	      )
	      .collect(Collectors.toList());
	      
	    CompletableFuture
	      .allOf(resultSet.toArray(
	    	  new CompletableFuture[resultSet.size()]
	      ))
	      .join();
	  }	
  }*/
  
 /* private Map<String,Map<String,Double>> derivateRepository(
    String repository,
    String type)
      throws GitAPIException, GitRepositoryException {

    var stats = new Stats(player);
    return stats
      .repository(repository)
      .derivate(type);
  }

  private Map<String, Map<String,Double>> derivateBranch(
    String repository, 
    String branchId,
    String type) {

    var stats = new Stats(player);
    return stats
      .repository(repository)
      .branch(branchId)
      .derivate(type);
  }

  public Map<String, List<Map<String,Double>>> relevantCommits(
    String repository, 
    String branchId, 
    String metric,
    String type)
      throws GitAPIException, GitRepositoryException {

    var data = new HashMap<String, List<Map<String,Double>>>();

    var average = derivateRepository(repository, type)
      .get(branchId);
      
    var branchData = derivateBranch(repository, branchId, type);
  
    var good = new ArrayList<Map<String,Double>>();
    branchData.forEach((commit,metrics) -> {
      if(metrics.get(metric) < average.get(metric)) {
        good.add(Map.<String,Double>of(commit,metrics.get(metric)));
      }
    });

    good.sort((a,b) -> {
      var firstValue = a.values().toArray(new Double[1])[0];
      var secondValue = b.values().toArray(new Double[1])[0];

      if(firstValue < secondValue) return -1;
      else if(firstValue > secondValue) return 1;
      else return 0;
    });

    var bad = new ArrayList<Map<String,Double>>();
    branchData.forEach((commit,metrics) -> {
      if(metrics.get(metric) > average.get(metric)) {
        bad.add(Map.<String,Double>of(commit,metrics.get(metric)));
      }
    });

    bad.sort((a,b) -> {
      var firstValue = a.values().toArray(new Double[1])[0];
      var secondValue = b.values().toArray(new Double[1])[0];

      if(firstValue < secondValue) return 1;
      else if(firstValue > secondValue) return -1;
      else return 0;
    });

    data.put("good", good);
    data.put("bad", bad);

    return data;
  }*/
}
