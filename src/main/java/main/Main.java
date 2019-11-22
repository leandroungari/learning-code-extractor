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
  	throws GitRepositoryException, 
  				 FileNotFoundException {
  	
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


