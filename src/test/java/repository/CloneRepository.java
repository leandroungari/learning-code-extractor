package repository;

import java.io.File;

import org.junit.Test;

public class CloneRepository {

    @Test
    public void start() throws Exception {
        //https://github.com/apache/tomcat.git
        //https://github.com/danilofes/refactoring-toy-example.git
    		//https://github.com/google/guava.git

        Repository.create(
        	"https://github.com/google/guava.git",
        	new File("/home/leandroungari/Documents/repositories/guava") 
        );

    }

}
