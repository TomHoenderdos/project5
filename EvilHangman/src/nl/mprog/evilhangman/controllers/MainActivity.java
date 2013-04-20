package nl.mprog.evilhangman.controllers;


import java.util.List;

import nl.mprog.evilhangman.R;
import nl.mprog.evilhangman.R.id;
import nl.mprog.evilhangman.R.layout;
import nl.mprog.evilhangman.R.menu;
import nl.mprog.evilhangman.helpers.SettingsHelper;
import nl.mprog.evilhangman.helpers.WordHelper;
import nl.mprog.evilhangman.models.SettingsModel;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "DefaultLocale", "NewApi", "ShowToast" })
public class MainActivity extends Activity implements OnKeyboardActionListener{

	// The game this is currently playing
	private Game mainGame;	
	
	/**
	 * onCreate is called when the activity launches.
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);      
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.activity_main);
        
        KeyboardView keyboardView = (KeyboardView) findViewById(R.id.keyboardView);
        Keyboard keyboard = new Keyboard(this, R.layout.custom_keyboard);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(true);      
        keyboardView.setOnKeyboardActionListener(this);

        WordHelper.instance.initialize(this);
        
        mainGame = new EvilGame(this);
        
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
    
    /**
     * Changes a visual keyboard key label with a string.
     * @param key_press
     * @param label
     */
    public void setKeyboardKeyLabel(final int key_press, final String label) {
    	this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
		        KeyboardView keyboardView = (KeyboardView) findViewById(R.id.keyboardView);
		        Keyboard keyboard = keyboardView.getKeyboard();
		        List<Key> keys = keyboard.getKeys();
		        for(Key key : keys) {
		        	if(key.label.toString().toLowerCase().charAt(0) == key_press) {
		        		key.label = label;
		        		keyboardView.invalidateKey(keys.indexOf(key));
		        	}
		        }
			}    		
    	});
    }

    /**
     * Returns the visual keyboard to default
     */
    public void resetKeyboard() {
    	this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
		        KeyboardView keyboardView = (KeyboardView) findViewById(R.id.keyboardView);
		        Keyboard keyboard = new Keyboard(MainActivity.this, R.layout.custom_keyboard);
		        keyboardView.setKeyboard(keyboard);    	
			}    		
    	});
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
    	Intent intent = new Intent(getBaseContext(), Preferences.class);
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
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	this.mainGame.processKey(keyCode);    	
    	return super.onKeyDown(keyCode, event);
    }
    public void onKey(int primaryCode, int[] keyCodes) {
    	this.mainGame.processKey(primaryCode);
    }
    
	@Override
	public void onPress(int arg0) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onRelease(int arg0) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onText(CharSequence arg0) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void swipeDown() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void swipeLeft() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void swipeRight() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void swipeUp() {
		// TODO Auto-generated method stub		
	}
}
