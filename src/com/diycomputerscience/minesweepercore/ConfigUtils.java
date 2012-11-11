package com.diycomputerscience.minesweepercore;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

public class ConfigUtils {

	private static final Logger cLogger = Logger.getLogger(ConfigUtils.class);
	
	public static String configDirName = ".jminesweepercore";
	
	public static final void initConfigDirectory() {
		File configDir = getConfigDirAsFile();
		try {
			if(!configDir.exists()) {			
				cLogger.info("Game configuration directory does not exist '" + configDir.getCanonicalPath() + "'");				
				boolean created = configDir.mkdir();
				if(created) {
					cLogger.info("Created configuration directory");
				} else {
					cLogger.warn("Could not create game configuration directory.");
				}			
			} else {
				cLogger.info("config directory already exists '" + configDir.getCanonicalPath() + "'");
			}
		} catch(IOException ioe) {
			cLogger.warn("Caught Exception while creating config directory '" + configDir.exists() + "'");
		}
	}
		
	
	public static final File getConfigDirAsFile() {
		String homeDir = System.getProperty("user.home");
		File configDir = new File(homeDir + "/" + configDirName);
		return configDir;
	}
}
