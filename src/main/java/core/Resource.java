package core;

import java.util.Map;

import com.google.gson.Gson;

import util.Storage;

public class Resource {

	private String repository;
	private String branch;
	private ResourceType code;

	private static Map<ResourceType, String> options = Map
		.ofEntries(
			Map.entry(
				ResourceType.LIST_OF_BRANCHES,"branches.json"
			),
			Map.entry(
				ResourceType.LIST_OF_COMMITS, "commits.json"
			),
			Map.entry(
				ResourceType.COMMIT_INFO, "commits.json"
			)
		);

	private SettingsExecutor settings = new SettingsExecutor();

	private String getOptions() {
		
		switch(code) {
		
		case LIST_OF_BRANCHES:
			return options
				.get(ResourceType.LIST_OF_BRANCHES);
		
		case LIST_OF_COMMITS:
			return branch + "/" + 
			options.get(ResourceType.LIST_OF_COMMITS);
			
		case COMMIT_INFO:
			return branch + "/" + 
				options.get(ResourceType.COMMIT_INFO);
			
		default:
			return null;
		}
	}
	
	private Resource(String name, ResourceType code) {
		this.code = code;
		this.repository = name;
	}

	public static Resource create(
		String repository,
		ResourceType resourceCode) {

		return new Resource(repository, resourceCode);
	}
	
	public Resource branch(String id) {
		repository = id;
		return this;
	}

	private Storage typeOfStorage() {
		
		switch(code) {
		
		case LIST_OF_BRANCHES:
		case LIST_OF_COMMITS:
		case COMMIT_INFO:
			
			return new Storage(
				settings
				.metadataFolder()
				.getAbsolutePath());
			
			/*
			 * return new Storage(
				settings
				.repositoriesFolder()
				.getAbsolutePath());
			 */
			
			/*
			 * return new Storage(settings.metricsFolder()
				.getAbsolutePath());
			 */
			default:
				return null;
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
			.get(getOptions());

		var gson = new Gson();
		return gson.fromJson(content, type);
	}

	public <T> void write(T data) {

		var gson = new Gson();
		var content = gson.toJson(data);

		typeOfStorage()
		.folder(repository)
		.file(getOptions(), content);
	}

}
