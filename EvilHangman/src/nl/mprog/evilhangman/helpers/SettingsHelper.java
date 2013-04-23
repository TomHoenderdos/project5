package nl.mprog.evilhangman.helpers;

import java.io.IOException;

import nl.mprog.evilhangman.controllers.DatabaseHandler;
import nl.mprog.evilhangman.models.SettingsModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public enum SettingsHelper {
	instance;
	
	private DatabaseHandler dbhandler = null;
	private SQLiteDatabase db = null;
	
	//Settings table name
	private static final String TABLE_SETTINGS = "settings";
	
    // Settings Columns names
    private static final String KEY_EVIL = "evil";
    private static final String KEY_MAX_AT = "maxattempts";
    private static final String KEY_WC = "wordcount";
    
    private int count = 0;
    
    public void initialize(Context ctx) {
    	
    	dbhandler = new DatabaseHandler(ctx.getApplicationContext());
    	try {
			dbhandler.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	private void open(){
		dbhandler.openDataBase();
		db = dbhandler.getReadableDatabase();
	}
	
	private void close() {
		dbhandler.close();
	}
    
 // Get settings
    public SettingsModel getSettings(){
    	this.open();
    	
    	// Select all from table settings
        String selectQuery = "SELECT * FROM " + TABLE_SETTINGS;

    	Cursor cursor = db.rawQuery(selectQuery, null);
    	SettingsModel setting = new SettingsModel();
    	
    	//Loop through the rows and add to the settings list
    	cursor.moveToFirst();
    	if(cursor.getCount() > 0){
    		setting.setEvil(Boolean.parseBoolean(cursor.getString(0))); //Evil
        	setting.setMaxAttempts(Integer.parseInt(cursor.getString(1))); //Max Attempts
        	setting.setWordCount(Integer.parseInt(cursor.getString(2))); //Wordcount
    	}

        // return list of settings
    	cursor.close();
    	this.close();
        return setting;

    }

    // Save settings
    public void saveSettings(SettingsModel settingsModel){
    	this.open();
    	 ContentValues values = new ContentValues();
    	 values.put(KEY_EVIL, settingsModel.getEvil());
    	 values.put(KEY_MAX_AT, settingsModel.getMaxAttempts());
    	 values.put(KEY_WC, settingsModel.getWordCount());
    	  
    	 if (count == 0) {
    		 db.insert(TABLE_SETTINGS, null, values);
    	 } else {
    		 db.update(TABLE_SETTINGS, values, null, null);
    	 }
    	 count++;
    	// Inserting Row
    	 
        
        this.close();
    }
}
