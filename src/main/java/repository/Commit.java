package repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;

public class Commit {

	private ObjectId id;
	
	public static Commit create(ObjectId commitId) {
		
		return new Commit(commitId);
	}
	
	private Commit(ObjectId commitId) {
		id = commitId;
	}
	
	public String id() {
		return id;
	}
	
	private RevCommit build() {
		
		try {
			if(commit == null) {
				var walk = new RevWalk(git.getRepository());
				commit = walk.parseCommit(
					ObjectId.fromString(id)
				);
				walk.close();
			}
			
			return commit;
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<String> allFiles() {
		
		var list = new ArrayList<String>();
		
		try {
			
			var walk = new TreeWalk(git.getRepository());
			walk.addTree(build().getTree());
			walk.setRecursive(true);
			walk.setPostOrderTraversal(false);
			
			while(walk.next()) {
				list.add(walk.getPathString());
			}
			
			walk.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public List<String> allFilesByExtension(String ext) {
		
		return allFiles()
			.stream()
			.filter(file -> file.endsWith(ext))
			.collect(Collectors.toList());
	}

	private void setRepository(Git repo) {
		
		git = repo;
	}
	
	public File file(String name) {
		
		var file = new File(name);
		file.build(git, id);
		return file;
	}
	
	public CanonicalTreeParser createTreeParser() {
	
		var walk = new RevWalk(git.getRepository());
		//Creating a tree reader
		var reader = git.getRepository().newObjectReader();
				
		var treeParser = new CanonicalTreeParser();
		try {
			
			RevCommit newCommit = walk.parseCommit(ObjectId.fromString(id));
			
			ObjectId newTreeId = newCommit.getTree();
			treeParser.reset(reader, newTreeId);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		walk.close();
		return treeParser;
	}
	
	public Commit before() {
		
		var revWalk = new RevWalk(git.getRepository());
		
		String beforeId = "";
		try {
			
			revWalk.markStart(build());
			beforeId = revWalk
				.next()
				.getParent(0)
				.getName();
			
		} catch (
			ArrayIndexOutOfBoundsException | IOException e
		) {
			
			revWalk.close();
			return null;
		}
			
		revWalk.close();
		return Commit.create(beforeId, git);
	}
	
	public List<Diff> diff() {
		
		var list = new ArrayList<Diff>();
		var otherCommit = before();
		
		//Creating parser for old and new tree and its own ids
		var oldTreeParser = otherCommit.createTreeParser();
		var newTreeParser = this.createTreeParser();
		
	  try {
			git
				.diff()
				.setNewTree(newTreeParser)
				.setOldTree(oldTreeParser)
				.call()
				.stream()
				.forEach(a -> list.add(Diff.build(a)));
			
			return list;
			
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		return id;
	}
	
	
}
