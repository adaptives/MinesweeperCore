/**
 * 
 */
package com.diycomputerscience.minesweepergui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.diycomputerscience.minesweepercore.Board;
import com.diycomputerscience.minesweepercore.ConfigUtils;
import com.diycomputerscience.minesweepercore.PersistenceException;
import com.diycomputerscience.minesweepercore.Point;
import com.diycomputerscience.minesweepercore.RandomBoardInitializer;
import com.diycomputerscience.minesweepercore.UncoveredMineException;
/**
 * @author pshah
 * 
 */

public class MinesweeperUI extends JFrame {
	
	private static final long serialVersionUID = 1L;
	final int row = 6;
	final int col = 6;
	
	private Board mineBoard;
		
	private static final Logger cLogger = Logger.getLogger(MinesweeperUI.class);
	private ResourceBundle resourceBundle;
	
	public MinesweeperUI() {
		try {
			this.resourceBundle = ResourceBundle.getBundle("MessageBundle");
		} catch(MissingResourceException mre) {
			cLogger.warn("Could not locate MessageBundle file" + mre);
		}
		this.setTitle(resourceBundle.getString("name"));
		this.setLayout(new GridLayout(1, 1));
		mineBoard = new Board(new RandomBoardInitializer());
		this.setMineLayout(mineBoard);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setJMenuBar(buildMenuBar());		
	}

	/**
	 * This method sets the layoutManager to Panel and creates the Buttons for
	 * the grid
	 */
	private void setMineLayout(Board mineBoard) {									
		JPanel panel;
		panel = new JPanel();
		panel.setLayout(new GridLayout(row, col));

		/** In this for loop create the Buttons and populate the Button Array */
		JButton gridButton;
		
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				gridButton = new JButton("");
				gridButton.setName(i+","+j);
				gridButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent me) {
						int option =0;
						String buttonName = ((JButton)me.getComponent()).getName();
						StringBuffer strbuff = new StringBuffer(buttonName);
						int index = strbuff.indexOf(",");
						int i = Integer.parseInt(((String)strbuff.substring(0,index)));
						int j = Integer.parseInt(((String)strbuff.substring(index+1)).toString());
						if (SwingUtilities.isLeftMouseButton(me)) {
							try {												
								Component comArr = MinesweeperUI.this.getContentPane().getComponent(0);
								Component com []= ((JPanel)comArr).getComponents();
								MinesweeperUI.this.uncoverSquareUI(com, i, j);								
							} catch (UncoveredMineException ue) {					
								// Exit the game
								option = JOptionPane.showConfirmDialog(MinesweeperUI.this,
																	   resourceBundle.getString("gameover.dialogue.msg"),
																	   resourceBundle.getString("gameover.dialogue.msg.title"),
																	   JOptionPane.YES_NO_OPTION);
								if(option == JOptionPane.NO_OPTION) {
									System.exit(0);
								}	
								else {
									MinesweeperUI.this.getContentPane().removeAll();
									MinesweeperUI.this.invalidate();
									MinesweeperUI.this.mineBoard = new Board(new RandomBoardInitializer());
									MinesweeperUI.this.setMineLayout(MinesweeperUI.this.mineBoard);									
								}
							}							
						} else {
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

	
	private JMenuBar buildMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu file = new JMenu("File");
		JMenuItem fileSave = new JMenuItem("Save");
		fileSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					mineBoard.save();
				} catch(PersistenceException pe) {
					cLogger.warn("Could not save the game", pe);
				}
			}		
		});
		JMenuItem fileLoad = new JMenuItem("Load");
		fileLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					MinesweeperUI.this.getContentPane().removeAll();
					MinesweeperUI.this.invalidate();
					MinesweeperUI.this.mineBoard.load();
					MinesweeperUI.this.setMineLayout(MinesweeperUI.this.mineBoard);
				} catch(PersistenceException pe) {
					//TODO: error dialogue
					//TODO: This button should be enabled only if a previously saved state exists
					cLogger.warn("Could not load game from previously saved state");
				}
			}			
		});
		JMenuItem close = new JMenuItem("Close");
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {			
				System.exit(0);
			}		
		});
		file.add(fileSave);
		file.add(fileLoad);
		file.add(close);
		menuBar.add(file);
		
		JMenu help = new JMenu("Help");
		JMenuItem helpAbout = new JMenuItem("About");
		help.add(helpAbout);
		menuBar.add(help);
		
		return menuBar;
	}

	/**
	 * 
	 * @param com Contains the array of JButton components
	 * @param row the row on which user has clicked
	 * @param col the col on which user has clicked
	 * 
	 * This method match the row and column with the button name and if found it sets the minecount
	 * and changes its background color and enabled= false
	 * @throws UncoveredMineException 
	 */
	private void uncoverSquareUI(Component com[],int row,int col) throws UncoveredMineException {
		for(int a=0; a < com.length;a++) {
			if(com[a].getName().equals(row+","+col)) {
				((JButton)com[a]).setText(""+mineBoard.fetchSquareCount(new Point(row, col)));
				((JButton)com[a]).setBackground(new Color(255, 255, 255));
				((JButton)com[a]).setEnabled(false);
				Point p = new Point(row, col);
				this.mineBoard.uncoverSquare(p);
				if(this.mineBoard.fetchSquareCount(p) == 0) {
					List<Point> neighbours = this.mineBoard.neighbours(p);
					for(Point zeroP : neighbours) {
						if(componentEnabled(com, zeroP.row, zeroP.col)) {
							uncoverSquareUI(com, zeroP.row, zeroP.col);
						}						
					}
				}
				return;
			}	
		}
	}
	
	private boolean componentEnabled(Component com[],int row,int col) {
		for(int a=0; a < com.length;a++) {
			if(com[a].getName().equals(row+","+col)) {
				if(((JButton)com[a]).isEnabled()) {
					return true;
				}
			}
		
		}			
		return false;
	}
		
	public static void main(String args[]) {
		ConfigUtils.initConfigDirectory();		
		// Create a JFrame and init it here
		MinesweeperUI MUI = new MinesweeperUI();
		MUI.setSize(300, 300);
		MUI.setVisible(true);
		
	}
}
