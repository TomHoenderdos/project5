package nl.mprog.evilhangman.controllers;


import java.util.ArrayList;
import java.util.Locale;

import nl.mprog.evilhangman.R;
import nl.mprog.evilhangman.R.id;
import nl.mprog.evilhangman.R.string;
import nl.mprog.evilhangman.helpers.GameHelper;
import nl.mprog.evilhangman.helpers.WordHelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public abstract class Game {
	private String currentWord = new String();
	private String wordPattern = new String();
	private ArrayList<Character> badCharacters = new ArrayList<Character>();
	private ArrayList<Character> goodCharacters = new ArrayList<Character>();
	
	private int currentAttempts;

	enum GameState {
		NOT_STARTED,
		STARTED
	};	
	
	private GameState gamestate = GameState.NOT_STARTED;	
	
	private MainActivity ctx;
	
	/**
	 * Constructor of Game
	 * @param ctx
	 */
	public Game(MainActivity ctx) {
		this.ctx = ctx;
		this.newGame();
	}

	// Game Mechanics
	
	/**
	 * Starts a new game. If a game is already running the user will be asked to confirm.
	 */
	public void newGame() {
		
    	if(this.gamestate == GameState.STARTED) {
    		
    		// already started? notify user
        	AlertDialog.Builder alert = new AlertDialog.Builder(this.ctx);

        	alert.setTitle(this.ctx.getString(R.string.new_game_title));
        	alert.setMessage(this.ctx.getString(R.string.new_game_message));
	    	alert.setCancelable(false);

	    	alert.setPositiveButton(this.ctx.getString(R.string.positive_button_text), new DialogInterface.OnClickListener() {
	    		public void onClick(DialogInterface dialog, int whichButton) {
	    			Game.this.gamestate = Game.GameState.NOT_STARTED;
	      			Game.this.ctx.onGameFinished();
	    		}
	    	});
	    	
	    	alert.setNegativeButton(this.ctx.getString(R.string.negative_button_text), null);
        	alert.show();
        	
    	} else if(this.gamestate == GameState.NOT_STARTED) {
    		
    		// reset variable
	    	this.currentAttempts = 0; // reset attempts
	        this.newWord(); // choose new word
	        this.badCharacters.clear(); // clear bad characters
	        this.goodCharacters.clear(); // clear good characters
	        this.gamestate = GameState.STARTED; // set state to started
	
	        // update ui
	    	this.updateWordPattern();
	    	this.updateAttempts();
	    	ctx.resetKeyboard();
	    	
	    	Toast.makeText(this.ctx, "New Word:"+this.currentWord, Toast.LENGTH_LONG).show();	
    	}
	}
	
	/**
	 * Selects a completely random word
	 */
	public void newWord() {
		this.currentWord = WordHelper.instance.getRandomWord().toLowerCase(Locale.getDefault());
	}

	/**
	 * This method is called when a user presses a correct character. If evil is turned on it will make sure a
	 * new word is selected. It will also take care of losing and winning a game.
	 * @param key_press The key that was pressed.
	 */
	public void onCorrectAnswer(char key_press) {  
    	this.goodCharacters.add(key_press);	
		ctx.setKeyboardKeyLabel(key_press, this.ctx.getString(R.string.keyboard_button_correct));	
		
    	this.updateWordPattern(); 
    	if(!this.wordPattern.contains("_")) {
    		this.onWinGame();
    	}
    }
    
    /**
     * This method is called whenever a user guessed a character that is not in the current word. 
     * It updates the attempts accordingly.
     */
    public void onWrongAnswer(char key_press) {
    	this.badCharacters.add(key_press);	
		ctx.setKeyboardKeyLabel(key_press, this.ctx.getString(R.string.keyboard_button_incorrect));	
		
    	this.currentAttempts++;
    	this.updateAttempts();
    }
    
    /**
     * This is called when a user has won a game. 
     */
    public void onWinGame() {
    	//Toast.makeText(this, "You won the game!", Toast.LENGTH_LONG).show();
        this.gamestate = GameState.NOT_STARTED;
    	this.endGamePopup(this.ctx.getString(R.string.win_game_message), true);
    }

    /**
     * This is called when a user has lost a game. 
     */
    public void onLoseGame() {
    	//Toast.makeText(this, "You lost the game!", Toast.LENGTH_LONG).show();
        this.gamestate = GameState.NOT_STARTED;
    	this.endGamePopup(this.ctx.getString(R.string.lose_game_message), false);
    }	
    
	// UI Updates	
	
	/**
	 * Updates the word pattern that is shown on screen. The pattern is derived from
	 * the current word in conjunction with all used characters.
	 */
    public void updateWordPattern() {
        this.wordPattern = "";
        
    	TextView word_view = (TextView) this.ctx.findViewById(R.id.word_view);
    	StringBuilder placeholder = new StringBuilder(" ");
    	for(int i = 0; i < this.currentWord.length(); i++) {
    		char c = this.currentWord.toCharArray()[i];
    		if(this.goodCharacters.contains(c)) {
	    		placeholder.append(c).append(" ");
	    		this.wordPattern += c;
    		} else {
	    		placeholder.append("_ ");
	    		this.wordPattern += "_";
    		}
    	}
    	word_view.setText(placeholder);
    }
    
    
    /**
     * Updates the attempts that is shown on screen.
     */
    public void updateAttempts() {
    	TextView attempts_view = (TextView) this.ctx.findViewById(R.id.attempts_view);
    	attempts_view.setText("Attempts "+this.currentAttempts+"/"+GameHelper.instance.getMaxAttempts());    	  
    	
    	if(this.currentAttempts == GameHelper.instance.getMaxAttempts()) {
    		this.onLoseGame();
    	}
    }
    
    public void endGamePopup(String title, boolean show_highscore) {
    	AlertDialog.Builder alert = new AlertDialog.Builder(this.ctx);

    	alert.setTitle(title);

    	alert.setCancelable(false);
    	if(show_highscore) {
    		alert.setMessage(this.ctx.getString(R.string.highscore_message));

	    	final EditText input = new EditText(this.ctx);
	    	alert.setView(input);
	    	
	    	alert.setPositiveButton(this.ctx.getString(R.string.new_game_title), new DialogInterface.OnClickListener() {
	    		public void onClick(DialogInterface dialog, int whichButton) {
	    			String value = input.getText().toString();
	    			System.out.println(value);
	      			Game.this.ctx.onGameFinished();
	    		}
	    	});
    	}
    	else {
    	  	alert.setPositiveButton(this.ctx.getString(R.string.new_game_title), new DialogInterface.OnClickListener() {
	    		public void onClick(DialogInterface dialog, int whichButton) {
	      			Game.this.ctx.onGameFinished();
	    		}
	    	});
    	}
    	
    	alert.show();
    }
    
    // Events    

    /**
     * This method is called from the main activity when any valid key is pressed.
     * @param keyCode
     */
    public void processKey(int keyCode) {   
    	if(this.currentAttempts < GameHelper.instance.getMaxAttempts()) {
	    	int begin = 'a';
	    	int min_value = KeyEvent.KEYCODE_A;
	    	char key_press = (char)(begin + (keyCode-min_value));
	    	
	    	if(keyCode >= 29 && keyCode <= 54) {
		    	if(this.badCharacters.contains(key_press) || this.goodCharacters.contains(key_press)) {
		        	//Toast.makeText(this, "You already guessed " + key_press + ".", Toast.LENGTH_LONG).show();
		    	} else {
		        	if(this.currentWord.contains(String.valueOf(key_press))) {      		
		        		this.onCorrectAnswer(key_press);    
		        	} else {
		        		this.onWrongAnswer(key_press);        		
		        	}
		    	}
	    	}
    	}
    }
    
    // getters setters
    
	public String getCurrentWord() {
		return currentWord;
	}

	public void setCurrentWord(String currentWord) {
		this.currentWord = currentWord;
	}

	public ArrayList<Character> getBadCharacters() {
		return badCharacters;
	}

	public void setBadCharacters(ArrayList<Character> badCharacters) {
		this.badCharacters = badCharacters;
	}	
	
	public ArrayList<Character> getGoodCharacters() {
		return goodCharacters;
	}

	public void setGoodCharacters(ArrayList<Character> goodCharacters) {
		this.goodCharacters = goodCharacters;
	}

	public String getWordPattern() {
		return wordPattern;
	}

	public void setWordPattern(String wordPattern) {
		this.wordPattern = wordPattern;
	}

	public int getCurrentAttempts() {
		return currentAttempts;
	}

	public void setCurrentAttempts(int currentAttempts) {
		this.currentAttempts = currentAttempts;
	}

	public GameState getGamestate() {
		return gamestate;
	}

	public void setGamestate(GameState gamestate) {
		this.gamestate = gamestate;
	}

	public MainActivity getCtx() {
		return ctx;
	}

	public void setCtx(MainActivity ctx) {
		this.ctx = ctx;
	}    
}
