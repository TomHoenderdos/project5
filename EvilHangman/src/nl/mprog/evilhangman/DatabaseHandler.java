package nl.mprog.evilhangman;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "EvilHangman";

    // Table names
    private static final String TABLE_SETTINGS = "settings";
    private static final String TABLE_HIGHSCORES = "highscores";

    // Settings Columns names
    private static final String KEY_EVIL = "evil";
    private static final String KEY_MAX_AT = "maxattempts";
    private static final String KEY_MIN_WC = "min_wordcount";
    private static final String KEY_MAX_WC = "max_wordcount";

    // Highscore Columns names
    private static final String KEY_NAME = "name";
    private static final String KEY_SCORE = "score";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Ceating the Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SETTINGS_TABLE = "CREATE TABLE " + TABLE_SETTINGS + "("
                + KEY_EVIL + " BOOLEAN,"
        		+ KEY_MAX_AT + " INTEGER,"
        		+ KEY_MIN_WC + " INTEGER,"
        		+ KEY_MAX_WC + " INTEGER" + ")";

        String CREATE_HIGHSCORES_TABLE = "CREATE TABLE " + TABLE_HIGHSCORES + "("
                + KEY_NAME + " VARCHAR(255),"
        		+ KEY_SCORE + " INTEGER" + ")";

        db.execSQL(CREATE_SETTINGS_TABLE);
        db.execSQL(CREATE_HIGHSCORES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIGHSCORES);

        // Create tables again
        onCreate(db);
    }


    //Get Wordlist
    public Wordlist getWordList(int wordLength){

        String selectQuery = "SELECT * FROM WordList WHERE length(name) == " + wordLength;
    }


    public Wordlist getWordList(){

        String selectQuery = "Select * FROM WordList";
    }

    //Adding new Highscore
    public void addHighscore(Highscore highscore){
    	SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, highscore.getName());
        values.put(KEY_SCORE, highscore.getScore());

        // Inserting Row
        db.insert(TABLE_HIGHSCORES, null, values);
        db.close(); // Closing database connection
    }

    // Get all highscores
    public Highscore getHighscores(){
        List<Highscore> highscoreList = new ArrayList<Highscore>();

        // Select All from table highscore
        String selectQuery = "SELECT  * FROM " + TABLE_HIGHSCORES;

    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(selectQuery, null);

    	// Loop through all rows and add to the List
    	if (cursor.moveToFirst()){
    		do {
    			Highscore highscore = new Highscore();
                highscore.setName(cursor.getString(0)); //Name
    			highscore.setScore(cursor.getString(1)); //Score
                highscoreList.add(highscore);
    		} while (cursor.moveToNext());
    	}
    	return highscoreList;
    }

    // Get settings
    public Settings getSettings(){
    	List<Settings> settingsList = new ArrayList<Settings>();

        // Select all from table settings
        String selectQuery = "SELECT  * FROM " + TABLE_SETTINGS;

    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(selectQuery, null);

    	//Loop through the rows and add to the settings list
    	if(cursor.moveToFirst()){
    		Settings setting = new Settings();
            setting.setEvil(cursor.getString(0)); //Evil
    		setting.setMaxAttempts(cursor.getString(1)); //Max Attempts
    		setting.setMaxWordCount(cursor.getString(2)); //Min Wordcount
    		setting.setMinWordCount(cursor.getString(3)); //Max Wordcount
            settingsList.add(setting)
    	} while (cursor.moveToNext());

        // return list of settings
        return settingsList;

    }

    // Save settings
    public void saveSettings(Settings settings){
    	 SQLiteDatabase db = this.getWritableDatabase();

    	 ContentValues values = new ContentValues();
    	 values.put("KEY_EVIL", Settings.get(0).toString());
    	 values.put("KEY_MAX_AT", Settings.get(1).toString());
    	 values.put("KEY_MIN_WC", Settings.get(2).toString());
    	 values.put("KEY_MAX_WC", Settings.get(3).toString());

    	// Inserting Row
        db.insert(TABLE_SETTINGS, null, values);
        db.close(); // Closing database connection
    }
}
