package nl.mprog.evilhangman.helpers;
import java.io.IOException;
import nl.mprog.evilhangman.controllers.DatabaseHandler;
import nl.mprog.evilhangman.models.SettingsModel;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
public enum SettingsHelper {
	instance;

	//	Private vars
	// Database	
	private DatabaseHandler dbhandler = null;
	private SQLiteDatabase db = null;

	// Settings table name
	private static final String TABLE_SETTINGS = "settings";

	// Settings Columns names
	private static final String KEY_EVIL = "evil";
	private static final String KEY_MAX_AT = "maxattempts";
	private static final String KEY_WC = "wordcount";

	// instance constructor	
	public void initialize(Context ctx) {
		dbhandler = new DatabaseHandler(ctx.getApplicationContext());
		try {
			dbhandler.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void openDatabase(){
		dbhandler.openDataBase();
		db = dbhandler.getReadableDatabase();
	}

	private void closeDatabase() {
		dbhandler.close();
	}

	// Get settings
	public SettingsModel getSettings(){
		this.openDatabase();

		// Select all from table settings
		String selectQuery = "SELECT * FROM " + TABLE_SETTINGS;
		Cursor cursor = db.rawQuery(selectQuery, null);
		SettingsModel setting = new SettingsModel();

		//Loop through the rows and add to the settings list
		cursor.moveToFirst();
		if(cursor.getCount() > 0){
			boolean evil = (Integer.parseInt(cursor.getString(0)) != 0);
			setting.setEvil(evil); //Evil
			setting.setMaxAttempts(Integer.parseInt(cursor.getString(1))); //Max Attempts
			setting.setWordCount(Integer.parseInt(cursor.getString(2))); //Wordcount
		}

		// return list of settings
		cursor.close();
		this.closeDatabase();
		return setting;
	}

	// Save settings
	public void saveSettings(SettingsModel settingsModel){
		ContentValues values = new ContentValues();
		values.put(KEY_EVIL, settingsModel.getEvil());
		values.put(KEY_MAX_AT, settingsModel.getMaxAttempts());
		values.put(KEY_WC, settingsModel.getWordCount());


		this.openDatabase();
		db.update(TABLE_SETTINGS, values, null, null);
		this.closeDatabase();
	}
}
