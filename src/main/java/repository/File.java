package repository;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

public class File {

	private String name;
	private ObjectId id;
	private Git git;
	
	protected File(String name) {
		this.name = name;
		this.git = null;
	}
	
	public String getName() {
		return name;
	}
	
	public String get() {
		
		try {
			var loader = git.getRepository().open(id);
			var data = loader.getBytes();
			return new String(data);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return "";
	}
	
	
	protected void build(Git git, String commitId) {
		
		this.git = git;
		
		try {

			var revwalk = new RevWalk(git.getRepository());
			var commit = revwalk.parseCommit(
				ObjectId.fromString(commitId)
			);
			
			var tree = commit.getTree();
			var treewalk = new TreeWalk(git.getRepository());
			treewalk.addTree(tree);
			treewalk.setRecursive(true);
			treewalk.setFilter(PathFilter.create(name));
			
			if(!treewalk.next()) {

				revwalk.close();
				treewalk.close();
				
				throw new FileNotFoundException(
					"This file does not exist!"
				);
			}
			
			id = treewalk.getObjectId(0);
			
			revwalk.close();
			treewalk.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
