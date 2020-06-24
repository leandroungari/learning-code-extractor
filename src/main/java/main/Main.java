package main;

import java.io.FileNotFoundException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import command.ClusteringCommand;
import command.CommandExecutor;
import command.FinderCommand;
import command.MappingsCommand;
import command.MetricsCommand;
import command.repository.CommitsCommand;
import command.repository.DownloadCommand;
import repository.exception.GitRepositoryException;

/**
 * 
 * @author leandroungari
 *
 */
@SpringBootApplication
@ComponentScan({"api"})
//@ComponentScan({"api.controller"})
public class Main {

  public static void main(String[] args) 
  	throws GitRepositoryException, 
  				 FileNotFoundException {
    
  	if (args.length == 0) {
  		runAPI(args);
  	}
  	else {
      var executor = new CommandExecutor(args);
      executor.add("mappings", new MappingsCommand());
      executor.add("clustering", new ClusteringCommand());
      executor.add("download", new DownloadCommand());
      executor.add("metrics", new MetricsCommand());
      executor.add("commits", new CommitsCommand());
      executor.add("find", new FinderCommand());

      executor.execute();
  	}  	
  }

  public static void runAPI(String[] args) {
  	
  	SpringApplication.run(
  		Main.class, args
  	);
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
  public FilterRegistrationBean corsFilter() {
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
      config.addAllowedMethod(HttpMethod.PUT);
      config.addAllowedMethod(HttpMethod.DELETE);
      source.registerCorsConfiguration("/**", config);
      FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
      bean.setOrder(0);
      return bean;
  }

}


