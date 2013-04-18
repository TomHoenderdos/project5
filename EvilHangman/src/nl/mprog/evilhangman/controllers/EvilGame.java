package nl.mprog.evilhangman.controllers;

import java.util.ArrayList;

import nl.mprog.evilhangman.helpers.WordHelper;
import android.widget.Toast;

public class EvilGame extends Game {

	public EvilGame(MainActivity ctx) {
		super(ctx);
	}
	
	/**
	 * This method is called when a user presses a correct character. If evil is turned on it will make sure a
	 * new word is selected. It will also take care of losing and winning a game.
	 * @param key_press The key that was pressed.
	 */
	@Override
    public void onCorrectAnswer(char key_press) {  
    	String old_word = this.getCurrentWord();	
    	
    	ArrayList<Character> tempList = this.getBadCharacters();
    	tempList.addAll(this.getGoodCharacters());
    	
    	this.setCurrentWord(WordHelper.instance.getEvilWord(this.getCurrentWord(), this.getWordPattern(), tempList, key_press));
    	if(this.getCurrentWord().equals(old_word)) {
    	 	super.onCorrectAnswer(key_press);
    	}
    	else {    	
    		if(this.getCurrentWord().length() == 0) {  
        	 	super.onCorrectAnswer(key_press);
    		}
    		else {  
        	 	super.onWrongAnswer(key_press);
    			Toast.makeText(this.getCtx(), "New word:" + this.getCurrentWord(), Toast.LENGTH_LONG).show();	
    		}
        	this.updateWordPattern(); 	
    	}
    }  
}
