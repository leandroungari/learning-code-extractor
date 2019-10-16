package core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import util.Storage;

public class Resource {

	private String repository;
	private Integer code;
	
	private static final Integer LIST_OF_BRANCHES = 0;
	private static Map<Integer, String> options = Map.ofEntries(
		Map.entry(LIST_OF_BRANCHES, "branches.json")
	);
	
	private SettingsExecutor settings = new SettingsExecutor();
	
	private Resource(String name, Integer code) {
		this.code = code;
		this.repository = name;
	}
	
	public static Resource create(String repository, Integer resourceCode) {
		
		return new Resource(repository, resourceCode);
	}
	
	private Storage typeOfStorage() {
		if (code < 1000) {
			
			return new Storage(
				settings.metadataFolder().getAbsolutePath()
			);
		}
		else if (code < 2000) {
			
			return new Storage(
					settings.repositoriesFolder().getAbsolutePath()
			);
		}
		else {
			
			return new Storage(
				settings.metricsFolder().getAbsolutePath()
			);
		}
	}
	
	public boolean exists() {
		
		Storage storage = typeOfStorage();
		
		return storage
				.folder(repository)
				.exists(options.get(code));
	}
	
	public <T> T read(Class<T> type) {
		
		String content = typeOfStorage()
		.folder(repository)
		.get(options.get(code));
		
		var gson = new Gson();
		return gson.fromJson(content, type);
	}
	
	public <T> void write(T data) {
		
		var gson = new Gson();
		var content = gson.toJson(data);
		
		typeOfStorage()
		.folder(repository)
		.file(options.get(code), content);
	}
	
	
}
