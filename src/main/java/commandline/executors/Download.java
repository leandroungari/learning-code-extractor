package commandline.executors;

import java.io.File;

import commandline.Executor;
import repository.Repository;
import repository.exception.GitRepositoryException;

public class Download implements Executor{

	private String url;
	private String folder;
	private boolean isHelper;
	
	@Override
	public void run() {
		
		if(isHelper) {
			helper();
		}
		else {
			
			try {
				Repository.create(
				  url,
				  new File(folder) 
				);
			} catch (GitRepositoryException e) {
				System.err.println("== Download Executor ==");
				System.err.println("Git Repository error");
			}
		}
	}

	@Override
	public void param(String ...params) {
		
		if("-h".equals(params[0])) {
			isHelper = true;
		}
		else {
			url = params[0];
			folder = params[1];
		}
	}

	@Override
	public void helper() {
		
		System.out.println("This command downloads the specified repository in a local folder");
		System.out.println("download [url] [folder]");
		System.out.println(" - [url] The URL of git to clone repository");
		System.out.println(" - [folder] The local folder");
	}

}
