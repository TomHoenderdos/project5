package nl.mprog.evilhangman;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

	private WordList words;
	private String current_word = new String();
	private int current_attempts;
	private int max_attempts;
	private int word_length;
	private ArrayList<Character> guessed_characters = new ArrayList<Character>();
	private String match_holder = new String();
	private boolean game_started = false;
	private DatabaseHandler dbh = null;
	private WordHelper word;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);      
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.activity_main);

        this.word = new WordHelper(this);
        
        KeyboardView keyboardView = (KeyboardView) findViewById(R.id.keyboardView);
        Keyboard keyboard = new Keyboard(this, R.layout.custom_keyboard);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(true);      
        keyboardView.setOnKeyboardActionListener(this);

//        DatabaseHandler db = new DatabaseHandler(this);
//        String[] s = db.getWords();
        
        this.words = new WordList(this);    
        this.newGame();
               
        
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

    
    private void newGame() {
    	if(this.game_started) {
        	AlertDialog.Builder alert = new AlertDialog.Builder(this);

        	alert.setTitle("New Game");
        	alert.setMessage("Are you sure you want to start a new game?");
	    	alert.setCancelable(false);

	    	alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	    		@Override
				public void onClick(DialogInterface dialog, int whichButton) {
	    			game_started = false;
	      			newGame();
	    		}
	    	});
	    	alert.setNegativeButton("No", null);
        	alert.show();
    	} else {
	    	this.current_attempts = 0;
	    	
	    	SettingsModel set = new SettingsModel();
	    	
	    	set.setEvil(true);
	    	set.setWordCount(20);
	    	set.setMaxAttempts(6);
	    	
	    	SettingsHelper sh = new SettingsHelper(this);
//	    	SettingsModel shm = new SettingsModel();
	    	
	    	
	    	this.max_attempts = sh.getSettings().getMaxAttempts();
	        this.current_word = words.getRandomWord().toLowerCase(Locale.getDefault());
	        this.guessed_characters.clear();
	        this.game_started = true;
	
	    	this.updateWord();
	    	this.updateAttempts();
	    	this.resetKeyboard();
	    	
	    	Toast.makeText(this, "New Word:"+this.current_word, Toast.LENGTH_LONG).show();		
    	}
    }
    
    private void updateWord() {
        this.match_holder = "";
        
    	TextView word_view = (TextView) this.findViewById(R.id.word_view);
    	StringBuilder placeholder = new StringBuilder(" ");
    	for(int i = 0; i < current_word.length(); i++) {
    		char c = this.current_word.toCharArray()[i];
    		if(guessed_characters.contains(c)) {
	    		placeholder.append(c).append(" ");
	    		this.match_holder += c;
    		} else {
	    		placeholder.append("_ ");
	    		this.match_holder += "_";
    		}
    	}
    	word_view.setText(placeholder);
    }
    
    private void updateAttempts() {
    	TextView attempts_view = (TextView) this.findViewById(R.id.attempts_view);
    	attempts_view.setText("Attempts "+this.current_attempts+"/"+this.max_attempts);    	  
    	
    	if(this.current_attempts == this.max_attempts) {
    		this.loseGame();
    		this.newGame();
    	}
    }

    private void processKey(int keyCode) {   
    	if(this.current_attempts < this.max_attempts) {
	    	int begin = 'a';
	    	int min_value = KeyEvent.KEYCODE_A;
	    	char key_press = (char)(begin + (keyCode-min_value));
	    	
	    	if(keyCode >= 29 && keyCode <= 54) {
		    	if(guessed_characters.contains(key_press)) {
		        	//Toast.makeText(this, "You already guessed " + key_press + ".", Toast.LENGTH_LONG).show();
		    	} else {
		        	if(this.current_word.contains(String.valueOf(key_press))) {      		
		        		this.correctAnswer(key_press);    
		        	} else {
			        	guessed_characters.add(key_press);
		        		this.setKeyboardKeyLabel(key_press, "–");
		        		this.wrongAnswer();        		
		        	}
		    	}
	    	}
    	}
    }    
    public void correctAnswer(char key_press) {  
    	String old_word = this.current_word;
    	guessed_characters.add(key_press);	  	
    	this.current_word = this.words.getCurrent_word_list_model().getEvilWord(this.current_word, this.match_holder, this.guessed_characters, key_press).toLowerCase();
    	if(this.current_word.equals(old_word)) {
    		this.setKeyboardKeyLabel(key_press, "✔");	
    		
        	this.updateWord(); 
        	if(!this.match_holder.contains("_")) {
        		this.winGame();
        	}
    	}
    	else {    	
        	Toast.makeText(this, "New word:" + this.current_word, Toast.LENGTH_LONG).show();
    		this.setKeyboardKeyLabel(key_press, "–");
    		wrongAnswer();  	
        	this.updateWord(); 	
    	}
    }
    
    public void wrongAnswer() {
    	this.current_attempts++;
    	this.updateAttempts();
    }
    
    public void winGame() {
    	//Toast.makeText(this, "You won the game!", Toast.LENGTH_LONG).show();
        this.game_started = false;
    	endGamePopup("You won!", true);
    }
    
    public void loseGame() {
    	//Toast.makeText(this, "You lost the game!", Toast.LENGTH_LONG).show();
        this.game_started = false;
    	endGamePopup("You suck!", false);
    }
    
    public void endGamePopup(String title, boolean show_highscore) {
    	AlertDialog.Builder alert = new AlertDialog.Builder(this);

    	alert.setTitle(title);

    	alert.setCancelable(false);
    	if(show_highscore) {
    		alert.setMessage("Fill in your name for your highscore:");

	    	final EditText input = new EditText(this);
	    	alert.setView(input);
	    	
	    	alert.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
	    		@Override
				public void onClick(DialogInterface dialog, int whichButton) {
	    			String value = input.getText().toString();
	    			System.out.println(value);
	      			newGame();
	    		}
	    	});
    	}
    	else {
    	  	alert.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
	    		@Override
				public void onClick(DialogInterface dialog, int whichButton) {
	      			newGame();
	    		}
	    	});
    	}
    	
    	alert.show();
    }
    
    private void setKeyboardKeyLabel(int key_press, String label) {
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

    /*
     * returns: void
     * function: reset the keyboard
     */
    private void resetKeyboard() {
        KeyboardView keyboardView = (KeyboardView) findViewById(R.id.keyboardView);
        Keyboard keyboard = new Keyboard(this, R.layout.custom_keyboard);
        keyboardView.setKeyboard(keyboard);    	
    }
    
    @Override
    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
//    	getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

   
    /*
     * (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
	public boolean onOptionsItemSelected(MenuItem item){
    	switch (item.getItemId()){
    	case R.id.action_refresh:
    		newGame();
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
    	
    	newGame();
    }
    
    public void cancelSettings(){
    	setContentView(R.layout.activity_main);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	processKey(keyCode);    	
    	return super.onKeyDown(keyCode, event);
    }

    @Override
	public void onKey(int primaryCode, int[] keyCodes) {
    	processKey(primaryCode);
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
