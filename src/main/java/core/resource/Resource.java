package core.resource;

import core.controller.SettingsController;
import storage.Storage;

public abstract class Resource<T> {

	protected Storage storage;

	protected static SettingsController settings;

	Resource() {

		initializeSettings();
	}

	private void initializeSettings() {
		if(settings == null) {
			settings = SettingsController.get();
		}
	}

	public Storage storage() {
		return storage;
	}

	public abstract Resource<T> setParams(String... params);

	public abstract T read(Class<T> type);

	public abstract void write(T data);

	public boolean exists() {

		return storage().exists();
	}

}
