package com.diycomputerscience.minesweepercore;

public class MinesweeperConfig {

	private PersistenceStrategy persistenceStrategy;
	
	private static MinesweeperConfig minesweeperConfig = new MinesweeperConfig();;
	
	private MinesweeperConfig() {
		this.persistenceStrategy = new FilePersistenceStrategy(new DefaultFileConnectionFactory());
	}
	
	public static MinesweeperConfig getInstance() {
		return minesweeperConfig;
	}
	
	public PersistenceStrategy getPersistenceStrategy() {
		return this.persistenceStrategy;
	}
}
