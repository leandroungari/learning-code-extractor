package commandline.executors;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Pattern;

import commandline.Executor;
import repository.Repository;
import repository.exception.GitRepositoryException;
import repository.metrics.MetricsAnalyzer;

public class Extract implements Executor {

	private String repoFolder;
	private String analysisFolder;
	private String repositoryName;
	private boolean isHelper;
	
	@Override
	public void run() {
		
		if(isHelper) {
			helper();
		}
		else {
			
			Repository repo = null;
			
			try {
				repo = Repository
					.create(new File(repoFolder));
			} catch (GitRepositoryException e) {
				System.err.println("== Extract Executor ==");
				System.err.println("Git Repository error");
			}

			var analyzer = new MetricsAnalyzer();

			try {
				analyzer
					.repository(repo)
					.repositoryName(repositoryName)
					.localFolder(new File(analysisFolder))
					.enableLogs(false)
					.run();
			} catch (FileNotFoundException e) {
				System.err.println("== Extract Executor ==");
				System.err.println("File not found");
			}
		}
	}

	@Override
	public void param(String... params) {
		if("-h".equals(params[0])) {
			isHelper = true;
		}
		else {
			this.repoFolder = params[0];
			this.analysisFolder = params[1];
			
			var list = repoFolder.split(Pattern.quote("/"));
			this.repositoryName = list[list.length - 1];
		}
	}

	@Override
	public void helper() {
		System.out.println("This command analyses a repository to extract code metrics");
		System.out.println("extract [repoFolder] [analysisFolder]");
		System.out.println(" - [repoFolder] The folder of repository");
		System.out.println(" - [analysisFolder] A specified folder to store the data");
	}

}
