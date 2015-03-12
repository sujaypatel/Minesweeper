/**
 * Game.java
 * CS 342 Project 2 - Minesweeper
 */

package project2;

/**
 * Starts an instance of the minesweeper game.
 * 
 * @author Anthony Colon, Sujay Patel
 */
public class Game {
	public static void main(String[] args) {
		new BoardView(new MinesweeperModel());
	}
}
