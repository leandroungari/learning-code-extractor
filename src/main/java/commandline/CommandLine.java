package commandline;

import java.io.FileNotFoundException;
import java.util.Arrays;

import commandline.executors.Download;
import commandline.executors.Metrics;
import commandline.executors.MetricsFromDiffFiles;
import commandline.executors.Test;
import repository.exception.GitRepositoryException;

public class CommandLine {

	public void exec(String[] args) 
		throws GitRepositoryException, 
						FileNotFoundException {
		
		Executor executor = null;
		var params = Arrays.copyOfRange(args, 1, args.length);
		
		switch (args[0]) {

		case "test":
			executor = new Test();
			break;
		
		case "download":
			
			executor = new Download();
			break;
			
		case "metrics":
			executor = new Metrics();
			break;

		case "metrics-diff-files":
			executor = new MetricsFromDiffFiles();
			break;
		
		}
		
		if(executor != null) {
			
			executor.param(params);
			executor.run();
		}
	}
}
