package commandline;

import java.io.File;

import repository.Repository;
import repository.exception.GitRepositoryException;

public class CommandLine {

	public void exec(String[] args) 
		throws GitRepositoryException {
		
		switch (args[0]) {

		case "test":
			this.test();
			break;
		
		case "download":
			
			this.download(args[1], args[2]);
			break;
			
		}
	}
	
	private void test() {
		System.out.println("Testing ...");
	}

	private void download(String url, String localFolder) 
		throws GitRepositoryException {
		
		Repository.create(
      url,
      new File(localFolder) 
    );
	}
	
	

}
