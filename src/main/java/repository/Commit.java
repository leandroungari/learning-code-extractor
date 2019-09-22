package repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

public class Commit {

	private ObjectId id;
	
	public static Commit create(ObjectId commitId) {
		
		return new Commit(commitId);
	}
	
	private Commit(ObjectId commitId) {
		id = commitId;
	}
	
	public ObjectId id() {
		return id;
	}
	
	
	public List<File> allFiles() {
		
		var list = new ArrayList<File>();
		var factory = new GitFactory();
	
		try {
			factory.treeStart(id, walk -> {
				list.add(new File(
					walk.getPathString(), 
					walk.getObjectId(0)
				));
			});
			
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}				
		return list;
	}
	
	public List<File> allFilesByExtension(String ext) {
		
		return allFiles()
			.stream()
			.filter(file -> file.name().endsWith(ext))
			.collect(Collectors.toList());
	}
	
	public CanonicalTreeParser createTreeParser() {
	
		var factory = new GitFactory();
		return factory.createTreeParser(id);
	}
	
	public Commit before() {
		
		var factory = new GitFactory();
		try {
			return Commit.create(factory.commit(id, 1));
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
	
	public List<Diff> diff() {
		
		var list = new ArrayList<Diff>();
		var otherCommit = before();
		
		var oldTree = otherCommit.createTreeParser();
		var newTree = this.createTreeParser();
		
		var factory = new GitFactory();
	  try {
			
			factory.diff(
				oldTree, 
				newTree, 
				a -> list.add(Diff.build(a))
			);	
			
		} catch (GitAPIException e) {
			System.err.println(e.getMessage());
		}
		
	  return list;
	}
	
	public File filter(String filename) {
		
		var factory = new GitFactory();
		try {
			return factory.filterFile(id, filename);
		} catch (IOException e) {
		
			System.err.println(e.getMessage());
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		return id.toString();
	}
}
