package repository;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;


public class GitFactory {

	private static Git git;
	private static RevWalk walk;
	private static TreeWalk treeWalk;
	
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
	
	public TreeWalk treeWalk() {
		
		if(treeWalk == null)
			treeWalk = new TreeWalk(git.getRepository());
		return treeWalk;
	}
	
	public RevCommit commit(ObjectId id) 
		throws MissingObjectException, 
						IncorrectObjectTypeException, 
						IOException {
		
		return walk.parseCommit(id);
	}
	
	public RevCommit commit(ObjectId id, Integer num) 
		throws MissingObjectException, 
						IncorrectObjectTypeException, 
						IOException {
		
		var revWalk = walk();
		
		revWalk.markStart(commit(id));
		return revWalk
			.next()
			.getParent(0);
	}
	
	public void commitStart(
		ObjectId id, 
		Consumer<? super RevCommit> action
	) throws MissingObjectException, 
						IncorrectObjectTypeException, 
						IOException {
		
		walk.markStart(commit(id));
		walk.forEach(action);
	}
	
	
	
	public void treeStart(
			ObjectId id,
			Consumer<? super TreeWalk> action
		) throws MissingObjectException, 
							IncorrectObjectTypeException, 
							CorruptObjectException, 
							IOException {
			
		var treeWalker = treeWalk();
		treeWalker.addTree(commit(id).getTree());
		treeWalker.setRecursive(true);
		treeWalker.setPostOrderTraversal(false);
		
		while(treeWalker.next()) {
			action.accept(treeWalk);
		}
	}
	
	public CanonicalTreeParser createTreeParser(ObjectId id) {
		
		var reader = git.getRepository().newObjectReader();		
		var treeParser = new CanonicalTreeParser();
		
		try {	
			var commit = commit(id);	
			ObjectId treeId = commit.getTree();
			treeParser.reset(reader, treeId);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return treeParser;
	}
	
	public void diff(
		CanonicalTreeParser older,
		CanonicalTreeParser newer,
		Consumer<? super DiffEntry> action
	) throws GitAPIException {
		
		git
		.diff()
		.setNewTree(newer)
		.setOldTree(older)
		.call()
		.stream()
		.forEach(action);
	}

	public String fileContent(ObjectId id) 
		throws MissingObjectException, 
						IOException {
		
		var loader = git
			.getRepository()
			.open(id);
		return new String(loader.getBytes());
	}

	
}
