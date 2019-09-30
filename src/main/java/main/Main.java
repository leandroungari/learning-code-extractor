package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import commandline.CommandLine;
import repository.exception.GitRepositoryException;

/**
 * 
 * @author leandroungari
 *
 */
@SpringBootApplication
@ComponentScan({"api.controller"})
public class Main {

  public static void main(String[] args) 
  	throws GitRepositoryException {
    
  	if (args.length == 0) {
  		runAPI(args);
  	}
  	else {
  		var commandLine = new CommandLine();
    	commandLine.exec(args);
  	}  	
  }

  public static void runAPI(String[] args) {
  	
  	SpringApplication.run(
  		Main.class, args
  	);
  }
}
