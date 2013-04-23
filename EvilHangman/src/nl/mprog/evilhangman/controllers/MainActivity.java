package nl.mprog.evilhangman.controllers;

import nl.mprog.evilhangman.R;
import nl.mprog.evilhangman.helpers.HighscoresHelper;
import nl.mprog.evilhangman.helpers.SettingsHelper;
import nl.mprog.evilhangman.helpers.WordHelper;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

	// The game this is currently playing
	private Game mainGame;	
	private GestureDetector gestureDetector;

	/**
	 * onCreate is called when the activity launches.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);      

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_main);

		// create the keyboard view
		GameKeyboardView keyboardView = (GameKeyboardView) this.findViewById(R.id.keyboardView);
		Keyboard keyboard = new Keyboard(this, R.layout.custom_keyboard);
		keyboardView.setKeyboard(keyboard);
		keyboardView.setEnabled(true);
		keyboardView.setPreviewEnabled(true);  

		// initialize wordhelper
		WordHelper.instance.initialize(this); 

		// initialize SettingsHelper
		SettingsHelper.instance.initialize(this);

		// initialize HighscoreHelper
//		HighscoresHelper.instance.initialize(this);

		// create the a game
		onGameFinished();

		// ensure this line is below creating a game
		keyboardView.setOnKeyboardActionListener(new GameKeyboardListener(this.mainGame));

		// create a gesture detector for swiping down and up
		this.gestureDetector = new GestureDetector(this, new GameGestureDetectorListener(this));
	}

	/**
	 * Callback called by a game
	 */
	public void onGameFinished() {
		if (SettingsHelper.instance.getSettings().getEvil()){
			Log.w("Game Mode", "Evil");
			mainGame = new EvilGame(this);
		} else {
			Log.w("Game Mode", "Normal");
			mainGame = new NormalGame(this);
		}	
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		this.mainGame.processKey(keyCode);    	
		return super.onKeyDown(keyCode, event);
	}

	public boolean onTouchEvent(MotionEvent me) {
		this.gestureDetector.onTouchEvent(me);
		return super.onTouchEvent(me);
	}


	/**
	 * The context menu is created here.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.game_menu, menu);
		//    	getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	/**
	 * Called when one of the buttons is pressed in the context menu.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){
		case R.id.action_refresh:
			this.mainGame.newGame();
			return true;
		case R.id.action_settings:
			showSettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	/*
	 * Shows the settings for the game
	 */
	public void showSettings(){
		this.startActivity(new Intent(getBaseContext(), Preferences.class));
	}


	// getters

	public Game getMainGame() {
		return mainGame;
	}

	public GestureDetector getGestureDetector() {
		return gestureDetector;
	}

}
