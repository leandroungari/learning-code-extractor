package util;

import java.io.File;
import java.io.FileNotFoundException;

public interface Analyzer {

	public void run() throws FileNotFoundException;
	public Analyzer localFolder(File path);
}
