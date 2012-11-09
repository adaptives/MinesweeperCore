package com.diycomputerscience.minesweepercore;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;


/**
 * 
 */

/**
 * @author pshah
 *
 */
public class FilePersistenceStrategy implements PersistenceStrategy {

	public static final String SQUARE_FORMAT = "%d,%d:%b-%s";
	public static final String SQUARE_LOAD_REGEX = "(\\d*),(\\d*):(true|false)-(COVERED|UNCOVERED|FLAGGED)";

	private FileConnectionFactory fileConnectionFactory;
	
	private static final Logger cLogger = Logger.getLogger(FilePersistenceStrategy.class);
	
	public FilePersistenceStrategy(FileConnectionFactory fileConnectionfacory) {
		this.fileConnectionFactory = fileConnectionfacory;
	}
	
	@Override
	public void save(Square squares[][]) throws PersistenceException {
		try {
			cLogger.debug("Saving current board state to file '" + this.fileConnectionFactory.getFileName()+ "'");
			PrintWriter writer = getWriter();
			for(int row=0; row<Board.MAX_ROWS; row++) {
				for(int col=0; col<Board.MAX_COLS; col++) {
					Square square = squares[row][col];
					String squareRep = String.format(SQUARE_FORMAT, 
													 row, 
													 col, 
													 square.isMine(), 
													 square.getStatus());
					writer.println(squareRep);
				}
			}
			cLogger.debug("Completed saving board state to file");
		} catch(Exception e) {
			String msg = "Could not save the board";
			throw new PersistenceException(msg, e);
		}
	}

	private PrintWriter getWriter() throws Exception {
		return fileConnectionFactory.getWriter();
	}

	@Override
	public Square[][] load() throws PersistenceException {
		
		Square squares[][] = new Square[Board.MAX_ROWS][Board.MAX_COLS];
		
		try {
			cLogger.debug("Loading board state from file '" + this.fileConnectionFactory.getFileName()+ "'");
			// Get a reader to the file
			BufferedReader reader = getReader();
			// Compile the regex pattern
			Pattern regexPattern = Pattern.compile(SQUARE_LOAD_REGEX);
			String line = "";
			// For every line
			while((line = reader.readLine()) != null) {
				// Create a matcher object to perform matching on the line using the regex pattern
				Matcher matcher = regexPattern.matcher(line);
				matcher.find();
				// Get the first matching group. ie the pattern enclosed in the first () representing the row co-ordinate
				String sRow = matcher.group(1);
				int row = Integer.parseInt(sRow);
				// Get the second matching group, representing the col co-ordinate
				String sCol = matcher.group(2);
				int col = Integer.parseInt(sCol);
				// Get the third matching group, representing whether the square is a mine
				String sIsMine = matcher.group(3);
				boolean mine = Boolean.parseBoolean(sIsMine);
				// Get the fourth matching group, representing whether the square has been uncovered by the user
				String sStatus = matcher.group(4);
				Square.STATUS status = Square.STATUS.valueOf(sStatus);
				
				// Build a square object from the information gathered above
				Square square = new Square();
				square.setMine(mine);
				
				square.setStatus(status);
				squares[row][col] = square;
			}
			cLogger.debug("Completed loading board state");
		} catch(Exception ioe) {
			String msg = "Could not load persisted state of the board";
			throw new PersistenceException(msg, ioe);
		}
		
		return squares;
	}

	private BufferedReader getReader() throws Exception {
		return this.fileConnectionFactory.getReader();
	}

}
