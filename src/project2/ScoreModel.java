/**
 * ScoreModel.java
 * CS 342 Project 2 - Minesweeper
 */
package project2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model component of top scores list
 * 
 * @author Anthony Colon, Sujay Patel
 */
public class ScoreModel implements Serializable {
	private static final String FILENAME = "scores.bin";
	private static final long serialVersionUID = 1L;
	private static final int MAX_LIST_SIZE = 10;
	
	private List<userScore> scoreList;
	
	/**
	 * Constructor loads the old score file or sets a new list if it has not been created
	 */
	@SuppressWarnings("unchecked")	//we are sure this is going to be a List
	public ScoreModel() {
		try {
			//Try to load scoresList from file
			FileInputStream fis = new FileInputStream(FILENAME);
			ObjectInputStream ois = new ObjectInputStream(fis);
			scoreList = (List<userScore>) ois.readObject();
			ois.close();
		} catch (Exception e) {
			//Doesn't exist or can't load, make new list
			scoreList = new ArrayList<userScore>();
		}
		
	}
	
	/**
	 * Add new score to list and removes last entry if the new score belongs in the top 10.
	 * @param name Player's name
	 * @param score Player's score
	 */
	public void addScore(String name, int score) {
		if (!isTopScore(score)) return;
		userScore newScore = new userScore(name, score);
		scoreList.add(newScore);
		Collections.sort(scoreList);
		if (scoreList.size()>MAX_LIST_SIZE) scoreList.remove(MAX_LIST_SIZE);
			
	}
	
	/**
	 * Writes scores to binary file.
	 */
	public void saveScores() {
		try {
			//Try to save scoresList to file
			FileOutputStream fos = new FileOutputStream(FILENAME);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(scoreList);
			oos.close();
		} catch (Exception e) {
			// Can't save scores, show error in console
			System.err.println("Can't write to " + FILENAME + " file!");
			e.printStackTrace();
		}		
	}
	
	/**
	 * Clears all scores.
	 */
	public void clearScores() {
		scoreList.clear();
	}
	
	/**
	 * Size of score list
	 * @return score list size
	 */
	public int size() {
		return scoreList.size();
	}
	
	/**
	 * Makes a string of the top scores
	 * @return String of top scores
	 */
	public String toString() {
		if (scoreList.size()==0) return "(none)";
		
		String returnString = new String();
		for (int i=0; i<MAX_LIST_SIZE; i++) {
			if (!(i<scoreList.size())) break;	//safety check in case there are not enough scores
			returnString = returnString.concat((i+1) + ". " + scoreList.get(i) + "\n"); 
		}
		return returnString;
	}

	/**
	 * Determines if a score should be in top scores
	 * @param score Score to compare
	 * @return true if score should be in top scores
	 */
	public boolean isTopScore(int score) {
		if (scoreList.size() < MAX_LIST_SIZE) return true;
		int bottomScore = scoreList.get(MAX_LIST_SIZE-1).getScore();
		if (bottomScore > score)
			return true;
		else
			return false;
		
	}
	
	/**
	 * This class is used as the type of object to store in scoreList
	 * It contains the name and score information of a player and provides
	 * a way to compare players.
	 */
	private class userScore implements Serializable, Comparable<userScore> {
		private static final long serialVersionUID = 1L;
		private String name;
		private int score;		
		
		/**
		 * Constructor builds immutable instance of userScore
		 * @param name Player's name
		 * @param score Player's score
		 */
		public userScore(String name, int score) {
			super();
			this.name = name;
			this.score = score;
		}

		/**
		 * Gets the score
		 * @return the score
		 */
		public int getScore() {
			return score;
		}
		
		/**
		 * String contains the name and score 
		 */
		public String toString() {
			return new String(this.name + " - " + this.score);
		}
		
		/**
		 * Compares this score to other score
		 */
		@Override
		public int compareTo(userScore o) {
			return this.score - o.score;
		}
	}
}
