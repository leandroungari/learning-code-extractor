package repository.metrics;

import java.io.File;
import java.io.FileNotFoundException;

import org.eclipse.jgit.api.errors.GitAPIException;

import com.google.gson.Gson;

import repository.Branch;
import repository.Commit;
import repository.Repository;
import util.Analyzer;
import util.Storage;

public class MetricsAnalyzer 
  implements Analyzer{

	private File folder;
	
	private String name;
	private Repository repo;
	
	public MetricsAnalyzer(
		String repositoryName, 
		Repository repo) {
	  
		this.name = repositoryName;
		this.repo = repo;
	}
	
	@Override
	public MetricsAnalyzer localFolder(File folder) {
	  this.folder = folder;
	  return this;
	}
	
	@Override
	public void run() throws FileNotFoundException {
		
		if(folder == null) throw new FileNotFoundException(
			"You must define a local folder!"
		); 
		
	  var storage = new Storage(
	    folder.getAbsolutePath() + "/" + name
	  );
	  try {
	    
      repo
      .branches()
      .forEach(branch -> {
        this.processing(storage, branch);
      });
      
    } catch (GitAPIException e) {
      System.err.println(e.getMessage());
    }
	}
	
	private void processing(
	  Storage storage, 
	  Branch branch) {
	  
	  branch
	  .commits()
	  .forEach(commit -> {
	    this.processing(
	      storage.folder(branch.id().getName()),
	      commit
	    );
	  });
	}
	
	private void processing(
	  Storage storage, 
	  Commit commit) {
	  
	  var data = new MetricsData();
	  
	  new CKMetric()
	  .calculate(
	    commit.id(), 
	    result -> {
	    	
	    	var position = result
	    		.getFile()
	    		.lastIndexOf(name) + name.length() + 1;
	    	
	    	var text = result
	    		.getFile()
		      .substring(position);
	    	
	      data
	      .addFiles(
	      	text, 
	        result
	      );	      
	    }
	  );
	  
	  this.store(
	    storage,
	    commit.id().getName() + ".json",
	    data
	  );
	}
	
	private void store(
	  Storage storage, 
	  String filename, 
	  MetricsData obj) {
	  
	  var gson = new Gson();
    var data = gson.toJson(obj);
    
    storage.file(filename, data);
	}
	

}
