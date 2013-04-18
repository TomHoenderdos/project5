package nl.mprog.evilhangman;

import java.io.IOException;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SettingsHelper {
	
	private DatabaseHandler dbhandler = null;
	private SQLiteDatabase db = null;
	
	//Settings table name
	private static final String TABLE_SETTINGS = "settings";
	
    // Settings Columns names
    private static final String KEY_EVIL = "evil";
    private static final String KEY_MAX_AT = "maxattempts";
    private static final String KEY_WC = "wordcount";
    
    public SettingsHelper(Context context){
    	
    	dbhandler = new DatabaseHandler(context.getApplicationContext());
    	try {
			dbhandler.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	public void open(){
		dbhandler.openDataBase();
		db = dbhandler.getReadableDatabase();
	}
	
	public void close() {
		dbhandler.close();
	}
    
 // Get settings
    public SettingsModel getSettings(){
    	open();
    	
    	// Select all from table settings
        String selectQuery = "SELECT  * FROM " + TABLE_SETTINGS;

    	Cursor cursor = db.rawQuery(selectQuery, null);
    	SettingsModel setting = new SettingsModel();
    	
    	//Loop through the rows and add to the settings list
    	if(cursor.moveToFirst()){
            setting.setEvil(Boolean.parseBoolean(cursor.getString(0))); //Evil
    		setting.setMaxAttempts(Integer.parseInt(cursor.getString(1))); //Max Attempts
    		setting.setWordCount(Integer.parseInt(cursor.getString(2))); //Wordcount
    	} while (cursor.moveToNext());

        // return list of settings
    	close();
        return setting;

    }

    // Save settings
    public void saveSettings(SettingsModel settingsModel){
    	open();
    	 ContentValues values = new ContentValues();
    	 values.put(KEY_EVIL, settingsModel.getEvil());
    	 values.put(KEY_MAX_AT, settingsModel.getMaxAttempts());
    	 values.put(KEY_WC, settingsModel.getWordCount());
    	 

    	// Inserting Row
        db.insert(TABLE_SETTINGS, null, values);
        close();
    }
}
