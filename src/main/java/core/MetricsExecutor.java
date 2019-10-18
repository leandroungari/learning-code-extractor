package core;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.data.MetricsData;
import core.data.Commit;
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
		String commit) {
		
		var data = new MetricsData();
		
		analyzer
		.enableLogs(false)
		.process(commit, (file, result) -> {
			
			data.addFiles(file, result);
		});
		
		Resource
		.create(repository, ResourceType.METRICS_ALL_FILES)
		.folder("commits")
		.name(commit);
	}
	
	public void analyzeDiffFilesInACommit(
		String repository,
		String branch,
		String commit
		) 
		throws GitRepositoryException, 
						GitAPIException {
		
		var data = new MetricsData();
		var diffFiles = Arrays.asList(player.diffFilesFromCommit(
			repository, 
			branch, 
			commit
		))
		.stream()
		.map(diff -> diff.getNewPath())
		.collect(Collectors.toList());
		
		analyzer
		.enableLogs(false)
		.process(commit, (file, result) -> {
			
			if(diffFiles.contains(file)) {
				data.addFiles(file, result);
			}
		});
		
		Resource
		.create(repository, ResourceType.METRICS_DIFF_FILES)
		.folder("commits")
		.name(commit);
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
			analyzeAllFilesInACommit(
				repository, 
				branch, 
				commit.getId().getName()
			);
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
	
}
