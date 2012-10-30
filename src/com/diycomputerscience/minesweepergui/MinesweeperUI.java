/**
 * 
 */
package com.diycomputerscience.minesweepergui;

import java.awt.Color;


import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


import com.diycomputerscience.minesweepercore.Board;
import com.diycomputerscience.minesweepercore.Point;
import com.diycomputerscience.minesweepercore.RandomBoardInitializer;
import com.diycomputerscience.minesweepercore.UncoveredMineException;

/**
 * @author pshah
 * 
 */

public class MinesweeperUI extends JFrame implements MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final int row = 6;
	final int col = 6;
	
	private static JButton gridArr[][];
	private  static Board mineBoard;
	
	private int option =0;
	
	public MinesweeperUI() {
		this.setTitle("Minesweeper");
		this.setLayout(new GridLayout(1, 1));
		mineBoard = new Board(new RandomBoardInitializer());
		this.setMineLayout();
		this.addWindowListener();
		
	}

	/**
	 * This method sets the layoutManager to Panel and creates the Buttons for
	 * the grid
	 */
	private void setMineLayout() {
		JPanel panel;
		
		
		this.setSize(300, 300);
		this.setVisible(true);
		panel = new JPanel();
		panel.setLayout(new GridLayout(row, col));

		/** In this for loop create the Buttons and populate the Button Array */
		JButton gridButton;

		gridArr = new JButton[row][col];
		
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				gridButton = new JButton("");
				gridButton.setName(i+","+j);
				gridArr[i][j] = gridButton;
				
			}
		}
		

		this.addGridButtons(panel);
		this.addGridMouseListener();
		this.getContentPane().add(panel);
		this.validate();
		
	}// setMineLayout()

	/** This method adds the JButtons array to the Panel on the JFrame */
	private void addGridButtons(JPanel panel) {
		
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++)
				panel.add(gridArr[i][j]);
			
				
		}
	}

	/**
	 * This method circulates through the Buttons Array and adds MouseListener
	 * to each Button on the grid
	 */
	private void addGridMouseListener() {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++)
				gridArr[i][j].addMouseListener(this);
		}
	
	}// addGridMouseListener()
	
	
	@Override
	public void mouseEntered(MouseEvent me) {

	}

	@Override
	public void mouseClicked(MouseEvent me) 
	{
		
		String buttonName = ((JButton)me.getComponent()).getName();
		StringBuffer strbuff = new StringBuffer(buttonName);
		int index = strbuff.indexOf(",");
		int i = Integer.parseInt(((String)strbuff.substring(0,index)));
		int j = Integer.parseInt(((String)strbuff.substring(index+1)).toString());

		if (SwingUtilities.isLeftMouseButton(me)) 
		{
			try
			{
						
				mineBoard.uncoverSquare(new Point(i, j));
				
			} catch (UncoveredMineException ue) 
			{					
				
			}
			if (mineBoard.isSquareMine(new Point(i, j))) 
			{
			
				// Exit the game
				option = JOptionPane.showConfirmDialog(this,"You have Lost The Game,want to play again","Really Quit",JOptionPane.YES_NO_OPTION);
				if(option == JOptionPane.NO_OPTION)
					this.dispose();
				else
				{
					
					this.getContentPane().removeAll();
					mineBoard = new Board(new RandomBoardInitializer());
					this.setMineLayout();
					this.addWindowListener();
					this.invalidate();
					
				}
			}
			else
			 {
				// not a mine so display its count
				((JButton) me.getComponent()).setBackground(new Color(255, 255, 255));
				((JButton) me.getComponent()).setEnabled(false);
				if(mineBoard.fetchSquareCount(new Point(i, j))!=0)
					gridArr[i][j].setText(""+ mineBoard.fetchSquareCount(new Point(i, j)));
				else
					//The MineCount is Zero 
					this.zeroMine(i, j);
			}
		}

		else {
			// if rightButton clicked
			if (SwingUtilities.isRightMouseButton(me))
				((JButton) me.getComponent()).setBackground(new Color(255, 0, 0));

		}
 		
	}//end of mouseclicked()

	@Override
	public void mouseExited(MouseEvent me) {
		// this.layGrid();
	}

	@Override
	public void mousePressed(MouseEvent me) {

	}

	@Override
	public void mouseReleased(MouseEvent me) {
		// this.layGrid();
	}

	/**
	 * This method adds Window closing listener
	 */
	private void addWindowListener() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				System.exit(DISPOSE_ON_CLOSE);
			}
		});

	}// addWindowListener()
	
	private void zeroMine(int row,int col)
	{
		if(row == 0 && col == 0)
		{
			gridArr[row][col].setText(""+mineBoard.fetchSquareCount(new Point(row, col)));
			((JButton) gridArr[row][col].getComponentAt(row,col)).setBackground(new Color(255, 255, 255));
			((JButton) gridArr[row][col].getComponentAt(row,col)).setEnabled(false);
			if(gridArr[row][col+1].getComponentAt(row,col+1).isEnabled())
			{	
				if(mineBoard.fetchSquareCount(new Point(row, col+1)) == 0)
					zeroMine(row, col+1);
				else
				{
					gridArr[row][col+1].setText(""+mineBoard.fetchSquareCount(new Point(row, col+1)));
					((JButton) gridArr[row][col+1].getComponentAt(row,col+1)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row][col+1].getComponentAt(row,col+1)).setEnabled(false);
				}
				
			}
				
			if(gridArr[row+1][col].getComponentAt(row+1,col).isEnabled())
			{	
				if(mineBoard.fetchSquareCount(new Point(row+1, col)) == 0)
					zeroMine(row+1, col);
				else
				{
					gridArr[row+1][col].setText(""+mineBoard.fetchSquareCount(new Point(row+1, col)));
				 
					((JButton) gridArr[row+1][col].getComponentAt(row+1,col)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row+1][col].getComponentAt(row+1,col)).setEnabled(false);
				}
				
			}
			
			if(gridArr[row+1][col+1].getComponentAt(row+1,col+1).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row+1, col+1)) == 0)
					zeroMine(row+1, col+1);
				else
				{
					gridArr[row+1][col+1].setText(""+mineBoard.fetchSquareCount(new Point(row+1, col+1)));
				 
					((JButton) gridArr[row+1][col+1].getComponentAt(row+1,col+1)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row+1][col+1].getComponentAt(row+1,col+1)).setEnabled(false);
				}
				
			}
			return;
		}
		
		
		if(row ==0 && col == Board.MAX_COLS-1)
		{
				gridArr[row][col].setText(""+mineBoard.fetchSquareCount(new Point(row, col)));
				((JButton) gridArr[row][col].getComponentAt(row,col)).setBackground(new Color(255, 255, 255));
				((JButton) gridArr[row][col].getComponentAt(row,col)).setEnabled(false);
				
				if(gridArr[row][col-1].getComponentAt(row,col-1).isEnabled())
				{
					if(mineBoard.fetchSquareCount(new Point(row, col-1)) == 0)
						zeroMine(row, col-1);
					else
					{
						gridArr[row][col-1].setText(""+mineBoard.fetchSquareCount(new Point(row, col-1)));
					 
						((JButton) gridArr[row][col-1].getComponentAt(row,col-1)).setBackground(new Color(255, 255, 255));
						((JButton) gridArr[row][col-1].getComponentAt(row,col-1)).setEnabled(false);
					}
					
				}
				
				if(gridArr[row+1][col-1].getComponentAt(row+1,col-1).isEnabled())
				{
					if(mineBoard.fetchSquareCount(new Point(row+1, col-1)) == 0)
						zeroMine(row+1, col-1);
					else
					{
						gridArr[row+1][col-1].setText(""+mineBoard.fetchSquareCount(new Point(row+1, col-1)));
					 
						((JButton) gridArr[row+1][col-1].getComponentAt(row+1,col-1)).setBackground(new Color(255, 255, 255));
						((JButton) gridArr[row+1][col-1].getComponentAt(row+1,col-1)).setEnabled(false);
					}
					
				}
				
				if(gridArr[row+1][col].getComponentAt(row+1,col).isEnabled())
				{
					if(mineBoard.fetchSquareCount(new Point(row+1, col)) == 0)
						zeroMine(row+1, col);
					else
					{
						gridArr[row+1][col].setText(""+mineBoard.fetchSquareCount(new Point(row+1, col)));
					 
						((JButton) gridArr[row+1][col].getComponentAt(row+1,col)).setBackground(new Color(255, 255, 255));
						((JButton) gridArr[row+1][col].getComponentAt(row+1,col)).setEnabled(false);
					}
					
				}
				
				return;
			}
		if(row == 0 && (col > 0 && col <= ( Board.MAX_COLS-2)) )
		{
			gridArr[row][col].setText(""+mineBoard.fetchSquareCount(new Point(row, col)));
			((JButton) gridArr[row][col].getComponentAt(row,col)).setBackground(new Color(255, 255, 255));
			((JButton) gridArr[row][col].getComponentAt(row,col)).setEnabled(false);
			if(gridArr[row][col-1].getComponentAt(row,col-1).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row, col-1)) == 0)
					zeroMine(row, col-1);
				else
				{
					gridArr[row][col-1].setText(""+mineBoard.fetchSquareCount(new Point(row, col-1)));
					 
					((JButton) gridArr[row][col-1].getComponentAt(row,col-1)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row][col-1].getComponentAt(row,col-1)).setEnabled(false);
				}
				
			}
			if(gridArr[row][col+1].getComponentAt(row,col+1).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row, col+1)) == 0)
					zeroMine(row, col+1);
				else
				{
					gridArr[row][col+1].setText(""+mineBoard.fetchSquareCount(new Point(row, col+1)));
					((JButton) gridArr[row][col+1].getComponentAt(row,col+1)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row][col+1].getComponentAt(row,col+1)).setEnabled(false);
				}
				
			}
			
			if(gridArr[row+1][col-1].getComponentAt(row+1,col-1).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row+1, col-1)) == 0)
					zeroMine(row+1, col-1);
				else
				{
					gridArr[row+1][col-1].setText(""+mineBoard.fetchSquareCount(new Point(row+1, col-1)));
				 	((JButton) gridArr[row+1][col-1].getComponentAt(row+1,col-1)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row+1][col-1].getComponentAt(row+1,col-1)).setEnabled(false);
				}
				
			}
			if(gridArr[row+1][col].getComponentAt(row+1,col).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row+1, col)) == 0)
					zeroMine(row+1, col);
				else
				{
					gridArr[row+1][col].setText(""+mineBoard.fetchSquareCount(new Point(row+1, col)));
				 	((JButton) gridArr[row+1][col].getComponentAt(row+1,col)).setBackground(new Color(255, 255, 255));
				 	((JButton) gridArr[row+1][col].getComponentAt(row+1,col)).setEnabled(false);
				}
				
			}
			
			if(gridArr[row+1][col+1].getComponentAt(row+1,col+1).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row+1, col+1)) == 0)
					zeroMine(row+1, col+1);
				else
				{
					gridArr[row+1][col+1].setText(""+mineBoard.fetchSquareCount(new Point(row+1, col+1)));
				 
					((JButton) gridArr[row+1][col+1].getComponentAt(row+1,col+1)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row+1][col+1].getComponentAt(row+1,col+1)).setEnabled(false);
				}
				
			}
			return;
		}
		if((row <=(Board.MAX_ROWS-2)&& row > 0) && col == 0)
		{
			gridArr[row][col].setText(""+mineBoard.fetchSquareCount(new Point(row, col)));
			((JButton) gridArr[row][col].getComponentAt(row,col)).setBackground(new Color(255, 255, 255));
			((JButton) gridArr[row][col].getComponentAt(row,col)).setEnabled(false);
			if(gridArr[row-1][col].getComponentAt(row-1,col).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row-1, col)) == 0)
					zeroMine(row-1, col);
				else
				{
					gridArr[row-1][col].setText(""+mineBoard.fetchSquareCount(new Point(row-1, col)));
					 
					((JButton) gridArr[row-1][col].getComponentAt(row-1,col)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row-1][col].getComponentAt(row-1,col)).setEnabled(false);
				}
				
			}
				
			if(gridArr[row-1][col+1].getComponentAt(row-1,col+1).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row-1, col+1)) == 0)
					zeroMine(row-1, col+1);
				else
				{
					gridArr[row-1][col+1].setText(""+mineBoard.fetchSquareCount(new Point(row-1, col+1)));
					 
					((JButton) gridArr[row-1][col+1].getComponentAt(row-1,col+1)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row-1][col+1].getComponentAt(row-1,col+1)).setEnabled(false);
				}
				
			}
			if(gridArr[row][col+1].getComponentAt(row,col+1).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row, col+1)) == 0)
					zeroMine(row, col+1);
				else
				{
					gridArr[row][col+1].setText(""+mineBoard.fetchSquareCount(new Point(row, col+1)));
					 
					((JButton) gridArr[row][col+1].getComponentAt(row,col+1)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row][col+1].getComponentAt(row,col+1)).setEnabled(false);
				}
				
			}
			if(gridArr[row+1][col].getComponentAt(row+1,col).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row+1, col)) == 0)
					zeroMine(row+1, col);
				else
				{
					gridArr[row+1][col].setText(""+mineBoard.fetchSquareCount(new Point(row+1, col)));
					 
					((JButton) gridArr[row+1][col].getComponentAt(row+1,col)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row+1][col].getComponentAt(row+1,col)).setEnabled(false);
				}
				
			}
			if(gridArr[row+1][col+1].getComponentAt(row+1,col+1).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row+1, col+1)) == 0)
					zeroMine(row+1, col+1);
				else
				{
					gridArr[row+1][col+1].setText(""+mineBoard.fetchSquareCount(new Point(row+1, col+1)));
					 
					((JButton) gridArr[row+1][col+1].getComponentAt(row+1,col+1)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row+1][col+1].getComponentAt(row+1,col+1)).setEnabled(false);
				}
				
			}
			
			return;
		}
		if(row ==(Board.MAX_ROWS-1) && col == 0)
		{
			gridArr[row][col].setText(""+mineBoard.fetchSquareCount(new Point(row, col)));
			((JButton) gridArr[row][col].getComponentAt(row,col)).setBackground(new Color(255, 255, 255));
			((JButton) gridArr[row][col].getComponentAt(row,col)).setEnabled(false);
			if(gridArr[row-1][col].getComponentAt(row-1,col).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row-1, col)) == 0)
					zeroMine(row-1, col);
				else
				{
					gridArr[row-1][col].setText(""+mineBoard.fetchSquareCount(new Point(row-1, col)));
					 
					((JButton) gridArr[row-1][col].getComponentAt(row-1,col)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row-1][col].getComponentAt(row-1,col)).setEnabled(false);
				}
				
			}
			if(gridArr[row-1][col+1].getComponentAt(row-1,col+1).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row-1, col+1)) == 0)
					zeroMine(row-1, col+1);
				else
				{
					gridArr[row-1][col+1].setText(""+mineBoard.fetchSquareCount(new Point(row-1, col+1)));
					 
					((JButton) gridArr[row-1][col+1].getComponentAt(row-1,col+1)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row-1][col+1].getComponentAt(row-1,col+1)).setEnabled(false);
				}
				
			}
			if(gridArr[row][col+1].getComponentAt(row,col+1).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row, col+1)) == 0)
					zeroMine(row, col+1);
				else
				{
					gridArr[row][col+1].setText(""+mineBoard.fetchSquareCount(new Point(row, col+1)));
					 
					((JButton) gridArr[row][col+1].getComponentAt(row,col+1)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row][col+1].getComponentAt(row,col+1)).setEnabled(false);
				}
				
			}
			return;
		}
		if(row ==(Board.MAX_ROWS-1) &&( col > 0 && col <= Board.MAX_COLS-2))
		{
			gridArr[row][col].setText(""+mineBoard.fetchSquareCount(new Point(row, col)));
			((JButton) gridArr[row][col].getComponentAt(row,col)).setBackground(new Color(255, 255, 255));
			((JButton) gridArr[row][col].getComponentAt(row,col)).setEnabled(false);
			if(gridArr[row-1][col-1].getComponentAt(row-1,col-1).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row-1, col-1)) == 0)
					zeroMine(row-1, col-1);
				else
				{
					gridArr[row-1][col-1].setText(""+mineBoard.fetchSquareCount(new Point(row-1, col-1)));
					 
					((JButton) gridArr[row-1][col-1].getComponentAt(row-1,col-1)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row-1][col-1].getComponentAt(row-1,col-1)).setEnabled(false);
				}
				
			}
			
			if(gridArr[row-1][col].getComponentAt(row-1,col).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row-1, col)) == 0)
					zeroMine(row-1, col);
				else
				{
					gridArr[row-1][col].setText(""+mineBoard.fetchSquareCount(new Point(row-1, col)));
					 
					((JButton) gridArr[row-1][col].getComponentAt(row-1,col)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row-1][col].getComponentAt(row-1,col)).setEnabled(false);
				}
				
			}
			
			if(gridArr[row-1][col+1].getComponentAt(row-1,col+1).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row-1, col+1)) == 0)
					zeroMine(row-1, col+1);
				else
				{
					gridArr[row-1][col+1].setText(""+mineBoard.fetchSquareCount(new Point(row-1, col+1)));
					 
					((JButton) gridArr[row-1][col+1].getComponentAt(row-1,col+1)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row-1][col+1].getComponentAt(row-1,col+1)).setEnabled(false);
				}
				
			}
			
			if(gridArr[row][col-1].getComponentAt(row,col-1).isEnabled())
			{
			
				if(mineBoard.fetchSquareCount(new Point(row, col-1)) == 0)
					zeroMine(row, col-1);
				else
				{
					gridArr[row][col-1].setText(""+mineBoard.fetchSquareCount(new Point(row, col-1)));
					 
					((JButton) gridArr[row][col-1].getComponentAt(row,col-1)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row][col-1].getComponentAt(row,col-1)).setEnabled(false);
				}
			
			}
			if(gridArr[row][col+1].getComponentAt(row,col+1).isEnabled())
			{
			
				if(mineBoard.fetchSquareCount(new Point(row, col+1)) == 0)
					zeroMine(row, col+1);
				else
				{
					gridArr[row][col+1].setText(""+mineBoard.fetchSquareCount(new Point(row, col+1)));
					 
					((JButton) gridArr[row][col+1].getComponentAt(row,col+1)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row][col+1].getComponentAt(row,col+1)).setEnabled(false);
				}
			
			}
			return;
		}
		if(row ==(Board.MAX_ROWS-1) && col == (Board.MAX_COLS-1))
		{
			gridArr[row][col].setText(""+mineBoard.fetchSquareCount(new Point(row, col)));
			((JButton) gridArr[row][col].getComponentAt(row,col)).setBackground(new Color(255, 255, 255));
			((JButton) gridArr[row][col].getComponentAt(row,col)).setEnabled(false);
			if(gridArr[row-1][col-1].getComponentAt(row-1,col-1).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row-1, col-1)) == 0)
					zeroMine(row-1, col-1);
				else
				{
					gridArr[row-1][col-1].setText(""+mineBoard.fetchSquareCount(new Point(row-1, col-1)));
					 
					((JButton) gridArr[row-1][col-1].getComponentAt(row-1,col-1)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row-1][col-1].getComponentAt(row-1,col-1)).setEnabled(false);
				}
				
			}
			
			if(gridArr[row-1][col].getComponentAt(row-1,col).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row-1, col)) == 0)
					zeroMine(row-1, col);
				else
				{
					gridArr[row-1][col].setText(""+mineBoard.fetchSquareCount(new Point(row-1, col)));
					 
					((JButton) gridArr[row-1][col].getComponentAt(row-1,col)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row-1][col].getComponentAt(row-1,col)).setEnabled(false);
				}
				
			}
			
			if(gridArr[row][col-1].getComponentAt(row,col-1).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row, col-1)) == 0)
					zeroMine(row, col-1);
				else
				{
					gridArr[row][col-1].setText(""+mineBoard.fetchSquareCount(new Point(row, col-1)));
					 
					((JButton) gridArr[row][col-1].getComponentAt(row,col-1)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row][col-1].getComponentAt(row,col-1)).setEnabled(false);
				}
				
			}
			
			return;
		}
		if((row <=(Board.MAX_ROWS-2 ) && row >0) && col == (Board.MAX_COLS-1))//**check this cond
		{
			gridArr[row][col].setText(""+mineBoard.fetchSquareCount(new Point(row, col)));
			((JButton) gridArr[row][col].getComponentAt(row,col)).setBackground(new Color(255, 255, 255));
			((JButton) gridArr[row][col].getComponentAt(row,col)).setEnabled(false);
			if(gridArr[row-1][col-1].getComponentAt(row-1,col-1).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row-1, col-1)) == 0)
					zeroMine(row-1, col-1);
				else
				{
					gridArr[row-1][col-1].setText(""+mineBoard.fetchSquareCount(new Point(row-1, col-1)));
					 
					((JButton) gridArr[row-1][col-1].getComponentAt(row-1,col-1)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row-1][col-1].getComponentAt(row-1,col-1)).setEnabled(false);
				}
			}
			
			if(gridArr[row-1][col].getComponentAt(row-1,col).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row-1, col)) == 0)
					zeroMine(row-1, col);
				else
				{
					gridArr[row-1][col].setText(""+mineBoard.fetchSquareCount(new Point(row-1, col)));
					 
					((JButton) gridArr[row-1][col].getComponentAt(row-1,col)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row-1][col].getComponentAt(row-1,col)).setEnabled(false);
				}
			}
			
			if(gridArr[row][col-1].getComponentAt(row,col-1).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row, col-1)) == 0)
					zeroMine(row, col-1);
				else
				{
					gridArr[row][col-1].setText(""+mineBoard.fetchSquareCount(new Point(row, col-1)));
					 
					((JButton) gridArr[row][col-1].getComponentAt(row,col-1)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row][col-1].getComponentAt(row,col-1)).setEnabled(false);
				}
			
			}
			
			if(gridArr[row+1][col-1].getComponentAt(row+1,col-1).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row+1, col-1)) == 0)
					zeroMine(row+1, col-1);
				else
				{
					gridArr[row+1][col-1].setText(""+mineBoard.fetchSquareCount(new Point(row+1, col-1)));
					 
					((JButton) gridArr[row+1][col-1].getComponentAt(row+1,col-1)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row+1][col-1].getComponentAt(row+1,col-1)).setEnabled(false);
				}
			
			}
			
			if(gridArr[row+1][col].getComponentAt(row+1,col).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row+1, col)) == 0)
					zeroMine(row+1, col);
				else
				{
					gridArr[row+1][col].setText(""+mineBoard.fetchSquareCount(new Point(row+1, col)));
					 
					((JButton) gridArr[row+1][col].getComponentAt(row+1,col)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row+1][col].getComponentAt(row+1,col)).setEnabled(false);
				}
			
			}
			
			return;
		}
		if((row <=(Board.MAX_ROWS-2) && row > 0) && (col <= (Board.MAX_COLS-2) && col > 0))
		{
			gridArr[row][col].setText(""+mineBoard.fetchSquareCount(new Point(row, col)));
			((JButton) gridArr[row][col].getComponentAt(row,col)).setBackground(new Color(255, 255, 255));
			((JButton) gridArr[row][col].getComponentAt(row,col)).setEnabled(false);
			if(gridArr[row-1][col-1].getComponentAt(row-1,col-1).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row-1, col-1)) == 0)
					zeroMine(row-1, col-1);
				else
				{
					gridArr[row-1][col-1].setText(""+mineBoard.fetchSquareCount(new Point(row-1, col-1)));
					 
					((JButton) gridArr[row-1][col-1].getComponentAt(row-1,col-1)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row-1][col-1].getComponentAt(row-1,col-1)).setEnabled(false);
				}
			
			}
			
			if(gridArr[row-1][col].getComponentAt(row-1,col).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row-1, col)) == 0)
					zeroMine(row-1, col);
				else
				{
					gridArr[row-1][col].setText(""+mineBoard.fetchSquareCount(new Point(row-1, col)));
					 
					((JButton) gridArr[row-1][col].getComponentAt(row-1,col)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row-1][col].getComponentAt(row-1,col)).setEnabled(false);
				}
			
			}
			
			if(gridArr[row-1][col+1].getComponentAt(row-1,col+1).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row-1, col+1)) == 0)
					zeroMine(row-1, col+1);
				else
				{
					gridArr[row-1][col+1].setText(""+mineBoard.fetchSquareCount(new Point(row-1, col+1)));
					 
					((JButton) gridArr[row-1][col+1].getComponentAt(row-1,col+1)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row-1][col+1].getComponentAt(row-1,col+1)).setEnabled(false);
				}
			
			}
			if(gridArr[row][col+1].getComponentAt(row,col+1).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row, col+1)) == 0)
					zeroMine(row, col+1);
				else
				{
					gridArr[row][col+1].setText(""+mineBoard.fetchSquareCount(new Point(row, col+1)));
					 
					((JButton) gridArr[row][col+1].getComponentAt(row,col+1)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row][col+1].getComponentAt(row,col+1)).setEnabled(false);
				}
			
			}
			if(gridArr[row][col-1].getComponentAt(row,col-1).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row, col-1)) == 0)
					zeroMine(row, col-1);
				else
				{
					gridArr[row][col-1].setText(""+mineBoard.fetchSquareCount(new Point(row, col-1)));
					 
					((JButton) gridArr[row][col-1].getComponentAt(row,col-1)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row][col-1].getComponentAt(row,col-1)).setEnabled(false);
				}
			
			}
			
			if(gridArr[row+1][col+1].getComponentAt(row+1,col+1).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row+1, col+1)) == 0)
					zeroMine(row+1, col+1);
				else
				{
					gridArr[row+1][col+1].setText(""+mineBoard.fetchSquareCount(new Point(row+1, col+1)));
					 
					((JButton) gridArr[row+1][col+1].getComponentAt(row+1,col+1)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row+1][col+1].getComponentAt(row+1,col+1)).setEnabled(false);
				}
			
			}
			if(gridArr[row+1][col-1].getComponentAt(row+1,col-1).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row+1, col-1)) == 0)
					zeroMine(row+1, col-1);
				else
				{
					gridArr[row+1][col-1].setText(""+mineBoard.fetchSquareCount(new Point(row+1, col-1)));
					 
					((JButton) gridArr[row+1][col-1].getComponentAt(row+1,col-1)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row+1][col-1].getComponentAt(row+1,col-1)).setEnabled(false);
				}
			
			}
			if(gridArr[row+1][col].getComponentAt(row+1,col).isEnabled())
			{
				if(mineBoard.fetchSquareCount(new Point(row+1, col)) == 0)
					zeroMine(row+1, col);
				else
				{
					gridArr[row+1][col].setText(""+mineBoard.fetchSquareCount(new Point(row+1, col)));
					 
					((JButton) gridArr[row+1][col].getComponentAt(row+1,col)).setBackground(new Color(255, 255, 255));
					((JButton) gridArr[row+1][col].getComponentAt(row+1,col)).setEnabled(false);
				}
			
			}
			return;
		}
		
	}//zeroMine()

	public static void main(String args[]) {
		// Create a JFrame and init it here
		MinesweeperUI MUI = new MinesweeperUI();
		
		

	}
}