package repository;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;


public class GitFactory {

	private static Git git;
	private static RevWalk walk;
	
	public GitFactory() {
		
		if(git == null) git = Repository.get().git();
	}
	
	public List<Ref> branches() throws GitAPIException {
		
		return git
			.branchList()
			.setListMode(ListMode.REMOTE)
			.call();
	}
	
	public Ref branch(String name) throws IOException {
		
		return git
			.getRepository()
			.getRefDatabase()
			.findRef(name);
	}
	
	public RevWalk walk() {
		
		if(walk == null) 
			walk = new RevWalk(git.getRepository());
		return walk;
	}
	
	public RevCommit commit(ObjectId id) 
		throws MissingObjectException, 
						IncorrectObjectTypeException, 
						IOException {
		
		return walk.parseCommit(id);
	}
	
	public Iterator<RevCommit> start(ObjectId id) 
		throws MissingObjectException, 
						IncorrectObjectTypeException, 
						IOException {
		
		walk.markStart(commit(id));
		return walk.iterator();
	}



	
}
