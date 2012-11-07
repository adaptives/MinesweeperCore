package com.diycomputerscience.minesweepercore;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BoardStateTest {

	private BoardState boardState;
	
	@Before
	public void setUp() throws Exception {
		this.boardState = new BoardState(Board.MAX_ROWS, Board.MAX_COLS);
		this.boardState.init();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMarkMines() {
		MockInitializer mockInitializer = new MockInitializer();
		Point mines[] = mockInitializer.mines();
		List<Point> lMines = Arrays.asList(mines);
		
		this.boardState.markMines(mines);
		for(int row=0; row<Board.MAX_ROWS; row++) {
			for(int col=0; col<Board.MAX_COLS; col++) {
				Point point = new Point(row,col);
				if(lMines.contains(point)) {
					assertTrue(this.boardState.getSquare(point).isMine());
				} else {
					assertTrue(!this.boardState.getSquare(point).isMine());
				}
			}
		}
	}
	
	@Test
	public void testComputeCounts() {
		MockInitializer mockInitializer = new MockInitializer();
		Point mines[] = mockInitializer.mines();
		this.boardState.markMines(mines);
		this.boardState.computeCounts();
		int expectedCounts[][] = new int[][]{
				{-1, 2, 1, 1, 0, 0},
				{2, 3, -1, 1, 0, 0},
				{1, -1, 2, 2, 1, 1},
				{1, 2, 2, 2, -1, 2},
				{0, 2, -1, 4, 3, -1},
				{0, 2, -1, -1, 2, 1}
		};
		for(int row=0; row<Board.MAX_ROWS;row++) {
			for(int col=0; col<Board.MAX_COLS; col++) {				
				int expectedCount = expectedCounts[row][col];
				if(expectedCount != -1) {
					String msg = "Failed for cell [" + row + "][" + col + "]";
					assertEquals(msg , expectedCount, boardState.getSquare(new Point(row,col)).getCount());
				}
			}
		}
	}

	@Test
	public void testSetSquareIsDefensive() throws Exception {
		MockInitializer mockInitializer = new MockInitializer();
		Point mines[] = mockInitializer.mines();
		this.boardState.markMines(mines);
		this.boardState.computeCounts();
		
		Point zeroZero = new Point(0,0);
		Square square = new Square();
		square.setCount(10000);
		this.boardState.setSquare(zeroZero, square);
		// lets change the count of the original square
		square.setCount(10);
		// change the count of a Square that we are still holding onto. This should not
		// cause the corresponding Square in BoardState to change		
		assertEquals(10000, getSquareUsingReflection(this.boardState, new Point(0,0)).getCount());				
	}
	
	@Test
	public void testSetSquaresIsDefensive() throws Exception {
		Square squares[][] = new Square[Board.MAX_ROWS][Board.MAX_COLS];
		for(int i=0; i<Board.MAX_ROWS; i++) {
			for(int j=0; j<Board.MAX_COLS; j++) {
				Square square = new Square();
				square.setCount(0);
				squares[i][j] = square;				
			}
		}
		
		BoardState boardState = new BoardState(Board.MAX_ROWS, Board.MAX_COLS);
		boardState.setSquares(squares);
		// change the count of a Square that we are still holding onto. This should not
		// cause the corresponding Square in BoardState to change
		squares[0][0].setCount(10);
		assertEquals(0, getSquareUsingReflection(boardState, new Point(0,0)).getCount());
	}
	
	@Test
	public void testGetSquaresIsDefensive() throws Exception {
		MockInitializer mockInitializer = new MockInitializer();
		Point mines[] = mockInitializer.mines();
		this.boardState.markMines(mines);
		this.boardState.computeCounts();
		
		Square squares[][]  = this.boardState.getSquares();
		// change the count of a Square that we are still holding onto. This should not
		// cause the corresponding Square in BoardState to change		
		squares[0][0].setCount(10000);
		assertFalse(getSquareUsingReflection(this.boardState, new Point(0,0)).getCount() == 10000);
	}
	
	private Square getSquareUsingReflection(BoardState boardState, Point p) 
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field boardStateFieldSquare = BoardState.class.getDeclaredField("squares");
		boardStateFieldSquare.setAccessible(true);
		Square squares[][] = (Square[][])boardStateFieldSquare.get(boardState);
		return squares[p.row][p.col];
	}
}
