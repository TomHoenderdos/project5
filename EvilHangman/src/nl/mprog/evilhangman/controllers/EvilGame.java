package nl.mprog.evilhangman.controllers;

import java.util.ArrayList;

import nl.mprog.evilhangman.helpers.WordHelper;

/**
 * EvilGame is a class that extends the abstract class Game. When this type of game is run
 * the program will try to fool an user in thinking they guessed the wrong answer as much as possible.
 * @author Jeroen Grootendorst
 *
 */
public class EvilGame extends Game {

	// When the game cannot be evil anymore this will stop attempting to be evil (to speed up guessing)
	private boolean cannotBeEvilAnymore = false;

	/**
	 * Class constructor providing a context
	 * @param ctx
	 */
	public EvilGame(MainActivity ctx) {
		super(ctx);
	}

	/**
	 * An extra variable will be reset in evil mode.
	 */
	@Override
	public void newGame() {
		super.newGame();
		this.cannotBeEvilAnymore = false;
	}

	/**
	 * Evil version of onCorrectAnswer
	 * @param key_press The key that was pressed.
	 */
	@Override
	public void onCorrectAnswer(char key_press) {  
		String old_word = this.getCurrentWord();	

		ArrayList<Character> tempList = this.getBadCharacters();
		tempList.addAll(this.getGoodCharacters());

		if(this.cannotBeEvilAnymore) {
			super.onCorrectAnswer(key_press);
		}
		else {
			this.setCurrentWord(WordHelper.instance.getEvilWord(this.getCurrentWord(), this.getWordPattern(), tempList, key_press));
			if(this.getCurrentWord().equals(old_word)) {
				super.onCorrectAnswer(key_press);
			}
			else {    	
				if(this.getCurrentWord().length() == 0) {  
					super.onCorrectAnswer(key_press);
					this.cannotBeEvilAnymore = true;
				}
				else {  
					super.onWrongAnswer(key_press);
					this.getCtx().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							//Toast.makeText(EvilGame.this.getCtx(), "New word:" + EvilGame.this.getCurrentWord(), Toast.LENGTH_LONG).show();	
						}    				
					});
				}
			}
		}
	}  
}
