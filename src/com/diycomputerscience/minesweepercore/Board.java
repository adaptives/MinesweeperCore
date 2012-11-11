package com.diycomputerscience.minesweepercore;

import java.io.IOException;

import org.apache.log4j.Logger;


public class Board {

	public static final int MAX_COLS = 6;
	public static final int MAX_ROWS = 6;
	
	private static final Logger cLogger = Logger.getLogger(Board.class);
	
	private BoardState boardState;
	
	public Board() {		
		
	}
	
	public Board(Initializer boardInitializer) {
		cLogger.debug("Creating board");
		this.boardState = new BoardState(MAX_ROWS, MAX_COLS);
		this.boardState.init();
		cLogger.debug("Initialized board");
		this.boardState.markMines(boardInitializer.mines());
		cLogger.debug("Marked mines using " + boardInitializer.getClass().getName());
		this.boardState.computeCounts();
		cLogger.debug("Computed counts");
	}
	
	/**
	 * Save the current state of the board to some persistent storage 
	 */
	public void save() throws PersistenceException {
		MinesweeperConfig.getInstance().getPersistenceStrategy().save(this.boardState.getSquares());
	}
	
	public void load() throws PersistenceException {
		this.boardState = new BoardState(MAX_ROWS, MAX_COLS);
		this.boardState.setSquares(MinesweeperConfig.getInstance().getPersistenceStrategy().load());
		this.boardState.computeCounts();
	}
	
	//all ops that can be done on this board
	
	public int fetchSquareCount(Point point) {
		return this.boardState.getSquare(point).getCount();
	}
	
	public boolean isSquareMine(Point point) {
		return this.boardState.getSquare(point).isMine();
	}
	
	public Square.STATUS fetchSquareStatus(Point point) {
		return this.boardState.getSquare(point).getStatus();
	}

	public void markSquareAsMine(Point point) {
		this.boardState.getSquare(point).markAsMine();		
	}

	public void uncoverSquare(Point point) throws UncoveredMineException  {
		this.boardState.getSquare(point).uncover();
	}
	
	//end ops
		
}
