package commandline.executors;

import commandline.Executor;
import core.Player;
import repository.exception.GitRepositoryException;

public class Download implements Executor{

	private String url;
	private String repository;
	private boolean isHelper;
	private Player player = new Player();
	
	@Override
	public void run() {
		
		if(isHelper) {
			helper();
		}
		else {
			
			try {
				player
				.repository(repository)
				.download(url);
			} catch (GitRepositoryException e) {
				
				e.printStackTrace();
			}
		}
	}

	@Override
	public void param(String ...params) {
		
		if("-h".equals(params[0])) {
			isHelper = true;
		}
		else {
			repository = params[0];
			url = params[1];
		}
	}

	@Override
	public void helper() {
		
		System.out.println("This command downloads the specified repository in a local folder");
		System.out.println("download [repository] [url]");
		System.out.println(" - [repository] Name of repository");
		System.out.println(" - [url] The URL of git to clone repository");
	}

}
