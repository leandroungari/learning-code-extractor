package core;

import java.util.HashMap;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.data.Branch;
import core.data.Commit;
import core.data.CommitInfo;
import core.data.DiffFile;
import core.data.MetricsData;
import repository.exception.GitRepositoryException;
import util.FileUtil;

public class Player {
  
  private static RepositoryExecutor repoExec = new RepositoryExecutor();
  private static MetricsExecutor metricExec = new MetricsExecutor();
  private static SettingsExecutor settingsExec = new SettingsExecutor();
  
  public Player() {
    
  }
	
	public Branch[] branches(
	  String repositoryName
	) throws GitAPIException, 
	          GitRepositoryException {
		
		var resource = Resource.create(
		  repositoryName, 
		  ResourceType.LIST_OF_BRANCHES
		);
		
		if(!resource.exists()) {
		  repoExec.branches(repositoryName);
		}
		
		return resource.read(Branch[].class);
	}
	
	public Commit[] commits(
		String repository,
		String branch) 
		throws GitAPIException, 
						GitRepositoryException {
		
		var resource = Resource.create(
			repository, 
			ResourceType.LIST_OF_COMMITS
		)
		.branch(branch);
		
		if(!resource.exists()) {
			repoExec.commits(repository, branch);
		}
		
		return resource.read(Commit[].class);
	}
	
	public CommitInfo commitInfo(
		String repository,
		String branch,
		String commit) 
		throws GitAPIException, 
						GitRepositoryException {
		
		var resource = Resource.create(
			repository, 
			ResourceType.COMMIT_INFO
		)
		.branch(branch);
		
		if(!resource.exists()) {
			repoExec.commitInfo(repository, branch, commit);
		}
		
		var listOfCommits = resource
			.read(Commit[].class);
		
		for(int i = 0; i < listOfCommits.length; i++) {
			if(commit.equals(
				listOfCommits[i]
				.getId()
				.getName())) {
				
				return listOfCommits[i].getInfo();
			}
		}
		
		return null;
	}
	
	public DiffFile[] diffFilesFromCommit(
		String repository,
		String branch,
		String commit
		) throws GitRepositoryException, 
							GitAPIException {
		
		var resource = Resource
			.create(repository, ResourceType.DIFF_FILES)
			.branch(branch);
		
		if(!resource.exists()) {
			repoExec.diffFiles(repository, branch, commit);
		}
		
		return resource.read(DiffFile[].class);
	}
	
	public List<String> availableRepositories() {
		
		return FileUtil.listOfFolders(
      settingsExec.metricsFolder()
    );
	}
	
	public HashMap<String, Integer[]> metricsFromDiffFilesInACommit(
		String repository,
		String branch,
		String commit) 
			throws GitRepositoryException, 
							GitAPIException {
		
		var resourceDiff = Resource
			.create(repository, ResourceType.METRICS_DIFF_FILES)
			.folder("commits")
			.name(commit);
		
		if (!resourceDiff.exists()) {
			metricExec.analyzeDiffFilesInACommit(
				repository, 
				branch, 
				commit
			);
		}
		
		return resourceDiff
			.read(MetricsData.class)
			.getFiles();
	}
	
	public HashMap<String, Integer[]> metricsFromAllFilesInACommit(
		String repository,
		String branch,
		String commit) 
		throws GitRepositoryException, 
						GitAPIException {
		
		var resourceDiff = Resource
			.create(repository, ResourceType.METRICS_ALL_FILES)
			.folder("commits")
			.name(commit);
		
		if (!resourceDiff.exists()) {
			metricExec.analyzeDiffFilesInACommit(
				repository, 
				branch, 
				commit
			);
		}
		
		return resourceDiff
			.read(MetricsData.class)
			.getFiles();
	}
	
	public HashMap<String, HashMap<String, Integer[]>> 
		metricsFromDiffFilesInARangeOfCommits(
			String repository,
			String branch,
			Commit[] commits) 
				throws GitRepositoryException, 
				GitAPIException {
		
		var map = new HashMap<String, HashMap<String, Integer[]>>();
		for(var commit: commits) {
			var result = metricsFromDiffFilesInACommit(
				repository, 
				branch, 
				commit.getId().getName()
			);
			
			map.put(
				commit.getId().getName(),
				result
			);
		}
		
		return map;
	}
	
	public HashMap<String, HashMap<String, Integer[]>> 
	metricsFromAllFilesInARangeOfCommits(
		String repository,
		String branch,
		Commit[] commits) 
			throws GitRepositoryException, 
			GitAPIException {
	
	var map = new HashMap<String, HashMap<String, Integer[]>>();
	for(var commit: commits) {
		var result = metricsFromAllFilesInACommit(
			repository, 
			branch, 
			commit.getId().getName()
		);
		
		map.put(
			commit.getId().getName(),
			result
		);
	}
	
	return map;
}
}
