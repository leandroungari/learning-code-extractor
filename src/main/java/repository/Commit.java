package repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
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
			e.printStackTrace();
		}				
		return list;
	}
	
	public List<File> allFilesByExtension(String ext) {
		
		return allFiles().stream()
      .filter(file -> file.name().endsWith(ext))
      .collect(Collectors.toList());
	}
	
	private AbstractTreeIterator createTreeParser() {

		var factory = new GitFactory();
		return factory.createTreeParser(id);
	}
	
	public Commit before() {
		
		var factory = new GitFactory();
		try {
			return Commit.create(factory.commit(id, 1));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private AbstractTreeIterator createTree() {
		if(id() == null) {
			return new EmptyTreeIterator();
		}
		else {
			return this.createTreeParser();
		}
	}
	
	public List<Diff> diff() {
		
    var factory = new GitFactory();

		var list = new ArrayList<Diff>();
		var otherCommit = before();
		
		var oldTree = otherCommit.createTree();
		var newTree = this.createTree();
		
	  try {
			factory.diff(
				oldTree, 
				newTree, 
				a -> list.add(Diff.build(a))
			);

		} catch (GitAPIException e) {
			e.printStackTrace();
		}

	  return list;
  }
	
	public File getFile(String filename) {
		
		var factory = new GitFactory();
		try {
			return factory.filterFile(id, filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public CommitInfo info() {
		
		var factory = new GitFactory();
		
		try {
			var commit = factory.commit(id);

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

			var fullMessage = commit
				.getFullMessage();
			
			return CommitInfo
				.create(id)
				.setAuthorName(name)
				.setEmail(email)
				.setShortMessage(message)
				.setFullMessage(fullMessage)
				.setDate(date);

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		return id.toString();
	}

	public ArrayList<Map<String, String>> showDiff() {
		
		var factory = new GitFactory();

		var result = new ArrayList<Map<String,String>>();
    for(var item: diff()) {
      result.add(factory.contentOfDiffCommit(this,item));
	  }
		return result;
	}
}
