package core.resource;

import com.google.gson.Gson;

import core.controller.SettingsController;
import util.FileUtil;
import util.Storage;

public abstract class Resource {

	protected String repository;
	protected String folder;
	protected String file;

	protected static SettingsController settings;

	Resource() {

		initializeSettings();
	}

	private void initializeSettings() {
		if(settings == null) {
			settings = SettingsController.get();
		}
	}

	public abstract Resource setParams(String... params);

	protected abstract String getBaseFolder();
	
	protected String getFolder() {

		return getBaseFolder()
      + "/" 
      + repository 
      + (folder == null || "".equals(folder) ? "" : "/" + folder);
	}

	protected String getFile() {
		
		return (file == null ? "" : file);
	}

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
		
		new Storage(getFolder()).file(getFile(), content);
	}
	
	public void delete() {
		
		FileUtil.deleteFolder(
			new Storage(getFolder()).getFile(getFile())
		);
	}

}
