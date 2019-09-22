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
import org.eclipse.jgit.treewalk.filter.PathFilter;

import repository.commit.File;


public class GitFactory {

	private static Git git;
	
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
		
		return new RevWalk(git.getRepository());
	}
	
	public TreeWalk treeWalk() {
			
		return new TreeWalk(git.getRepository());
	}
	
	public RevCommit commit(ObjectId id) 
		throws MissingObjectException, 
						IncorrectObjectTypeException, 
						IOException {
		
		var walk = walk();
		var commit = walk.parseCommit(id);
		walk.close();
		
		return commit;
	}
	
	public RevCommit commit(ObjectId id, Integer num) 
		throws MissingObjectException, 
						IncorrectObjectTypeException, 
						IOException {
		
		var revWalk = walk();
		
		revWalk.markStart(commit(id));
		var commit = revWalk
			.next()
			.getParent(0);
		
		revWalk.close();
		
		return commit;
	}
	
	public void commitStart(
		ObjectId id, 
		Consumer<? super RevCommit> action
	) throws MissingObjectException, 
						IncorrectObjectTypeException, 
						IOException {
		
		var walk = walk();
		walk.markStart(commit(id));
		walk.forEach(action);
		walk.close();
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
			action.accept(treeWalk());
		}
		
		treeWalker.close();
	}
	
	public CanonicalTreeParser createTreeParser(ObjectId id) {
		
		var reader = git.getRepository().newObjectReader();		
		var treeParser = new CanonicalTreeParser();
		
		try {	
			var commit = commit(id);	
			ObjectId treeId = commit.getTree();
			treeParser.reset(reader, treeId);
			
		} catch (IOException e) {
			System.err.println(e.getMessage());
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
	
	public File filterFile(ObjectId commitId, String filename) 
		throws MissingObjectException, 
						IncorrectObjectTypeException, 
						IOException {
		
		var tree = commit(commitId).getTree();
		var treeWalk = treeWalk();
		treeWalk.addTree(tree);
		treeWalk.setRecursive(true);
		treeWalk.setFilter(PathFilter.create(filename));
		
		var id = treeWalk.getObjectId(0);
		
		treeWalk.close();
		
		return new File(filename, id);
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
