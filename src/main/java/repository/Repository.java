package repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import repository.exception.UndefinedLocalRepositoryPathException;


public class Repository {

	private Git git;
	private String url;
	private File folder;
	
	
	public Repository() {
		
	}
	
	public Repository(String url, String localFolder) 
		throws FileAlreadyExistsException {
		
		folder = new File(localFolder);
		if(folder.exists()) {
			
			if(isRepository()) {
				
				try {
					git = Git.open(folder);
				} catch (IOException e) {			
					e.printStackTrace();
				}
			}
			else throw new FileAlreadyExistsException(
		     "This folder already exists!"
		  );
		}
		
	}
	
	/**
	 * 
	 * @param url 
	 */
	public Repository(String url) {
		
		this.url = url;
	}
	
	
	
	/**
	 * It verifies if the local folder is
	 * a git repository
	 * 
	 * !!It can be improved !!
	 * @return Whether the local folder is a repo
	 */
	private Boolean isRepository() {
	  
	  String local = folder.getAbsolutePath(); 
    
	  if(local.endsWith("/")) local += ".git/";
    else local += "/.git/";
    
    var gitFolder = new File(local);
    
    return gitFolder.exists();
	}
	
	/**
	 * It clones the repository to a specified folder
	 * @param localFolder Local folder
	 * @return 
	 * @throws GitAPIException 
	 * @throws TransportException 
	 * @throws InvalidRemoteException 
	 * @throws IOException 
	 */
	public Boolean cloneRepository(String localFolder) 
		throws UndefinedLocalRepositoryPathException, 
		       InvalidRemoteException, 
		       TransportException, 
		       GitAPIException, 
		       IOException {
	  
	  folder = new File(localFolder);
	  
	  if(folder.exists()) {
	    
	    if(isRepository()) {
	    	
	    	git = Git.open(folder);
	    	return false; 
	    }
	    
	    throw new FileAlreadyExistsException(
	      "This folder already exists!"
	    );
	  }
		
		if (url != null) {
			
			git = Git
				.cloneRepository()
				.setURI(url)
				.setDirectory(folder)
				.call();
			
			return true;
		}
		else 
		  throw new UndefinedLocalRepositoryPathException();
	}
	
	public List<String> allCommitsIDs() {
	  
	  var list = new ArrayList<String>();
	  
	  try {
	    var allRefs = git
	        .getRepository()
	        .getRefDatabase()
	        .getRefs();
	    
	    var walk = new RevWalk(git.getRepository());
	    for(Ref ref: allRefs) {
	      walk.markStart(walk.parseCommit(ref.getObjectId()));
	    }
	    
	    
	    for(RevCommit commit : walk) {
	      list.add(commit.getName());
	    }
	    
	    walk.close();
	  	    
	  } catch (IOException e) {
      e.printStackTrace();
    }
	  
	  
	  return list;
	}
	
	public Commit commit(String commitId) {
		
		return Commit.create(commitId, git);
	}
}
