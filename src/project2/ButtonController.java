/**
 * GameController.java
 * CS 342 Project 2 - Minesweeper
 */

package project2;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import project2.MinesweeperModel.Minefield;

/**
 * Control logic for interaction between game model and board view.
 * 
 * @author Anthony Colon, Sujay Patel
 */
public class ButtonController implements MouseListener {

    private int row;
    private int col;
    private BoardView boardView;
    
    private JPanel boardPanel;
    private JButton[][] buttons;
    
    private static final ImageIcon mineImage = new ImageIcon("mine.png");
    private static final ImageIcon flagImage = new ImageIcon("flag.png");
    private static final ImageIcon questionImage = new ImageIcon("question.png");
    
    /**
     * Constructor sets up location information and provides reference for
     * data model.
     * 
     * @param row Row
     * @param col Column
     * @param boardView The BoardView that will be controlled
     */
    public ButtonController(int row, int col, BoardView boardView) {
        this.row = row;
        this.col = col;
        this.boardView = boardView;
        
        this.boardPanel = boardView.getBoardPanel();
        this.buttons = boardView.getButtons();
    }
	
	/**
	 * Automatically reveals any tiles adjacent to an empty tile.
	 * If those tiles are also empty tiles, it will recursively reveal their
	 * adjacent tiles too.
	 *  
	 * @param row Row
	 * @param col Column
	 */
	private void blankFill(int row, int col) {
		int offsetRow, offsetCol;
		
		for (int i=-1; i<2; i++) {
			offsetRow = row+i;
			if (offsetRow<0 || offsetRow>(MinesweeperModel.DIMENSION-1)) continue;	//bounds check			
			for (int j=-1; j<2; j++) {
				offsetCol = col+j;
				if (offsetCol<0 || offsetCol>(MinesweeperModel.DIMENSION-1)) continue;	//bounds check
				
				if (buttons[offsetRow][offsetCol].getText().equals("") && buttons[offsetRow][offsetCol].getIcon()==null) {
				
					buttons[offsetRow][offsetCol].setText(String.valueOf(boardView.getMinefield().getChar(offsetRow, offsetCol)));
					buttons[offsetRow][offsetCol].setEnabled(false);
					boardView.decrementButtonCount();
					
					if (boardView.getMinefield().getPos(offsetRow, offsetCol) == Minefield.EMPTY) {
						blankFill(offsetRow, offsetCol);	//recursively fill other blank tiles
					}
					
					buttons[offsetRow][offsetCol].invalidate();
				}
			} 
		}
	}


    /**
     * Action for left or right clicking on a button.
     */
	@Override
	public void mouseClicked(MouseEvent e) {
		if (boardView.getMinefield().isGameover()) return;	//do not allow commands on finished game
		boardView.startTimer(); //starts timer if it has not already been started
		
		JButton button = (JButton)e.getSource();
		
		if (e.getButton() == MouseEvent.BUTTON1) {	//left click				
			if (!button.getText().equals("") || button.getIcon()!=null) return;	//do not handle a button press on a revealed/flagged button
			
			MinesweeperModel.Minefield locValue = boardView.getMinefield().getPos(row, col);
			if (locValue == MinesweeperModel.Minefield.MINE) {
				stopGame();
				revealMines();
				JOptionPane.showMessageDialog(boardPanel, "You blew the mines. Game Over!\n\nThank you for playing.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
				//code for displaying image can be placed here. (extra credit)
				return;
			} else if (locValue == MinesweeperModel.Minefield.EMPTY) {
				blankFill(row, col);
			} else {
				button.setText(String.valueOf(boardView.getMinefield().getChar(row, col)));
				button.setEnabled(false);
				boardView.decrementButtonCount();
			}

			if (boardView.getButtonCount()==0) {
				stopGame();
				revealMines();
				int score = boardView.getTime();
				String winMessage = new String("You won!\n" + "Time: " + score + " seconds.\n\nThank you for playing.");
				if (boardView.getTopScores().isTopScore(score)) {
					String name = JOptionPane.showInputDialog(boardPanel, winMessage + "\n\nEnter your name: ", "New High Score!", JOptionPane.QUESTION_MESSAGE);
					if (name==null || name.isEmpty()) name = new String("Guest");	//default name if one was not entered
					boardView.getTopScores().addScore(name, score);
					boardView.getTopScores().saveScores();
				} else {
					JOptionPane.showMessageDialog(boardPanel, winMessage, "You won!", JOptionPane.INFORMATION_MESSAGE );
				}
			}

			button.invalidate();
			boardPanel.revalidate();
			boardPanel.repaint();
		} else if (e.getButton() == MouseEvent.BUTTON3) {	//right click
			if (!button.getText().equals("")) return;	//do not handle a right button press on a revealed button
			toggleButton(button);
		}
	}

	/**
	 * Reveals all mines on board. If the game was won, all the mines are marked "M"
	 * If the game is lost, all the mines are indicated with an image.
	 */
	private void revealMines() {
		for (int i=0; i<MinesweeperModel.DIMENSION; i++) {
			for (int j=0; j<MinesweeperModel.DIMENSION; j++) {
				if (boardView.getMinefield().getPos(i, j) == MinesweeperModel.Minefield.MINE) {
					if (boardView.getButtonCount()!=0) 
						buttons[i][j].setIcon(mineImage);
					else {
						buttons[i][j].setIcon(flagImage);
						if (boardView.getMineCount()!=0) boardView.decrementMineCount();
					}
				}
			}
		}		
	}

	/**
	 * Toggles the state of the button between "", "M", and "?"
	 * @param button The button to toggle
	 */
	private void toggleButton(JButton button) {
		if (button.getIcon()==null) {
			if (boardView.getMineCount() != 0) {
				button.setIcon(flagImage);
				boardView.decrementMineCount();
			} else {
				button.setIcon(questionImage);
			}
		} else if (button.getIcon()==flagImage) {
			button.setIcon(questionImage);
			boardView.incrementMineCount();
		} else if (button.getIcon()==questionImage) {
			button.setIcon(null);
		}
	}
	
	/**
	 * Halts timer and marks game as finished
	 */
	private void stopGame() {
		boardView.stopTimer();
		boardView.getMinefield().setGameover(true);
	}

	//These methods are empty, but required for using MouseListener interface
	@Override
	public void mousePressed(MouseEvent e) { }

	@Override
	public void mouseReleased(MouseEvent e) { }

	@Override
	public void mouseEntered(MouseEvent e)  { }

	@Override
	public void mouseExited(MouseEvent e) { }
}
