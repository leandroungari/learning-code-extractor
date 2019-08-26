package repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

public class Commit {

	private Git git;
	private String id;
	private RevCommit commit;
	
	public Commit(String commitId) {
		
		id = commitId;
		this.git = null;
		commit = null;
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

	protected void setRepository(Git repo) {
		
		git = repo;
	}
	
	public File file(String name) {
		
		var file = new File(name);
		file.build(git, id);
		return file;
	}
	
	
}
