package core.resource;

import java.util.Map;

import com.google.gson.Gson;

import core.controller.SettingsController;
import util.FileUtil;
import util.Storage;

public class Resource {

	private String repository;
	private String branch;
	private String filename;
	private ResourceType code;

	private SettingsController settings;


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
			),
			Map.entry(
				ResourceType.DIFF_FILES, "commits.json"
			),
			Map.entry(
				ResourceType.METRICS_DIFF_FILES, "diff - "
			),
			Map.entry(
				ResourceType.METRICS_ALL_FILES, ""
			)
		);
	
	private Resource(String name, ResourceType c) {
		code = c;
		repository = name;
		branch = "";
		filename = "";
		settings = SettingsController.get();
	}

	public static Resource create(
		String repository,
		ResourceType resourceCode) {

		return new Resource(repository, resourceCode);
	}
	
	public Resource branch(String id) {
		branch += "/" + id;
		return this;
	}
	
	public Resource folder(String name) {
		branch += "/" + name;
		return this;
	}
	
	public Resource name(String name) {
		filename = name + ".json";
		return this;
	}

	private Storage typeOfStorage() {
    
		switch(code) {
		
		case LIST_OF_BRANCHES:
		case LIST_OF_COMMITS:
		case COMMIT_INFO:
		case DIFF_FILES:
			
			return new Storage(
        settings
				.folderOfMetadata()
				.getAbsolutePath());
			
			/*
			 * return new Storage(
				settings
				.repositoriesFolder()
				.getAbsolutePath());
			 */
			
		case METRICS_DIFF_FILES:
		case METRICS_ALL_FILES:
			return new Storage(
        settings
        .folderOfMetrics()
				.getAbsolutePath());
			 
			default:
				return null;
		}
	
	}

	public boolean exists() {

		Storage storage = typeOfStorage();

		return storage
			.folder(repository + branch)
			.exists(options.get(code) + filename);
	}

	public <T> T read(Class<T> type) {

		String content = typeOfStorage()
			.folder(repository + branch)
			.get(options.get(code) + filename);

		var gson = new Gson();
		return gson.fromJson(content, type);
	}

	public <T> void write(T data) {

		var gson = new Gson();
		var content = gson.toJson(data);
		
		typeOfStorage()
		.folder(repository + branch)
		.file(options.get(code) + filename, content);
	}
	
	public void delete() {
		
		FileUtil
		.deleteFolder(
			typeOfStorage()
			.getFile(options.get(code) + filename)
		);
	}

}
