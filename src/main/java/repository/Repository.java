package repository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

import core.Settings;
import repository.exception.GitRepositoryException;

public class Repository {

	private Git git;
	private String url;
	private File folder;

	private static Settings settings = Settings.get();

	public static Repository create(String name) {
		try {
      return Repository
        .create(new File(settings.folderOfRepositories() + "/" + name));
    } catch (GitRepositoryException e) {
			e.printStackTrace();
			return null;
    }
	}

	/**
	 * It instanciates a new repo
	 * 
	 * @param url
	 * @param folder
	 * @throws GitRepositoryException
	 */
	private Repository(String url, File folder)
		throws GitRepositoryException {
		
		if (url != null) {

			git = cloneRepository(url, folder);
		} 
		else if (folder != null) {

			if (!isRepository(folder))
				throw new GitRepositoryException(
					"This local folder is not a git repository!"
				);

			git = open(folder);
		}

		this.url = url;
		this.folder = folder;
	}

	/**
	 * We get a repo from a local folder
	 * 
	 * @param folder
	 * @return
	 */
	private Git open(File folder) {
		try {
			return Git.open(folder);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		return null;
	}

	/**
	 * It obtains a repository from a local folder
	 * 
	 * @param folder
	 * @return
	 * @throws GitRepositoryException
	 */
	public static Repository create(File folder)
			throws GitRepositoryException {

		if (folder == null)
			throw new NullPointerException(
					"The folder must not be null!");

		return new Repository(null, folder);
	}

	public static Repository create(String url, File folder)
			throws GitRepositoryException {

		if (url == null || folder == null)
			throw new NullPointerException(
					"The folder or url must not be empty!");

		return new Repository(url, folder);
	}

	/**
	 * It returns the remote url
	 * 
	 * @return url
	 */
	public String url() {
		return url;
	}

	/**
	 * It returns repository folder
	 * 
	 * @return
	 */
	public File folder() {
		return folder;
	}

	/**
	 * It verifies if the local folder is a git repository
	 * 
	 * @return Whether the local folder is a repo
	 */
	private boolean isRepository(File folder) {

		String local = folder.getAbsolutePath();

		if (local.endsWith("/"))
			local += ".git/";
		else
			local += "/.git/";

		var gitFolder = new File(local);

		return gitFolder.exists();
	}

	/**
	 * It clones the repository to a specified folder
	 * 
	 * @param localFolder Local folder
	 * @return
	 * @throws GitAPIException
	 * @throws TransportException
	 * @throws InvalidRemoteException
	 * @throws IOException
	 */
	private Git cloneRepository(String url, File local) {

		try {
			return Git
				.cloneRepository()
				.setURI(url)
				.setDirectory(local)
				.call();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get a list of remote branches of repository
	 * 
	 * @throws GitAPIException
	 * @throws GitRepositoryException
	 */
	public List<Branch> branches() throws GitAPIException {

		var factory = new GitFactory(this);
		return factory
			.branches()
			.stream()
			.map(ref -> {
				return new Branch(
					this,
					ref.getName(),
					ref.getObjectId().getName());
			})
			.collect(Collectors.toList());
	}
	
	public Branch branch(String id) 
	  throws GitAPIException {
	  
	  return branches()
	    .stream()
	    .filter(branch -> branch.id().name().equals(id))
	    .collect(Collectors.toList())
	    .get(0);
	}

	/**
	 * It shares an instance of git repository internally.
	 * 
	 * @return
	 */
	Git git() {
		return git;
	}

}
