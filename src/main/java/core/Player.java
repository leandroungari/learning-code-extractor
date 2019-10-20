package core;

import java.io.File;
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
	
	public File repositoriesFolder() {
		return settingsExec.repositoriesFolder();
	}

	public Branch[] branches(String repositoryName)
		throws GitAPIException,
		GitRepositoryException {

		var resource = Resource.create(repositoryName,
			ResourceType.LIST_OF_BRANCHES);

		if (!resource.exists()) {
			repoExec.branches(repositoryName);
		}

		return resource.read(Branch[].class);
	}

	public Commit[] commits(String repository,
		String branch) throws GitAPIException,
		GitRepositoryException {

		var resource = Resource
			.create(repository,
				ResourceType.LIST_OF_COMMITS)
			.branch(branch);

		if (!resource.exists()) {
			repoExec.commits(repository, branch);
		}

		return resource.read(Commit[].class);
	}

	public CommitInfo commitInfo(String repository,
		String branch, String commit)
		throws GitAPIException,
		GitRepositoryException {

		var resource = Resource.create(
			repository,
			ResourceType.LIST_OF_COMMITS
		)
		.branch(branch);
		
		var listOfCommits = resource
			.read(Commit[].class);

		for (int i = 0; i < listOfCommits.length; i++) {
			if (commit.equals(
				listOfCommits[i].getId().getName())) {
				
				if(listOfCommits[i].getInfo() == null) {
					repoExec.commitInfo(repository, branch,
						commit);
				}
				
				return listOfCommits[i].getInfo();
			}
		}

		return null;
	}

	public List<DiffFile> diffFilesFromCommit(
		String repository, String branch,
		String commit) throws GitRepositoryException,
		GitAPIException {

		var resource = Resource
			.create(repository, ResourceType.DIFF_FILES)
			.branch(branch);

		if (!resource.exists()) {
			repoExec.commits(repository, branch);
		}
		
		repoExec.diffFiles(repository, branch, commit);
		var listOfCommits = resource.read(Commit[].class);
		Commit commitItem = null;
		for(var i = 0; i < listOfCommits.length; i++) {
			if(commit.equals(listOfCommits[i].getId().getName())) {
				commitItem = listOfCommits[i];
			}
		}

		return commitItem.getDiffFiles();
	}

	public List<String> availableRepositories() {

		return FileUtil.listOfFolders(
			settingsExec.metricsFolder());
	}

	public MetricsData metricsFromDiffFilesInACommit(
		String repository, String branch,
		String commit) throws GitRepositoryException,
		GitAPIException {

		var resourceDiff = Resource
			.create(
				repository,
				ResourceType.METRICS_DIFF_FILES
			)
			.folder("commits")
			.name(commit);

		if (!resourceDiff.exists()) {
			metricExec.analyzeDiffFilesInACommit(
				repository, branch, commit);
		}

		return resourceDiff.read(MetricsData.class);
	}

	public MetricsData metricsFromAllFilesInACommit(
		String repository, String branch,
		String commit) throws GitRepositoryException,
		GitAPIException {

		var resourceAll = Resource
			.create(repository,
				ResourceType.METRICS_ALL_FILES)
			.folder("commits")
			.name(commit);

		if (!resourceAll.exists()) {
			metricExec.analyzeAllFilesInACommit(
				repository, branch, commit);
		}

		return resourceAll.read(MetricsData.class);
	}

	public HashMap<String, MetricsData> metricsFromDiffFilesInARangeOfCommits(
		String repository, String branch,
		Commit[] commits)
		throws GitRepositoryException,
		GitAPIException {

		var map = new HashMap<String, MetricsData>();
		for (var commit : commits) {
			
			var result = metricsFromDiffFilesInACommit(
				repository, 
				branch,
				commit
				.getId()
				.getName()
			);

			map.put(commit.getId().getName(), result);
		}

		return map;
	}

	public HashMap<String, MetricsData> metricsFromAllFilesInARangeOfCommits(
		String repository, String branch,
		Commit[] commits)
		throws GitRepositoryException,
		GitAPIException {

		var map = new HashMap<String, MetricsData>();
		for (var commit : commits) {
			var result = metricsFromAllFilesInACommit(
				repository, branch,
				commit.getId().getName());

			map.put(commit.getId().getName(), result);
		}

		return map;
	}
}
