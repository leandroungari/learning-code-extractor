package repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.EmptyTreeIterator;

import repository.commit.CommitInfo;
import repository.commit.Diff;
import repository.commit.File;

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
	
	public AbstractTreeIterator createTreeParser() {
	
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
		
		AbstractTreeIterator oldTree;
		if(otherCommit.id() == null) {
			oldTree = new EmptyTreeIterator();
		}
		else {
			oldTree = otherCommit.createTreeParser();
		}
		
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
	
	public CommitInfo info() {
		
		var factory = new GitFactory();
		RevCommit commit = null;
		
		try {
			commit = factory.commit(id);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		
		var name = commit
			.getAuthorIdent()
			.getName();
		
		var email = commit
			.getAuthorIdent()
			.getEmailAddress();
		
		var message = commit
			.getShortMessage();
		
		var date = commit
			.getAuthorIdent()
			.getWhen();
		
		return CommitInfo
			.create(id)
			.setAuthorName(name)
			.setEmail(email)
			.setShortMessage(message)
			.setDate(date);
	}
	
	@Override
	public String toString() {
		return id.toString();
	}
}
