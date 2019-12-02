package repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.lib.ObjectId;

public class Branch {

	private String name;
	private ObjectId id;
	
	/**
	 * This method instanciates a branch
	 * @param name
	 * @param id
	 * @return
	 */
	public static Branch create(String name, String id) {
		
		return new Branch(
			name, 
			ObjectId.fromString(id)
		);
	}
	
	private Branch(String name, ObjectId id) {
		this.name = name;
		this.id = id;
	}
	
	/**
	 * It gets branch id
	 * @return
	 */
	public ObjectId id() {
		return id;
	}
	
	/**
	 * It gets branch identifier
	 * @return
	 */
	public String name() {
		return name;
	}
	
	
	/**
	 * It list all commits in this branch
	 * @return
	 */
	public List<Commit> commits() {
		
		var factory = new GitFactory();
		var list = new ArrayList<Commit>();
		try {
			
			factory.commitStart(id, revcommit -> {
		
				list.add(0, Commit.create(revcommit.getId()));
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return list;
	}

	public List<Commit> commits(Integer from, Integer to) {

		var result = commits();
		if(from < 0 || to > result.size()-1 || from > to) {
			throw new Error("Invalid positions of range");
		}
		return result.subList(from, to+1);
	}

	public List<Commit> commits(Integer from, Integer to, Integer step) {

		var result = new ArrayList<Commit>();
		var sub = commits(from, to);
		for(var i = 0; i < sub.size(); i+= step) {
			result.add(sub.get(i));
		}
		return result;
	}

	public Commit commit(Integer position) {

		var result = commits();
		if(0 <= position && position < result.size()) {
			return result.get(position);
		}
		throw new Error("Invalid position in range of commits");
	}
	
	public Commit commit(String id) {
		
		return Commit
			.create(ObjectId.fromString(id));
	}
}
