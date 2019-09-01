package repository;

import static org.junit.Assert.*;

import org.junit.Test;

import repository.Repository;

public class CloneRepository {

    @Test
    public void cloneRepository() throws Exception {
        //https://github.com/apache/tomcat.git
        //https://github.com/danilofes/refactoring-toy-example.git

        var repo = new Repository("https://github.com/danilofes/refactoring-toy-example.git");
        var result = repo.cloneRepository("/home/leandroungari/refactoring-toy-example");

        assertEquals(result, true);
    }

}
