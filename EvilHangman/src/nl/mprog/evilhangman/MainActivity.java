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
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("DefaultLocale")
public class MainActivity extends Activity implements OnKeyboardActionListener{

	private WordList words;
	private String current_word = new String();
	private int current_attempts;
	private int max_attempts = 10;
	private int min_word_length;
	private int max_word_length;
	private ArrayList<Character> guessed_characters = new ArrayList<Character>();
	private String match_holder = new String();
	
	
	
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
        
        this.words = new WordList(this);    
        this.NewGame();
        
       /* for(int i = 0; i < words.size(); i++) {
        	System.out.println(words.get(words.keyAt(i)).toString());
        }*/
    }

    private void NewGame() {
    	this.current_attempts = 0;
    	this.max_attempts = 10;
        this.current_word = words.getRandomWord().toLowerCase(Locale.getDefault());
        this.guessed_characters.clear();

    	this.UpdateWord();
    	this.UpdateAttempts();
    	this.ResetKeyboard();
    	
    	Toast.makeText(this, "New Word:"+this.current_word, Toast.LENGTH_LONG).show();
    }
    
    private void UpdateWord() {
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
    
    private void UpdateAttempts() {
    	TextView attempts_view = (TextView) this.findViewById(R.id.attempts_view);
    	attempts_view.setText("Attempts "+this.current_attempts+"/10");    	  
    	
    	if(this.current_attempts == this.max_attempts) {
    		this.LoseGame();
    		this.NewGame();
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
		        		this.CorrectAnswer(key_press);    
		        	} else {
			        	guessed_characters.add(key_press);
		        		this.SetKeyboardKeyLabel(key_press, "–");
		        		this.WrongAnswer();        		
		        	}
		    	}
	    	}
    	}
    }    
    public void CorrectAnswer(char key_press) {  
    	String old_word = this.current_word;
    	guessed_characters.add(key_press);	  	
    	this.current_word = this.words.getCurrent_word_list_model().GetEvilWord(this.current_word, this.match_holder, this.guessed_characters, key_press).toLowerCase();
    	if(this.current_word.equals(old_word)) {
    		this.SetKeyboardKeyLabel(key_press, "✔");	
    		
        	this.UpdateWord(); 
        	if(!this.match_holder.contains("_")) {
        		this.WinGame();
        	}
    	}
    	else {    	
        	Toast.makeText(this, "New word:" + this.current_word, Toast.LENGTH_LONG).show();
    		this.SetKeyboardKeyLabel(key_press, "–");
    		WrongAnswer();  	
        	this.UpdateWord(); 	
    	}
    }
    
    public void WrongAnswer() {
    	this.current_attempts++;
    	this.UpdateAttempts();
    }
    
    public void WinGame() {
    	Toast.makeText(this, "You won the game!", Toast.LENGTH_LONG).show();
    }
    
    public void LoseGame() {
    	Toast.makeText(this, "You lost the game!", Toast.LENGTH_LONG).show();
    }
    
    private void SetKeyboardKeyLabel(int key_press, String label) {
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

    private void ResetKeyboard() {
        KeyboardView keyboardView = (KeyboardView) findViewById(R.id.keyboardView);
        Keyboard keyboard = new Keyboard(this, R.layout.custom_keyboard);
        keyboardView.setKeyboard(keyboard);    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	processKey(keyCode);    	
    	return super.onKeyDown(keyCode, event);
    }

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
