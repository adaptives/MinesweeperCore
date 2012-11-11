package com.diycomputerscience.minesweepercore;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class NeighboursTest {

	private Point p;
	private List<Point> expectedNeighbours;
	
	private Board board;
		
	@Parameters
	public static Collection data() {
		Point p = null;
		List<Point> expected = null;
		
		List data = new ArrayList();
		
		//data for top left
		p = new Point(0,0);
		expected = new ArrayList<Point>();
		expected.add(new Point(0,1));
		expected.add(new Point(1,1));
		expected.add(new Point(1,0));
		data.add(new Object[]{p,expected});
		
		// data for top row center
		p = new Point(0,1);
		expected = new ArrayList<Point>();
		expected.add(new Point(0,2));
		expected.add(new Point(1,2));
		expected.add(new Point(1,1));
		expected.add(new Point(1,0));
		expected.add(new Point(0,0));
		data.add(new Object[]{p,expected});
		
		//data for top right
		p = new Point(0,Board.MAX_COLS-1);
		expected = new ArrayList<Point>();
		expected.add(new Point(1,Board.MAX_COLS-1));
		expected.add(new Point(1,Board.MAX_COLS-2));
		expected.add(new Point(0,Board.MAX_COLS-2));
		data.add(new Object[]{p,expected});
		
		//data for right side column center
		p = new Point(1,Board.MAX_COLS-1);
		expected = new ArrayList<Point>();
		expected.add(new Point(0,Board.MAX_COLS-1));
		expected.add(new Point(2,Board.MAX_COLS-1));
		expected.add(new Point(2,Board.MAX_COLS-2));
		expected.add(new Point(1,Board.MAX_COLS-2));
		expected.add(new Point(0,Board.MAX_COLS-2));		
		data.add(new Object[]{p,expected});
		
		// data for bottom right
		p = new Point(Board.MAX_ROWS-1,Board.MAX_COLS-1);
		expected = new ArrayList<Point>();
		expected.add(new Point(Board.MAX_ROWS-2,Board.MAX_COLS-1));
		expected.add(new Point(Board.MAX_ROWS-1,Board.MAX_COLS-2));
		expected.add(new Point(Board.MAX_ROWS-2,Board.MAX_COLS-2));
		data.add(new Object[]{p,expected});
		
		// data for bottom row center
		p = new Point(Board.MAX_ROWS-1,Board.MAX_COLS-2);
		expected = new ArrayList<Point>();
		expected.add(new Point(Board.MAX_ROWS-2,Board.MAX_COLS-1));
		expected.add(new Point(Board.MAX_ROWS-1,Board.MAX_COLS-1));
		expected.add(new Point(Board.MAX_ROWS-1,Board.MAX_COLS-3));
		expected.add(new Point(Board.MAX_ROWS-2,Board.MAX_COLS-3));
		expected.add(new Point(Board.MAX_ROWS-2,Board.MAX_COLS-2));
		data.add(new Object[]{p,expected});
		
		// data for bottom left
		p = new Point(Board.MAX_ROWS-1,0);
		expected = new ArrayList<Point>();
		expected.add(new Point(Board.MAX_ROWS-1,1));
		expected.add(new Point(Board.MAX_ROWS-2,0));
		expected.add(new Point(Board.MAX_ROWS-2,1));
		data.add(new Object[]{p,expected});
		
		// data for left column center
		p = new Point(1,0);
		expected = new ArrayList<Point>();
		expected.add(new Point(0,1));
		expected.add(new Point(1,1));
		expected.add(new Point(2,1));
		expected.add(new Point(2,0));
		expected.add(new Point(0,0));
		data.add(new Object[]{p,expected});
		
		// data for center square
		p = new Point(1,1);
		expected = new ArrayList<Point>();		
		expected.add(new Point(0,2));
		expected.add(new Point(1,2));
		expected.add(new Point(2,2));
		expected.add(new Point(2,1));
		expected.add(new Point(2,0));
		expected.add(new Point(1,0));
		expected.add(new Point(0,0));
		expected.add(new Point(0,1));
		data.add(new Object[]{p,expected});
		
		return data;
	}
	
	public NeighboursTest(Point p, List<Point> expectedNeighbours) {
		this.p = p;
		this.expectedNeighbours = expectedNeighbours;
	}
	
	@Before
	public void setUp() throws Exception {
		this.board = new Board();
	}

	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	public void test() {
		List<Point> neighbours = this.board.neighbours(this.p);
		assertEquals(this.expectedNeighbours.size(), neighbours.size());
	}

}
