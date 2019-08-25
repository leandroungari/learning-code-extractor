package repository;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

import repository.exception.UndefinedLocalRepositoryPathException;


public class Repository {

	private Git git;
	private String url;
	private File localFolder;
	
	
	public Repository() {
		
	}
	
	/**
	 * 
	 * @param url 
	 */
	public Repository(String url) {
		
		this.url = url;
	}
	
	/**
	 * It clones the repository to a specified folder
	 * @param localFolder Local folder
	 * @return 
	 * @throws GitAPIException 
	 * @throws TransportException 
	 * @throws InvalidRemoteException 
	 * @throws Exception
	 */
	public Boolean cloneRepository(File localFolder) 
		throws UndefinedLocalRepositoryPathException, 
		InvalidRemoteException, TransportException, 
		GitAPIException {
		
		if (this.url != null) {
			
			this.git = Git
				.cloneRepository()
				.setURI(url)
				.setDirectory(localFolder)
				.call();
			
			return true;
		}
		else 
		  throw new UndefinedLocalRepositoryPathException();
	}
	
	
}
