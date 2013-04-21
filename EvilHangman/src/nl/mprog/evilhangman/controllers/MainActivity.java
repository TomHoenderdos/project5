package nl.mprog.evilhangman.controllers;

import nl.mprog.evilhangman.R;
import nl.mprog.evilhangman.helpers.SettingsHelper;
import nl.mprog.evilhangman.helpers.WordHelper;
import nl.mprog.evilhangman.models.SettingsModel;

import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.Switch;

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
        
        // initialize wordhelpe
        WordHelper.instance.initialize(this);  
        
        // create the a game
        this.mainGame = new EvilGame(this);
        
        // ensure this line is below creating a game
        keyboardView.setOnKeyboardActionListener(new GameKeyboardListener(this.mainGame));

        // create a gesture detector for swiping down and up
        this.gestureDetector = new GestureDetector(this, new GameGestureDetectorListener(this));
        
//        DatabaseHandler db = new DatabaseHandler(this);
//        String[] s = db.getWords();
        
       // this.words = new WordList(this);    
        
     // create RangeSeekBar as Integer range between 20 and 75
//        Context context;
//		RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(20, 75, context);
//        seekBar.setOnRangeSeekBarChangeListener(
//        		new OnRangeSeekBarChangeListener<Integer>() {
//                @Override
//                public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
//                        // handle changed range values
//                        Log.i(TAG, "User selected new range values: MIN=" + minValue + ", MAX=" + maxValue);
//                }
//        });
//
//        // add RangeSeekBar to pre-defined layout
//        ViewGroup layout = (ViewGroup) findViewById(<your-layout-id>);
//        layout.addView(seekBar);
        
        
//       for(int i = 0; i < words.size(); i++) {
//        	System.out.println(words.get(words.keyAt(i)).toString());
//       }
    }

    /**
    * Callback called by a game
    */
    public void onGameFinished() {
        mainGame = new EvilGame(this);
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
//    	intent.
    }
    
    public void saveSettingsAndRestart(){
    	// save settings
    	
    	SettingsModel st = new SettingsModel();
    	
    	// Evil
    	Switch evil = (Switch) findViewById(R.id.switch1);
    	st.setEvil(evil.isChecked());
    	
    	// MaxAttempts
    	SeekBar maxAttempts = (SeekBar) findViewById(R.id.seekBar2);
    	st.setMaxAttempts(maxAttempts.getProgress());
    	
    	// WordCount    	
    	SeekBar wordCount = (SeekBar) findViewById(R.id.seekBar1);
    	st.setWordCount(wordCount.getProgress());
//    	Toast.makeText(this, "Settings => MaxAT:" + st.getMaxAttempts() + ", WordCount:" + st.getWordCount() + ", Evil:" + st.getEvil(), Toast.LENGTH_LONG);
    	SettingsHelper sh = new SettingsHelper(this);
    	sh.saveSettings(st);

		this.mainGame.newGame();
    }
    
    public void cancelSettings(){
    	setContentView(R.layout.activity_main);
    }

	public Game getMainGame() {
		return mainGame;
	}

	public GestureDetector getGestureDetector() {
		return gestureDetector;
	}
	
}
