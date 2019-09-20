package repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import repository.exception.GitRepositoryException;


public class Repository {

	private Git git;
	private String url;
	private File folder;
	
	private static Repository current;
	
	private Repository(String url, File folder) 
		throws GitRepositoryException {
		
		if (url != null) {
			
			git = cloneRepository(url, folder); 
		}
		else if (folder != null) {
			
			if(!isRepository(folder)) throw 
				new GitRepositoryException(
					"This local folder is not a git repository!"
				);
			
			git = open(folder);
		}
		
		this.url = url;
		this.folder = folder;
	}
	
	private Git open(File folder) {
		try {
			return Git.open(folder);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		
		return null;
	}
	
	/*
	public static Repository create(String url) {
		
		if(url == null) throw 
			new NullPointerException("The URL must not be null!");
		
		current = new Repository(url, null);
		return current;
	}*/
	
	public static Repository create(File folder) 
		throws GitRepositoryException {
		
		if(folder == null) throw 
			new NullPointerException("The folder must not be null!");
		
		current = new Repository(null, folder);
		return current;
	}
	
	public static Repository create(String url, File folder) 
		throws GitRepositoryException {
		
		if (url == null || folder == null) throw
			new NullPointerException("The folder or url must not be empty!");
		
		current = new Repository(url, folder);	
		return current;
	}
	
	public Repository get() {
		return this;
	}
	
	/**
	 * It verifies if the local folder is
	 * a git repository
	 * 
	 * @return Whether the local folder is a repo
	 */
	private boolean isRepository(File folder) {
	  
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
	public Git cloneRepository(String url, File local) {
	  
			try {
				return Git
					.cloneRepository()
					.setURI(url)
					.setDirectory(local)
					.call();
			} catch (GitAPIException e) {
		
				System.err.println(e.getMessage());
			}
			
			return null;
	}
	
	/**
	 * Get a list of remote branches 
	 * of repository
	 * @throws GitAPIException
	 */
	public List<Branch> branches() throws GitAPIException {
		
		var gitFactory = new Git(git.add().getRepository());
		var branches = gitFactory.branchList()
				.setListMode(ListMode.REMOTE).call();
		
		List<Branch> list = new ArrayList<>();
		for(var branch: branches) {
	
			list.add(Branch.create(
				branch.getName(), 
				branch.getObjectId()
			));
		}
		
		gitFactory.close();
		return list;
	}
	
	public Branch branch(String name) throws IOException {
		
		var ref = git
			.getRepository()
			.getRefDatabase()
			.findRef(name);
		
		return Branch.create(ref.getName(), ref.getObjectId());
	}
	
	
	@Deprecated
	/**
	 * This method must be removed after, because
	 * we implement the commits extraction by branches
	 * 
	 */
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
	
	@Deprecated
	public Commit commit(String commitId) {
		
		return Commit.create(commitId, git);
	}
}
