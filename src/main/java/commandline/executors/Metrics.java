package commandline.executors;

import org.eclipse.jgit.api.errors.GitAPIException;

import commandline.Executor;
import core.Player;
import repository.exception.GitRepositoryException;


/**
 * Improve this
 * 
 * This implementation only process the entire repository
 * 
 * @author leandroungari
 *
 */
public class Metrics implements Executor {

	private String repository;
	private String type;
	private String branch;
	private String rangeOfCommits;
	private boolean isHelper;
	
	private Player player = new Player();
	
	@Override
	public void run() {
		
		if(isHelper) {
			helper();
		}
		else {
			try {
			
				processEntireRepository();
			
			} catch (GitAPIException
				| GitRepositoryException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void processEntireRepository() 
		throws GitAPIException, GitRepositoryException {
		
		var branches = player
			.repository(repository)
			.branches();
		for(var i = 0; i < branches.length; i++) {
			processingABranch(
				branches[i]
					.getId()
					.getName()
			);
		}
	}
	

	private void processingABranch(String branch) {
		
		try {
			
			var listOfCommits = player
			.repository(repository)
			.commits(branch);
				
			player
			.metrics(repository)
			.metricsFromDiffFilesInARangeOfCommits(
				branch, 
				listOfCommits
			);
		
		} catch (GitAPIException
			| GitRepositoryException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void param(String... params) {
		if("-h".equals(params[0])) {
			isHelper = true;
		}
		else {
			this.repository = params[0];
			this.type = params[1];
			this.branch = params[2];
			this.rangeOfCommits = params[3];
		}
	}

	@Override
	public void helper() {
		System.out.println("This command analyses a repository to extract code metrics");
		System.out.println("metrics [repository] [type] [rangeOfCommits]");
		System.out.println(" - [repository] Name of repository");
		System.out.println(" - [type] Diff or all files of commit");
		System.out.println(" - [branch] All branches or id");
		System.out.println(" - [rangeOfCommits] all or from-to : Ex. 0-10.");
	}

}
