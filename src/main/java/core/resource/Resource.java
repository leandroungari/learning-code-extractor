package core.resource;

import java.util.Map;

import com.google.gson.Gson;

import core.controller.SettingsController;
import util.FileUtil;
import util.Storage;

public abstract class Resource {

	private String repository;
	private String branch;
	private String filename;
	private ResourceType code;

	protected SettingsController settings;

	protected Resource() {

		if(settings == null) {
			settings = SettingsController.get();
		}
	}

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

	protected abstract String getFolder();

	protected abstract String getFile();

	public boolean exists() {

		var storage = new Storage(getFolder());
		return storage.exists(getFile());
	}

	public <T> T read(Class<T> type) {

		String content = new Storage(getFolder())
			.get(getFile());

		var gson = new Gson();
		return gson.fromJson(content, type);
	}

	public <T> void write(T data) {

		var gson = new Gson();
		var content = gson.toJson(data);
		
		new Storage(getFolder())
		.file(getFile(), content);
	}
	
	public void delete() {
		
		FileUtil
		.deleteFolder(
			new Storage(getFolder()).getFile(getFile())
		);
	}

}
