package com.diycomputerscience.minesweepercore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	
	public List<Point> neighbours(Point p) {
		List<Point> neighbours = new ArrayList<Point>();
		
		// top left
		if(p.row - 1 >= 0 && p.col -1 >= 0) {
			neighbours.add(new Point(p.row-1, p.col-1));
		}
		
		// top
		if(p.row - 1 >= 0) {
			neighbours.add(new Point(p.row-1, p.col));
		}
		
		// top right
		if(p.row - 1 >= 0 && p.col + 1 < Board.MAX_COLS) {
			neighbours.add(new Point(p.row-1, p.col+1));
		}
		
		// right
		if(p.col + 1 < Board.MAX_COLS) {
			neighbours.add(new Point(p.row, p.col+1));
		}
		
		// bottom right
		if(p.row + 1 < Board.MAX_ROWS && p.col + 1 < Board.MAX_COLS) {
			neighbours.add(new Point(p.row+1, p.col+1));
		}
		
		// bottom
		if(p.row + 1 < Board.MAX_ROWS) {
			neighbours.add(new Point(p.row+1, p.col));
		}
		
		// bottom left
		if(p.row + 1 < Board.MAX_ROWS && p.col - 1 >= 0) {
			neighbours.add(new Point(p.row+1, p.col-1));
		}
		
		// left
		if(p.col - 1 >= 0) {
			neighbours.add(new Point(p.row, p.col-1));
		}
		
		return neighbours;
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
