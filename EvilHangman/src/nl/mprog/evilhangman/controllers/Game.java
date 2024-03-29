package nl.mprog.evilhangman.controllers;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import nl.mprog.evilhangman.R;
import nl.mprog.evilhangman.helpers.SettingsHelper;
import nl.mprog.evilhangman.helpers.WordHelper;
import nl.mprog.evilhangman.models.Highscore;
import nl.mprog.evilhangman.models.SettingsModel;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
/**
 * Game is an abstract class designed for the base features of a Hang Man game. It is possible to run 
 * multiple games at once giving the fact you link it to other context. 
 * @author Jeroen Grootendorst
 *
 */
public abstract class Game {
	private String currentWord = new String();
	private String wordPattern = new String();
	private List<Character> badCharacters = new ArrayList<Character>();
	private List<Character> goodCharacters = new ArrayList<Character>();    
	// for processing guesses in correct order
	private final ExecutorService executor = Executors.newSingleThreadExecutor();
	private int currentAttempts;
	private int maxAttempts;
	private int wordCount;
	enum GameState {
		NOT_STARTED,
		STARTED
	};	
	private GameState gamestate = GameState.NOT_STARTED;	
	private final MainActivity ctx;	
	/**
	 * Class constructor providing a context
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
	protected void newGame() {
		//Get settings and save them in SQLite DB
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx.getBaseContext());
		SettingsModel settingsModel = SettingsHelper.instance.getSettings();
		settingsModel.setEvil(prefs.getBoolean("Evil", false));
		settingsModel.setMaxAttempts(prefs.getInt("sbp_MaxAttempts", 8));
		settingsModel.setWordCount(prefs.getInt("sbp_WordCount", 4));
		SettingsHelper.instance.saveSettings(settingsModel);
		SettingsModel settings = SettingsHelper.instance.getSettings();
		// Saved in SQLite   
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
			this.wordCount = settings.getWordCount();
			this.maxAttempts = settings.getMaxAttempts();
			this.currentAttempts = 0; // reset attempts
			this.newWord(); // choose new word
			this.badCharacters.clear(); // clear bad characters
			this.goodCharacters.clear(); // clear good characters
			this.gamestate = GameState.STARTED; // set state to started
			// update ui
			this.updateWordPattern();
			this.updateAttempts();
			((GameKeyboardView)this.ctx.findViewById(R.id.keyboardView)).resetKeyboard();
			//Toast.makeText(this.ctx, "New Word:"+this.currentWord, Toast.LENGTH_LONG).show();	
		}
	}
	/**
	 * Selects a completely random word
	 */
	protected void newWord() {
		this.currentWord = WordHelper.instance.getRandomWord().toLowerCase(Locale.getDefault());
	}
	/**
	 * This method is called when a user presses a correct character. If evil is turned on it will make sure a
	 * new word is selected. It will also take care of losing and winning a game.
	 * @param key_press The key that was pressed.
	 */
	protected void onCorrectAnswer(final char key_press) {    
		this.goodCharacters.add(key_press);	
		((GameKeyboardView)this.ctx.findViewById(R.id.keyboardView)).setKeyboardKeyLabel(key_press, Game.this.ctx.getString(R.string.keyboard_button_correct));	
		this.updateWordPattern(); 
		if(!this.wordPattern.contains("_")) {
			this.onWinGame();
		}
	}
	/**
	 * This method is called whenever a user guessed a character that is not in the current word. 
	 * It updates the attempts accordingly.
	 */
	protected void onWrongAnswer(final char key_press) {   
		this.badCharacters.add(key_press);	
		((GameKeyboardView)this.ctx.findViewById(R.id.keyboardView)).setKeyboardKeyLabel(key_press, Game.this.ctx.getString(R.string.keyboard_button_incorrect));	
		this.currentAttempts++;
		this.updateAttempts();
	}
	/**
	 * This is called when a user has won a game. 
	 */
	protected void onWinGame() {
		//Toast.makeText(this, "You won the game!", Toast.LENGTH_LONG).show();
		this.gamestate = GameState.NOT_STARTED;
		this.endGamePopup(this.ctx.getString(R.string.win_game_message), true);
	}
	/**
	 * This is called when a user has lost a game. 
	 */
	protected void onLoseGame() {
		//Toast.makeText(this, "You lost the game!", Toast.LENGTH_LONG).show();
		this.gamestate = GameState.NOT_STARTED;
		this.endGamePopup(this.ctx.getString(R.string.lose_game_message), false);
	}	
	// UI Updates	
	/**
	 * Updates the word pattern that is shown on screen. The pattern is derived from
	 * the current word in conjunction with all used characters.
	 */
	protected void updateWordPattern() {
		this.wordPattern = "";
		final TextView word_view = (TextView) this.ctx.findViewById(R.id.word_view);
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
		final StringBuilder tempPattern = placeholder;
		this.ctx.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				word_view.setText(tempPattern);
			}
		});
	}
	/**
	 * Updates the attempts that is shown on screen.
	 */
	protected void updateAttempts() {
		this.ctx.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				TextView attempts_view = (TextView) Game.this.ctx.findViewById(R.id.attempts_view);
				attempts_view.setText("Attempts "+Game.this.currentAttempts+"/"+Game.this.maxAttempts);    
			}
		});
		if(this.currentAttempts >= this.maxAttempts) {
			this.onLoseGame();
		}  
	}
	/**
	 * Shows a popup to notify a user of a win or loss.
	 * @param title title for alert
	 * @param show_highscore show highscore or not?
	 */
	protected void endGamePopup(final String title, final boolean show_highscore) {
		this.ctx.runOnUiThread(new Runnable() {
			@Override
			public void run() {

				AlertDialog.Builder alert = new AlertDialog.Builder(Game.this.ctx);
				alert.setTitle(title);
				alert.setCancelable(false);
				if(show_highscore) {
					alert.setMessage(Game.this.ctx.getString(R.string.highscore_message));
					final EditText input = new EditText(Game.this.ctx);
					alert.setView(input);
					alert.setPositiveButton(Game.this.ctx.getString(R.string.new_game_title), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							String value = input.getText().toString();
							int score = 0;
							if (SettingsHelper.instance.getSettings().getEvil()){
								score = ((Game.this.wordCount * 1000) / Game.this.currentAttempts ) * 2;
							} else {
								score = (Game.this.wordCount * 1000) / Game.this.currentAttempts;
							}
							Highscore highScore = new Highscore(Game.this.ctx);
							highScore.setName(value);
							highScore.setScore(score);
							highScore.setWord(Game.this.currentWord);
							highScore.save();

							Game.this.ctx.onGameFinished();
						}
					});
				}
				else {
					alert.setPositiveButton(Game.this.ctx.getString(R.string.new_game_title), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							Game.this.ctx.onGameFinished();
						}
					});
				}
				alert.show();
			}
		});
	}
	/**
	 * This method is called from the main activity when any valid key is pressed.
	 * @param keyCode
	 */
	public void processKey(int keyCode) { 
		if(this.currentAttempts < this.maxAttempts) {
			int begin = 'a';
			int min_value = KeyEvent.KEYCODE_A;
			final char key_press = (char)(begin + (keyCode-min_value));

			if(keyCode >= 29 && keyCode <= 54) {
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						if(Game.this.gamestate == GameState.NOT_STARTED) return; // game ended already, do nothing
						if(Game.this.badCharacters.contains(key_press) || Game.this.goodCharacters.contains(key_press)) {
							//Toast.makeText(this, "You already guessed " + key_press + ".", Toast.LENGTH_LONG).show();
						} else {
							if(Game.this.currentWord.contains(String.valueOf(key_press))) {      		
								Game.this.onCorrectAnswer(key_press);    
							} else {
								Game.this.onWrongAnswer(key_press);        		
							}
						}
					}
				};
				executor.execute(runnable);
			}
		}
	}
	// getters setters
	protected String getCurrentWord() {
		return currentWord;
	}
	protected void setCurrentWord(String currentWord) {
		this.currentWord = currentWord;
	}
	protected List<Character> getBadCharacters() {
		return badCharacters;
	}
	protected void setBadCharacters(List<Character> badCharacters) {
		this.badCharacters = badCharacters;
	}	
	protected List<Character> getGoodCharacters() {
		return goodCharacters;
	}
	protected void setGoodCharacters(List<Character> goodCharacters) {
		this.goodCharacters = goodCharacters;
	}
	protected String getWordPattern() {
		return wordPattern;
	}
	protected void setWordPattern(String wordPattern) {
		this.wordPattern = wordPattern;
	}
	protected int getCurrentAttempts() {
		return currentAttempts;
	}
	protected void setCurrentAttempts(int currentAttempts) {
		this.currentAttempts = currentAttempts;
	}
	protected GameState getGamestate() {
		return gamestate;
	}
	protected void setGamestate(GameState gamestate) {
		this.gamestate = gamestate;
	}
	protected MainActivity getCtx() {
		return ctx;
	}  
}
