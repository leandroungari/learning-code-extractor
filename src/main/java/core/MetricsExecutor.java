package core;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.data.MetricsData;
import core.data.Commit;
import repository.Repository;
import repository.exception.GitRepositoryException;
import repository.metrics.MetricsAnalyzer;

public class MetricsExecutor {

	private Player player;
	private MetricsAnalyzer analyzer;
	
	public MetricsExecutor() {
		
		analyzer = new MetricsAnalyzer();
		player = new Player();
	}
	
	public void analyzeAllFilesInACommit(
		String repository,
		String branch,
		String commit) throws GitRepositoryException {
		
		Repository.create(new File(
			player.repositoriesFolder() + "/"
			+ repository
		));
		
		var data = new MetricsData();
		
		analyzer
		.repositoryName(repository)
		.enableLogs(false)
		.process(commit, (file, result) -> {
			
			data.addFiles(file, result);
		});
		
		Resource
		.create(repository, ResourceType.METRICS_ALL_FILES)
		.folder("commits")
		.name(commit)
		.write(data);
		
	}
	
	public void analyzeDiffFilesInACommit(
		String repository,
		String branch,
		String commit
		) 
		throws GitRepositoryException, 
						GitAPIException {
		
		Repository.create(new File(
			player.repositoriesFolder() + "/"
			+ repository
		));
		
		var data = new MetricsData();
		var diffFiles = player.diffFilesFromCommit(
			repository, 
			branch, 
			commit
		)
		.stream()
		.map(diff -> diff.getNewPath())
		.collect(Collectors.toList());
		
		analyzer
		.repositoryName(repository)
		.enableLogs(false)
		.process(commit, (file, result) -> {
			
			if(diffFiles.contains(file)) {
				data.addFiles(file, result);
			}
		});
		
		Resource
		.create(repository, ResourceType.METRICS_DIFF_FILES)
		.folder("commits")
		.name(commit)
		.write(data);
	}
	
	public void analyzeAllFilesInARangeOfCommits(
		String repository,
		String branch,
		Commit[] rangeOfCommits) 
		throws GitAPIException, 
						GitRepositoryException {
		
		var listOfCommits = Arrays.asList( 
			player.commits(repository, branch)
		);
		
		parallelTasks(listOfCommits, commit -> {
			try {
				analyzeAllFilesInACommit(
					repository, 
					branch, 
					commit.getId().getName()
				);
			} catch (GitRepositoryException e) {
				
				e.printStackTrace();
			}
		}, 4);
	}
	
	public void analyzeDiffFilesInARangeOfCommits(
		String repository,
		String branch,
		Commit[] rangeOfCommits) 
		throws GitAPIException, 
						GitRepositoryException {
		
		var listOfCommits = Arrays.asList( 
			player.commits(repository, branch)
		);
		
		parallelTasks(listOfCommits, commit -> {
			try {
				analyzeDiffFilesInACommit(
					repository, 
					branch, 
					commit.getId().getName()
				);
			} catch (GitRepositoryException
				| GitAPIException e) {
				e.printStackTrace();
			}
		}, 4);
	}
	
	private void parallelTasks(
		List<Commit> list,
		Consumer<Commit> consumer, 
		int numExecutions) {
		
		for(Integer i = 0; i < list.size(); i+= numExecutions) {
	    
	    var resultSet = list
	    .subList(
	      i, 
	      (i+numExecutions < list.size() ? i+numExecutions : list.size())
	    )
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
		
  }
  
  public void statsOfRepository(String repository) 
    throws GitAPIException, GitRepositoryException {

    var branches = player.branches(repository);

    for(var branch: branches) {
      statsOfBranch
    }
  }


	
}
