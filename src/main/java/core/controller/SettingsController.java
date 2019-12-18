package core.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;

import util.FileUtil;

public class SettingsController {

  private static String base;
	
	private static File folder;
	private static File metrics;
	private static File metadata;
	private static File repositories;
	
	public static final int USER_FOLDER = 1;
	public static final int CURRENT_FOLDER = 2;

	private static SettingsController settings;
	
	private SettingsController () {
		
		start(USER_FOLDER);
	}

	private SettingsController (Integer folderOption) {
		
		start(folderOption);
	}

	public static SettingsController get() {
		
		if(settings == null) {
			settings = new SettingsController();
		}
		
		return settings;
	}

	public static SettingsController get(Integer folderOption) {
		
		if(settings == null) {
			settings = new SettingsController(folderOption);
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
			setBase(FileUtil.userFolder() + "/.learning-code-extractor");
		}
		else {
			setBase(FileUtil.currentFolder() + "/.learning-code-extractor");
		}
	}
	
	private void createFolder() {
		
		folder = new File(getBase());
		if(!FileUtil.folderExists(folder)) {
			
			FileUtil.createFolder(folder);
			if(System.getProperty("os.name").contains("Windows")) {
				
				var path = Paths.get(getBase());
				
				try {
					Files.setAttribute(path, "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	private void createAdditionalFolder() {
		
		String path = base().getAbsolutePath();
		
		repositories = new File(path + "/repositories");
		metadata = new File(path + "/metadata");
		metrics = new File(path + "/metrics");
		
		FileUtil.createFolder(repositories);
		FileUtil.createFolder(metadata);
		FileUtil.createFolder(metrics);
	}

	private String getBase() {
		return base;
	}

	private void setBase(String base) {
		SettingsController.base = base;
	}

}