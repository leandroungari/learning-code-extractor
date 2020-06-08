package repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.lib.ObjectId;

public class Branch {

	private Repository repository;
	private String name;
	private ObjectId id;

	Branch(Repository repo, String name, String id) {
		this.name = name;
		this.id = ObjectId.fromString(id);
		this.repository = repo;
	}

	/**
	 * It gets branch id
	 * 
	 * @return
	 */
	public ObjectId id() {
		return id;
	}

	/**
	 * It gets branch identifier
	 * 
	 * @return
	 */
	public String name() {
		return name;
	}

	/**
	 * It list all commits in this branch
	 * 
	 * @return
	 */
	public List<Commit> commits() {

		var factory = new GitFactory(repository);
		var list = new ArrayList<Commit>();
		try {

			factory.commitStart(id, revcommit -> {

				list.add(0, new Commit(this, revcommit.getId()));
			});

		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<Commit> commits(Integer from, Integer to) {

		var result = commits();
		if (from < 0 || to > result.size() - 1 || from > to) {
			throw new Error("Invalid positions of range");
		}
		return result.subList(from, to + 1);
	}

	public List<Commit> commits(Integer from, Integer to, Integer step) {

		var result = new ArrayList<Commit>();
		var sub = commits(from, to);
		for (var i = 0; i < sub.size(); i += step) {
			result.add(sub.get(i));
		}
		return result;
	}

	public Commit commit(Integer position) {

		var result = commits();
		if (0 <= position && position < result.size()) {
			return result.get(position);
		}
		throw new Error("Invalid position in range of commits");
	}

	public Commit commit(String id) {

		return new Commit(this, ObjectId.fromString(id));
	}

	public Repository containedRepository() {
		return repository;
	}
}
