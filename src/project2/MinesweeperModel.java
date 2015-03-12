/**
 * MinesweeperModel.java
 * CS 342 Project 2 - Minesweeper
 */

package project2;

import java.util.Random;

/**
 * This class is the the "model" component of the model view controller
 * software architecture we are using for the game. It contains all the logic
 * and game information necessary to play the game.
 * 
 * Number of Mines and Board Dimension are configurable and will be playable
 * with our BoardView and ButtonController.
 * 
 * @author Anthony Colon, Sujay Patel
 */
public class MinesweeperModel {
	public static final int NUM_OF_MINES = 10;
	public static final int DIMENSION = 10;
    
	private boolean gameover;
    private Minefield[][] minefield;
	
	/**
	 * All possible values at a location in the minefield.
	 */
    public enum Minefield {
    	EMPTY,
    	ONE,
    	TWO,
    	THREE,
    	FOUR,
    	FIVE,
    	SIX,
    	SEVEN,
    	EIGHT,
    	MINE; 
    	
    	/**
    	 * Increments minefield enum
    	 * @return next enum
    	 */
    	private Minefield next() {
            if (this==MINE) return MINE;	//should not be possible to increment MINE in code, but just in case
            return values()[ordinal() + 1];
        }
    }
	
    /**
     * Constructor sets up a new Minesweeper game model.
     * It creates a new board and sets the mines as well as what is displayed
     * at each position of the minefield.
     */
    public MinesweeperModel() {
    	initMinefield();
    	setMines();
    	setMinefieldMetadata();
    }

    /**
     * This method will set each position of the minefield with the
     * proper value that should be displayed when it is revealed.
     */
    private void setMinefieldMetadata() {
		for (int row=0; row<DIMENSION; row++)
			for (int col=0; col<DIMENSION; col++) {
				if (minefield[row][col] == Minefield.MINE)
	            	incrementAround(row, col);
			}
	}

    /**
     * Increments all tiles adjacent to a given location
     * @param row Row
     * @param col Column
     */
	private void incrementAround(int row, int col) {
		int offsetRow, offsetCol;
		
		for (int i=-1; i<2; i++) {
			offsetRow = row+i;
			if (offsetRow<0 || offsetRow>(DIMENSION-1)) continue;	//bounds check

			for (int j=-1; j<2; j++) {
				offsetCol = col+j;
				if (offsetCol<0 || offsetCol>(DIMENSION-1)) continue;	//bounds check

				if (minefield[offsetRow][offsetCol] != Minefield.MINE )	// do not increment Mine
					minefield[offsetRow][offsetCol] = minefield[offsetRow][offsetCol].next();	//increment enum
			} 
		}
	}

	/**
     * Randomly place mines on the field using pseudo random number generator
     */
	private void setMines() {
        Random random = new Random();
        int mineCount = 0;
        
        while (mineCount < NUM_OF_MINES) {
        	int row = random.nextInt(DIMENSION);
            int col = random.nextInt(DIMENSION);
            if (minefield[row][col] != Minefield.MINE) {	//make sure we don't put a mine where one already exists
            	minefield[row][col] = Minefield.MINE;
            	mineCount++;
            }
        }
	}

	/**
	 * Instantiate and initialize the minefield to be an empty grid.
	 */
	private void initMinefield() {
		minefield = new Minefield[DIMENSION][DIMENSION];
		for (int row=0; row<DIMENSION; row++)
			for (int col=0; col<DIMENSION; col++)
				minefield[row][col] = Minefield.EMPTY;
	}
	
	/**
	 * Getter for minefield position
	 * @param row Row
	 * @param col Column
	 * @return Value at position
	 */
	public Minefield getPos(int row, int col) {
		return minefield[row][col];
	}
	
	/**
	 * Prints model to the console.
	 */
	public String toString() {
		String returnString = new String();
		for (int row=0; row<DIMENSION; row++) {
			for (int col=0; col<DIMENSION; col++) {
				returnString = returnString.concat(getChar(row,col) + " ");
			}		
			returnString = returnString.concat("\n");
		}
		return returnString;
	}
	
	/**
	 * Gets the character representation of a given location.
	 * @param row Row
	 * @param col Column
	 * @return Character representation of location
	 */
	public char getChar(int row, int col) {
		return (minefield[row][col] == Minefield.MINE ? 'X': Integer.toString(minefield[row][col].ordinal()).charAt(0));
	}

	/**
	 * Getter for gameover
	 * @return the gameover
	 */
	public boolean isGameover() {
		return gameover;
	}

	/**
	 * Setter for gameover
	 * @param gameover the gameover to set
	 */
	public void setGameover(boolean gameover) {
		this.gameover = gameover;
	}
}