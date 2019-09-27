package util;

import java.io.File;
import java.io.FileNotFoundException;

import repository.Repository;

public interface Analyzer {

	public void run() throws FileNotFoundException;
	
	public Analyzer localFolder(File path);
	public Analyzer repository(Repository r);
	public Analyzer repositoryName(String name);
}
