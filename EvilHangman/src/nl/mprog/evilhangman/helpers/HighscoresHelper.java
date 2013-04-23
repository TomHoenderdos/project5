package nl.mprog.evilhangman.helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.mprog.evilhangman.controllers.DatabaseHandler;
import nl.mprog.evilhangman.models.Highscore;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public enum HighscoresHelper {
	instance;
	
	private DatabaseHandler dbhandler = null;
	private SQLiteDatabase db = null;
   // Table names
    private static final String TABLE_HIGHSCORES = "highscores";
	
 // Highscore Columns names
    private static final String KEY_NAME = "name";
    private static final String KEY_SCORE = "score";
	
    public void initialize(Context ctx) {
		dbhandler = new DatabaseHandler(ctx.getApplicationContext());
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
	
	 //Adding new Highscore
    public void addHighscore(Highscore highscore){
    	this.open();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, highscore.getName());
        values.put(KEY_SCORE, highscore.getScore());

        // Inserting Row
        db.insert(TABLE_HIGHSCORES, null, values);
        this.close(); // Closing database connection
    }

    // Get all highscores
    public List<Highscore> getHighscores(){
    	this.open();
    	List<Highscore> highscoreList = new ArrayList<Highscore>();

        // Select All from table highscore
        String selectQuery = "SELECT  * FROM " + TABLE_HIGHSCORES;

    	Cursor cursor = db.rawQuery(selectQuery, null);

    	// Loop through all rows and add to the List
    	if (cursor.moveToFirst()){
    		do {
    			Highscore highscore = new Highscore();
                highscore.setName(cursor.getString(0)); //Name
    			highscore.setScore(Integer.parseInt(cursor.getString(1))); //Score
                highscoreList.add(highscore);
    		} while (cursor.moveToNext());
    	}
    	this.close(); // Closing database connection
    	return highscoreList;
    }
	
}
