package nl.mprog.evilhangman;

import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.activity_main);

        WordList.instance.parseWords(this);
        
        
        for(int i = 0; i < WordList.instance.getWords().size(); i++) {
        	System.out.println(WordList.instance.getWords().get(WordList.instance.getWords().keyAt(i)).toString());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// TODO Auto-generated method stub
    	int begin = 'A';
    	int min_value = KeyEvent.KEYCODE_A;
    	int key_press = begin + (keyCode-min_value);
    	System.out.println("Key:"+(char)key_press);
    	
    	return super.onKeyDown(keyCode, event);
    }    
}
