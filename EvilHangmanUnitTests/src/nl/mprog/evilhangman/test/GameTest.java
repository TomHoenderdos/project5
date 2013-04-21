package nl.mprog.evilhangman.test;

import android.test.ActivityInstrumentationTestCase2;
import nl.mprog.evilhangman.controllers.Game;
import nl.mprog.evilhangman.controllers.MainActivity;

public class GameTest extends ActivityInstrumentationTestCase2<MainActivity> {
		
	Game mainGame;
	public GameTest() {
		super(MainActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
        mainGame = this.getActivity().getMainGame();
		super.setUp();
	}

	public void testAttemptReset() {
		int attempts = this.mainGame.getCurrentAttempts();
		assertEquals("A player should start with 0 attempts.", 0, attempts);
	}
	
	public void testWordNullable() {
		String word = this.mainGame.getCurrentWord();
		assertEquals("A random word was not selected!", false, word == null || word.length() == 0);
	}
	
	public void testWordPattern() {
		this.mainGame.setCurrentWord("MAGNETRON");
		this.mainGame.getGoodCharacters().add('A');
		this.mainGame.updateWordPattern();
		assertEquals("_A_______", this.mainGame.getWordPattern());
	}

}
