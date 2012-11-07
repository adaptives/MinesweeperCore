/**
 * 
 */
package com.diycomputerscience.minesweepergui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

public class MinesweeperUI extends JFrame //implements MouseListener 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final int row = 6;
	final int col = 6;
	
	private Board mineBoard;
	

	
	public MinesweeperUI() 
	{
		this.setTitle("Minesweeper");
		this.setLayout(new GridLayout(1, 1));
		mineBoard = new Board(new RandomBoardInitializer());
		this.setMineLayout(mineBoard);
		this.addWindowListener();
		
	}

	/**
	 * This method sets the layoutManager to Panel and creates the Buttons for
	 * the grid
	 */
	private void setMineLayout(Board mineBoard) 
	{
		JPanel panel;
		panel = new JPanel();
		panel.setLayout(new GridLayout(row, col));

		/** In this for loop create the Buttons and populate the Button Array */
		JButton gridButton;
		
		for (int i = 0; i < row; i++) 
		{
			for (int j = 0; j < col; j++) 
			{
				gridButton = new JButton("");
				gridButton.setName(i+","+j);
				gridButton.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent me) 
					{
						int option =0;
						String buttonName = ((JButton)me.getComponent()).getName();
						StringBuffer strbuff = new StringBuffer(buttonName);
						int index = strbuff.indexOf(",");
						int i = Integer.parseInt(((String)strbuff.substring(0,index)));
						int j = Integer.parseInt(((String)strbuff.substring(index+1)).toString());
						if (SwingUtilities.isLeftMouseButton(me)) 
						{
							try
							{
										
								MinesweeperUI.this.mineBoard.uncoverSquare(new Point(i, j));
								
							} catch (UncoveredMineException ue) 
							{					
								
							}
							if (MinesweeperUI.this.mineBoard.isSquareMine(new Point(i, j))) 
							{
								// Exit the game
								option = JOptionPane.showConfirmDialog(MinesweeperUI.this,"You have Lost The Game,want to play again","Really Quit",JOptionPane.YES_NO_OPTION);
								if(option == JOptionPane.NO_OPTION)
								{
									MinesweeperUI.this.dispose();
								}	
								else
								{
									MinesweeperUI.this.getContentPane().removeAll();
									MinesweeperUI.this.invalidate();
									MinesweeperUI.this.mineBoard = new Board(new RandomBoardInitializer());
									MinesweeperUI.this.setMineLayout(MinesweeperUI.this.mineBoard);
									
								}
							}
							else
							 {
								Component comArr = MinesweeperUI.this.getContentPane().getComponent(0);
								Component com []= ((JPanel)comArr).getComponents();
								if(MinesweeperUI.this.isMineCountZero(com,i,j))
								{
									//The MineCount is Zero 
									MinesweeperUI.this.zeroMine(i, j);
								}
							}
						}

						else {
							// if rightButton clicked
							if (SwingUtilities.isRightMouseButton(me))
							{
								((JButton) me.getComponent()).setBackground(new Color(255, 0, 0));
								
								MinesweeperUI.this.mineBoard.markSquareAsMine(new Point(i,j));
							}

						}
				 		
					}//end of mouseclicked()


 
				});
				
				panel.add(gridButton);
			}
		}
		this.getContentPane().add(panel);
		this.validate();
		
	}// setMineLayout()

	
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
	
	/**
	 * 
	 * @param com Contains the array of JButton components
	 * @param row the row on which user has clicked
	 * @param col the col on which user has clicked
	 * 
	 * This method match the row and column with the button name and if found it sets the minecount
	 * and changes its background color and enabled= false
	 */
	private void checkButtonName(Component com[],int row,int col)
	{
		for(int a=0; a < com.length;a++)
		{
			if(com[a].getName().equals(row+","+col))
			{
				((JButton)com[a]).setText(""+mineBoard.fetchSquareCount(new Point(row, col)));
				((JButton)com[a]).setBackground(new Color(255, 255, 255));
				((JButton)com[a]).setEnabled(false);
				return;
			}	
		}
	}//end of checkButtonName()
	
	/**
	 * 
	 * @param com Contains the array of JButton components
	 * @param row the row on which user has clicked
	 * @param col the col on which user has clicked
	 * @return
	 */
	private boolean isMineCountZero(Component com[],int row,int col)
	{
		for(int a=0; a < com.length;a++)
		{
			if(com[a].getName().equals(row+","+col))
			{
				if(mineBoard.fetchSquareCount(new Point(row, col))!=0)
				{
					((JButton)com[a]).setText(""+ mineBoard.fetchSquareCount(new Point(row, col)));
					((JButton)com[a]).setBackground(new Color(255, 255, 255));
					((JButton)com[a]).setEnabled(false);
					
					return false;
				}
				
			}	
			
		}
		return true;
	}//isMineCountZero()
	
	private boolean componentEnabled(Component com[],int row,int col)
	{
		for(int a=0; a < com.length;a++)
		{
			if(com[a].getName().equals(row+","+col))
			{
				if(((JButton)com[a]).isEnabled())
				{
					return true;
				}
			}//if
		
		}//for	
		
		return false;
	}
	
	private void fetchAndCall(Component com[],int row,int col)
	{
		if(mineBoard.fetchSquareCount(new Point(row, col)) == 0)
		{
			zeroMine(row, col);
		}
		else
		{
			this.checkButtonName(com,row,col);
		}
	}//end of fetchAndCall
	
	private void zeroMine(int row,int col)
	{
		Component comArr = this.getContentPane().getComponent(0);
		Component com []= ((JPanel)comArr).getComponents();
	
		if(row == 0 && col == 0)
		{
			this.checkButtonName(com,row,col);	
		
			if(this.componentEnabled(com,row,col+1))
			{	
				this.fetchAndCall(com,row,col+1);
			
			}
				
			if(this.componentEnabled(com,row+1,col))
			{	
			
				this.fetchAndCall(com,row+1,col);
			}
			
			if(this.componentEnabled(com,row+1,col+1))
			{
				this.fetchAndCall(com,row+1,col+1);
			}
			return;
		}
		

		if(row ==0 && col == Board.MAX_COLS-1)
		{
				this.checkButtonName(com,row,col);
				if(this.componentEnabled(com,row,col-1))
				{
					this.fetchAndCall(com,row,col-1);
				}
				
				if(this.componentEnabled(com,row+1,col-1))
				{
					this.fetchAndCall(com,row+1,col-1);
				}
				
				if(this.componentEnabled(com,row+1,col))
				{
					this.fetchAndCall(com,row+1,col);
				}
				
				return;
			}
		if(row == 0 && (col > 0 && col <= ( Board.MAX_COLS-2)) )
		{
			
			this.checkButtonName(com,row,col);
			if(this.componentEnabled(com,row,col-1))
			{
				this.fetchAndCall(com,row,col-1);
			}
			if(this.componentEnabled(com,row,col+1))
			{
				this.fetchAndCall(com,row,col+1);
			}
			
			if(this.componentEnabled(com,row+1,col-1))
			{
				this.fetchAndCall(com,row+1,col-1);
			}
			if(this.componentEnabled(com,row+1,col))
			{
				this.fetchAndCall(com,row+1,col);
			}
			
			if(this.componentEnabled(com,row+1,col+1))
			{
				this.fetchAndCall(com,row+1,col+1);
			}
			return;
		}
		if((row <=(Board.MAX_ROWS-2)&& row > 0) && col == 0)
		{
			
			this.checkButtonName(com,row,col);
			
			
			if(this.componentEnabled(com,row-1,col))
			{
				this.fetchAndCall(com,row-1,col);
			}
				
			
			if(this.componentEnabled(com,row-1,col+1))
			{
				this.fetchAndCall(com,row-1,col+1);
			}
			
			if(this.componentEnabled(com,row,col+1))
			{
				this.fetchAndCall(com,row,col+1);
			}
			
			if(this.componentEnabled(com,row+1,col))
			{
				this.fetchAndCall(com,row+1,col);
			}
			if(this.componentEnabled(com,row+1,col+1))
			{
				this.fetchAndCall(com,row+1,col+1);
			}
			
			return;
		}
		if(row ==(Board.MAX_ROWS-1) && col == 0)
		{
			this.checkButtonName(com,row,col);
			
		
			if(this.componentEnabled(com,row-1,col))
			{
				this.fetchAndCall(com,row-1,col);
			}
		
			if(this.componentEnabled(com,row-1,col+1))
			{
				this.fetchAndCall(com,row-1,col+1);
			}
			
			if(this.componentEnabled(com,row,col+1))
			{
				this.fetchAndCall(com,row,col+1);
			}
			return;
		}
		if(row ==(Board.MAX_ROWS-1) &&( col > 0 && col <= Board.MAX_COLS-2))
		{
			this.checkButtonName(com,row,col);
			
			
			if(this.componentEnabled(com,row-1,col-1))
			{
				this.fetchAndCall(com,row-1,col-1);
			}
			
			
			if(this.componentEnabled(com,row-1,col))
			{
				this.fetchAndCall(com,row-1,col);
			}
			
			
			if(this.componentEnabled(com,row-1,col+1))
			{
				this.fetchAndCall(com,row-1,col+1);
			}
			
			
			if(this.componentEnabled(com,row,col-1))
			{
			
				this.fetchAndCall(com,row,col-1);
			}
			
			if(this.componentEnabled(com,row,col+1))
			{
				this.fetchAndCall(com,row,col+1);
			
			}
			return;
		}
		if(row ==(Board.MAX_ROWS-1) && col == (Board.MAX_COLS-1))
		{
			
			this.checkButtonName(com,row,col);
			
			if(this.componentEnabled(com,row-1,col-1))
			{
				this.fetchAndCall(com,row-1,col-1);
			}
			
			if(this.componentEnabled(com,row-1,col))
			{
				this.fetchAndCall(com,row-1,col);
			}
			
			if(this.componentEnabled(com,row,col-1))
			{
				this.fetchAndCall(com,row,col-1);
			}
			
			return;
		}
		if((row <=(Board.MAX_ROWS-2 ) && row >0) && col == (Board.MAX_COLS-1))//**check this cond
		{
			this.checkButtonName(com,row,col);
			
			if(this.componentEnabled(com,row-1,col-1))
			{
				this.fetchAndCall(com,row-1,col-1);
			}
			
			if(this.componentEnabled(com,row-1,col))
			{
				this.fetchAndCall(com,row-1,col);
			}
			
			if(this.componentEnabled(com,row,col-1))
			{
				this.fetchAndCall(com,row,col-1);
			}
			
			
			if(this.componentEnabled(com,row+1,col-1))
			{
				this.fetchAndCall(com,row+1,col-1);
			}
			
			if(this.componentEnabled(com,row+1,col))
			{
				this.fetchAndCall(com,row+1,col);
			
			}
			
			return;
		}
		if((row <=(Board.MAX_ROWS-2) && row > 0) && (col <= (Board.MAX_COLS-2) && col > 0))
		{
			this.checkButtonName(com,row,col);
			
			if(this.componentEnabled(com,row-1,col-1))
			{
				this.fetchAndCall(com,row-1,col-1);
			}
			
			if(this.componentEnabled(com,row-1,col))
			{
				this.fetchAndCall(com,row-1,col);
			}
			
			if(this.componentEnabled(com,row-1,col+1))
			{
				this.fetchAndCall(com,row-1,col+1);
			}
			
			if(this.componentEnabled(com,row,col+1))
			{
				this.fetchAndCall(com,row,col+1);
			}
			
			if(this.componentEnabled(com,row,col-1))
			{
				this.fetchAndCall(com,row,col-1);
			}
			
			
			if(this.componentEnabled(com,row+1,col+1))
			{
				this.fetchAndCall(com,row+1,col+1);
			}
			
			if(this.componentEnabled(com,row+1,col-1))
			{
				this.fetchAndCall(com,row+1,col-1);
			}
			
			if(this.componentEnabled(com,row+1,col))
			{
				this.fetchAndCall(com,row+1,col);
			}
			return;
		}
		
	}//zeroMine()

	public static void main(String args[]) {
		// Create a JFrame and init it here
		MinesweeperUI MUI = new MinesweeperUI();
		MUI.setSize(300, 300);
		MUI.setVisible(true);
		
	}//main()
}//class
