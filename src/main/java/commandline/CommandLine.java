package commandline;

import java.io.File;
import java.io.FileNotFoundException;

import repository.Repository;
import repository.exception.GitRepositoryException;
import repository.metrics.MetricsAnalyzer;

public class CommandLine {

	public void exec(String[] args) 
		throws GitRepositoryException, 
						FileNotFoundException {
		
		switch (args[0]) {

		case "test":
			this.test();
			break;
		
		case "download":
			
			this.download(args[1], args[2]);
			break;
			
		case "extract":
			this.extract(args[1], args[2], args[3]);
		}
	}
	
	private void test() {
		System.out.println("Testing ...");
	}

	private void download(String url, String localFolder) 
		throws GitRepositoryException {
		
		System.out.println(url + localFolder);
		
		Repository.create(
      url,
      new File(localFolder) 
    );
	}
	
	private void extract(
		String sourceRepo, 
		String repositoryName, 
		String metricsFolder 
	) throws GitRepositoryException, 
						FileNotFoundException {
		
		var repo = Repository.create(new File(
			sourceRepo
		));
		
		var analyzer = new MetricsAnalyzer();
		 
		analyzer
		.repository(repo)
		.repositoryName(repositoryName)
		.localFolder(new File(
			metricsFolder
		))
		.enableLogs(false)
		.run();
	}
	
	

}
