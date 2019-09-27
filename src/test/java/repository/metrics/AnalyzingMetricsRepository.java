package repository.metrics;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import repository.Repository;
import repository.exception.GitRepositoryException;

public class AnalyzingMetricsRepository {

	private Repository repo;
	
	@Before
	public void before() throws GitRepositoryException {
		repo = Repository.create(new File(
			"/home/leandroungari/Documents/repositories/refactoring-toy-example"
		));
	}
	
	@Test
	public void entireRepository() throws FileNotFoundException {
		
		var analyzer = new MetricsAnalyzer(
			"refactoring-toy-example", 
			repo
		);
		
		
		analyzer
		.localFolder(new File(
			"/home/leandroungari/Documents/data/metrics"
		))
		.enableLogs(false)
		.run();
	}
	
	/*@Before
  public void before() throws GitRepositoryException {
    repo = Repository.create(new File(
      "/home/leandroungari/Documents/repositories/truth"
    ));
  }
  
  @Test
  public void entireRepository() throws FileNotFoundException {
    
    var analyzer = new MetricsAnalyzer(
      "truth", 
      repo
    );
    
    analyzer
    .localFolder(new File(
      "/home/leandroungari/Documents/data/metrics"
    ))
    .enableLogs(false)
    .run();
  }*/
	
}
