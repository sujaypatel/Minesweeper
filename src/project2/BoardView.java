/**
 * BoardView.java
 * CS 342 Project 2 - Minesweeper
 */

package project2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
 
/**
 * Provides the GUI and GUI display logic
 * 
 * @author Anthony Colon, Sujay Patel
 */
public class BoardView {	
	private MinesweeperModel minefield;
	private ScoreModel topScores;
	
	private Container container;
	private JFrame frame;
	private JButton buttons[][], resetButton;
	private JLabel timerLabel;
	private JLabel minesLabel;
	private JPanel boardPanel;

	private Timer timer;
	private boolean timerRunning;

	private int secCounter;
	private int mineCounter;
	private int buttonCounter;
	
	/**
	 * Constructor creates an instance of the game board in a GUI 
	 * @param minefield Minesweeper board to operate game on
	 */
	public BoardView(MinesweeperModel minefield) {
		this.minefield = minefield;	//define game model
		this.topScores = new ScoreModel();	//define score model
		this.timerRunning = false;
		
		// To set name of the window(game), we will use java swing function, JFrame
		frame = new JFrame("Minesweeper");
		container = frame.getContentPane();		

		createMenus();

		timerLabel = new JLabel("Time: 0");       		// Timer for placing the Timer indicating the number of occurring seconds
		timerLabel.setBackground(Color.BLACK);
		timerLabel.setForeground(Color.WHITE);
		timerLabel.setFont(new Font(null, Font.BOLD, 30));
		timerLabel.setBorder(BorderFactory.createLoweredBevelBorder());

		frame.setSize(600, 600);      	// Sets the size of the board

		minesLabel = new JLabel();    // This will let the user know the number of Mines the game contains
		minesLabel.setForeground(Color.WHITE);
		minesLabel.setBackground(Color.BLACK);
		minesLabel.setFont(new Font(null, Font.BOLD, 30));
		minesLabel.setBorder(BorderFactory.createLoweredBevelBorder());		
	
		/** Now, setting up the visual part of the game i.e. Boxes and Reset Mode using JButton */        
		resetButton = new JButton("Reset");
		resetButton.setForeground(Color.BLACK);
		resetButton.setBackground(Color.WHITE);
		resetButton.setFont(new Font(null, Font.BOLD, 30));
		resetButton.addActionListener(new MenuController(this, MenuController.MenuCommand.RESET));	//Reset button does the same thing as clicking Reset menu item      
		
		this.createGrid();
		this.resetBoard();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	
	/**
	 * Creates and adds all menus. Logic is handled in MenuController
	 */
	private void createMenus() {
		// Now, creating Menus and Menus' lists using JMenuBar & JMenu
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);     // Sets the frame into the menu
		
		//Game Menu
		JMenu menuGame = new JMenu("Game");
		JMenuItem itemReset = new JMenuItem("Reset", KeyEvent.VK_R);
		JMenuItem itemTopTen = new JMenuItem("Top Ten", KeyEvent.VK_T);
		JMenuItem itemResetScore = new JMenuItem("Reset Top Ten", KeyEvent.VK_S);
		
		JMenuItem itemExit = new JMenuItem("Exit", KeyEvent.VK_X);

		itemReset.addActionListener(new MenuController(this, MenuController.MenuCommand.RESET));
		itemTopTen.addActionListener(new MenuController(this, MenuController.MenuCommand.TOP_TEN));
		itemExit.addActionListener(new MenuController(this, MenuController.MenuCommand.EXIT));
		itemResetScore.addActionListener(new MenuController(this, MenuController.MenuCommand.RESET_SCORE));

		menuBar.add(menuGame);
		menuGame.setMnemonic(KeyEvent.VK_G);

		menuGame.add(itemReset);
		menuGame.add(itemTopTen);
		menuGame.addSeparator();
		menuGame.add(itemResetScore);
		menuGame.addSeparator();
		menuGame.add(itemExit);

		//Help Menu
		JMenu menuHelp = new JMenu("Help");
		JMenuItem itemHelp = new JMenuItem("Help", KeyEvent.VK_L);
		JMenuItem itemAbout = new JMenuItem("About", KeyEvent.VK_A);

		itemHelp.addActionListener(new MenuController(this, MenuController.MenuCommand.HELP));
		itemAbout.addActionListener(new MenuController(this, MenuController.MenuCommand.ABOUT));

		menuBar.add(menuHelp);
		menuHelp.setMnemonic(KeyEvent.VK_H);

		menuHelp.add(itemHelp);
		menuHelp.addSeparator();
		menuHelp.add(itemAbout);		
	}

	/**
	 * Creates all the buttons and assigns button controller
	 */
	private void createGrid() {		
		buttons = new JButton[MinesweeperModel.DIMENSION][MinesweeperModel.DIMENSION];			// To create the Panels(Board) of 10x10 i.e. equals to 100 Panels in the game

		// Now, creating a 10x10 Grid panels for the game
		boardPanel = new JPanel(new GridLayout(buttons.length, buttons.length));

		for (int row=0; row<MinesweeperModel.DIMENSION; row++) {
			for (int col=0; col<MinesweeperModel.DIMENSION; col++) {
				buttons[row][col] = new JButton();
				buttons[row][col].addMouseListener(new ButtonController(row, col, this));
				buttons[row][col].setBackground(Color.WHITE);       // You can change the color to your favorite one, if you want ;-) 
				boardPanel.add(buttons[row][col]);
			}
		}		       					

		// We imported the class java.awt.GridLayout to make a Grid for the game
		JPanel topPanel = new JPanel(new GridLayout(1,3));
		topPanel.setBackground(Color.BLACK);
		topPanel.add(minesLabel);
		topPanel.add(resetButton);   
		topPanel.add(timerLabel);

		container.setLayout(new BorderLayout());
		container.add(boardPanel, BorderLayout.CENTER); 
		container.add(topPanel, BorderLayout.NORTH);		
	}

	/**
	 * Updates Mines remaining label
	 */
	private void updateMinesLabel() {
		minesLabel.setText("Mines: " + mineCounter);
	}
	
	/**
	 * Increments Mines remaining
	 */
	public void incrementMineCount() {
		mineCounter++;
		updateMinesLabel();
	}
	
	/**
	 * Decrement Mines remaining
	 */
	public void decrementMineCount() {
		mineCounter--;
		updateMinesLabel();
	}
	
	/**
	 * Resets Mines remaining counter
	 */
	public void resetMineCount() {
		mineCounter = MinesweeperModel.NUM_OF_MINES;
		updateMinesLabel();
	}
	
	/**
	 * Starts timer if it is not running
	 */
	public void startTimer() {
		if (!timerRunning) {
			timer = new Timer();
			timer.schedule(new DisplayTime(), 1000, 1000);
			timerRunning = true;
		}
	}

	/**
	 * Stops timer if it is running
	 */
	public void stopTimer() {
		if (timerRunning) {
			timer.cancel();
			timerRunning = false;
		}
	}

	/**
	 * Resets Timer
	 */
	public void resetTimer() {
		secCounter = 0;
		timerLabel.setText("Time: 0");
	}

	/**
	 * Time elapsed since first button was clicked
	 * @return time elapsed
	 */
	public int getTime() {
		return secCounter;
	}

	public JPanel getBoardPanel() {
		return boardPanel;
	}

	public JButton[][] getButtons() {
		return buttons;
	}

	/**
	 * Decrements buttons remaining counter
	 */
	public void decrementButtonCount() {
		buttonCounter--;
	}
	
	/**
	 * Getter for buttonCounter
	 * @return buttons remaining count
	 */
	public int getButtonCount() {
		return buttonCounter;
	}
	
	/**
	 * Resets buttons remaining counter
	 */
	public void resetButtonCount() {
		buttonCounter = (MinesweeperModel.DIMENSION * MinesweeperModel.DIMENSION) - MinesweeperModel.NUM_OF_MINES;
	}
	
	/**
	 * Resets the board to make it ready for a new game.
	 */
	public void resetBoard() {
		setMinefield(new MinesweeperModel());
		
		for (int row=0; row<MinesweeperModel.DIMENSION; row++) {
			for (int col=0; col<MinesweeperModel.DIMENSION; col++) {
				buttons[row][col].setText("");
				buttons[row][col].setIcon(null);
				buttons[row][col].setEnabled(true);
			}
		}	
		
		this.stopTimer();
		this.resetButtonCount();	
		this.resetMineCount();
		this.resetTimer();
		
		frame.repaint();
		boardPanel.repaint();
	}
	
	/**
	 * Getter for minefield
	 * @return the minefield
	 */
	public MinesweeperModel getMinefield() {
		return minefield;
	}

	/**
	 * Setter for minefield
	 * @param minefield the minefield to set
	 */
	public void setMinefield(MinesweeperModel minefield) {
		this.minefield = minefield;
	}

	/**
	 * Control logic for the game timer
	 */
	private class DisplayTime extends TimerTask {    
		/**
		 * Code to run periodically in timer
		 */
		public void run() {
			EventQueue.invokeLater(new Runnable() {
				/**
				 * Increments timer
				 */
				public void run() {
					timerLabel.setText(String.valueOf("Time: "+(++secCounter)));
				} 
			});
		}
	}

	/**
	 * Getter for mineCounter
	 * @return value of mineCounter
	 */
	public int getMineCount() {
		return mineCounter;
	}

	/**
	 * Getter for topScores
	 * @return the topScores
	 */
	public ScoreModel getTopScores() {
		return topScores;
	}

}

