package nl.mprog.evilhangman.controllers;

import nl.mprog.evilhangman.R;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;

@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class Preferences extends PreferenceActivity {
	
	protected void onStart(){
		super.onStart();
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.preferences);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
//	public void onBackPressed(){
//		
//	}
	
}
