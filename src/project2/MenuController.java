/**
 * MenuController.java
 * CS 342 Project 2 - Minesweeper
 */

package project2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

/**
 * Controller for menus. Controls logic of what happens when menu items are clicked
 * 
 * @author Anthony Colon, Sujay Patel
 */
public class MenuController implements ActionListener {
	private MenuCommand menuCommand;
	private BoardView boardView;

	public enum MenuCommand {
		RESET,
		TOP_TEN,
		RESET_SCORE,
		EXIT,
		HELP,
		ABOUT
	}

	public MenuController(BoardView boardView, MenuCommand menuCommand) {
		this.boardView = boardView;
		this.menuCommand = menuCommand;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (menuCommand) {
		case RESET:
			boardView.resetBoard();
			break;
		case TOP_TEN:
			JOptionPane.showMessageDialog(boardView.getBoardPanel(), "Top Ten Scores:\n" + boardView.getTopScores(), "Top Ten Scores", JOptionPane.INFORMATION_MESSAGE);
			break;
		case EXIT:
			System.exit(0);
			break;
		case HELP:
			JOptionPane.showMessageDialog(boardView.getBoardPanel(), "How to play?\n\nThe rules in Minesweeper are simple:\nReveal a mine, and the game ends.\n" +
					"Reveal an empty square, and you keep playing.\nReveal a number, and it tells you how many mines lay hidden in the eight surrounding squares.\n" +
					"You use this information to deduce which nearby squares are safe to click.\n\nNot sure where the mines are?\n\n" +
					"Mark the mines using the right-click.\nIf you suspect a square conceals a mine, right-click it. This puts a flag on the square.\n" +
					"If you're not sure, right-click again to make it a question mark.\nRight clicking one more time will restore the square.","Help", JOptionPane.INFORMATION_MESSAGE);
			break;
		case ABOUT:
			JOptionPane.showMessageDialog(boardView.getBoardPanel(), "CS 342 Project 2 - Minesweeper\nSpring 2014\nUniversity of Illinois at Chicago" + 
					"\n\nAuthors:\nAnthony Colon\nacolon8@uic.edu\n\nSujay Patel\nspate292@uic.edu", "About", JOptionPane.INFORMATION_MESSAGE);
			break;
		case RESET_SCORE:
			boardView.getTopScores().clearScores();
			boardView.getTopScores().saveScores();
			JOptionPane.showMessageDialog(boardView.getBoardPanel(), "The Top Ten scores have been reset.", "Scores Reset", JOptionPane.INFORMATION_MESSAGE);
			break;
		default:
			break;
		}

	}

}
