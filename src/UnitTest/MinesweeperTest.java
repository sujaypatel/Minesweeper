/**
 * 
 */
package UnitTest;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import project2.MinesweeperModel;
import project2.ScoreModel;
/**
 *
 */
public class MinesweeperTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	/**
	 * Test constructor and display contents in console
	 */
	public void testMinesweeperModelConstructor() {
		MinesweeperModel mTest = new MinesweeperModel();
		System.out.println(mTest);
		assertNotNull(mTest);
	}
	
	@Test
	public void testScoreModel() {
		ScoreModel sTest = new ScoreModel();
		sTest.clearScores();
		sTest.saveScores();
		
		assertNotNull(sTest);
		assertEquals(0, sTest.size());
		
		sTest.addScore("Alicia", 92);
		sTest.addScore("Jimmy", 21);
		sTest.addScore("Jane", 42);
		sTest.addScore("Billy", 72);
		assertEquals(4, sTest.size());
		
		sTest.addScore("Tom", 52);
		sTest.addScore("Julie", 20);
		assertEquals(6, sTest.size());
		
		sTest.saveScores();
		System.out.println(sTest +"\n");
		
		sTest = new ScoreModel();
		assertEquals(6, sTest.size());
		assertTrue(sTest.isTopScore(50000));
		
		sTest.addScore("Sam", 53);
		sTest.addScore("Pam", 52);
		sTest.addScore("Caleb", 522);
		sTest.addScore("Kayla", 18);
		System.out.println(sTest +"\n");
		assertFalse(sTest.isTopScore(5000));
		assertTrue(sTest.isTopScore(19));
		sTest.addScore("Stephen", 19);
		assertEquals(10, sTest.size());
		sTest.addScore("Fail", 50000);
		assertEquals(10, sTest.size());
		
		System.out.println(sTest);
	}

}
