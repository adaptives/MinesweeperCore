package com.diycomputerscience.minesweepercore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;


public class DefaultFileConnectionFactory implements FileConnectionFactory {

	//This can also be read in from a configuration file
	private static final String persistenceFileName = "board.txt";
	
	@Override
	public PrintWriter getWriter() throws IOException {
		return new PrintWriter(getFileName());
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new FileReader(getFileName()));
	}

	@Override
	public String getFileName() throws IOException {
		File configDir = ConfigUtils.getConfigDirAsFile();
		return configDir.getCanonicalPath() + "/" + this.persistenceFileName;
	}
}
