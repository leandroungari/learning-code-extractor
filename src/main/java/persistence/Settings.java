package persistence;

import java.io.File;

import persistence.system.OperationSystem;

public class Settings {

  private static String base;
	
	private static File folder;
	private static File metrics;
	private static File metadata;
	private static File repositories;
	
	public static final int USER_FOLDER = 1;
	public static final int CURRENT_FOLDER = 2;

	private static OperationSystem os = new OperationSystem();

	private static Settings settings;
	
	private Settings () {
		
		start(USER_FOLDER);
	}

	private Settings (Integer folderOption) {
		
		start(folderOption);
	}

	public static Settings get() {
		
		if(settings == null) {
			settings = new Settings();
		}
		
		return settings;
	}

	public static Settings get(Integer folderOption) {
		
		if(settings == null) {
			settings = new Settings(folderOption);
		}
		
		return settings;
	}
	
	public File base() {
		return folder;
	}
	
	public File folderOfMetadata() {
		return metadata;
	}
	
	public File folderOfRepositories() {
		return repositories;
	}
	
	public File folderOfMetrics() {
		return metrics;
	}
  
  public Boolean isSet() {
    return getBase() != null;
  }

	public void start(int option) {
		
		selectOption(option);
		createFolder();
		createAdditionalFolder();
	}
	
	private void selectOption(Integer optionFolder) {
		
		if(optionFolder == USER_FOLDER) {
			setBase(os.directory().getUserFolder() + "/data");
		}
		else {
			setBase(os.directory().getCurrentFolder() + "/data");
		}
	}
	
	private void createFolder() {
		
		folder = new File(getBase());
		if(!os.directory().exists(folder)) {
			
			os.directory().mkdir(folder);		
		}
	}
	
	private void createAdditionalFolder() {
		
		String path = base().getAbsolutePath();
		
		repositories = new File(path + "/repositories");
		metadata = new File(path + "/metadata");
		
		os.directory().mkdir(repositories);
		os.directory().mkdir(metadata);
	}

	private String getBase() {
		return base;
	}

	private void setBase(String base) {
		Settings.base = base;
	}

}