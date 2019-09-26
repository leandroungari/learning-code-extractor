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
	public static Branch create(String name, ObjectId id) {
		
		return new Branch(name, id);
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
				list.add(Commit.create(id));
			});

		} catch (IOException e) {
		
			System.err.println(e.getMessage());
		}
		
		return list;
	}
	
	public Commit commit(String id) {
		
		return Commit.create(ObjectId.fromString(id));
	}
}
