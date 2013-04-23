package nl.mprog.evilhangman.models;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import nl.mprog.evilhangman.controllers.DatabaseHandler;


public class Highscore {
	//	private vars
	private String name;
	private Integer score;
	private String word;

	// Database vars
	private DatabaseHandler dbhandler = null;
	private SQLiteDatabase db = null;
	
	// Table name
    private static final String TABLE_HIGHSCORES = "highscores";

	// Highscore Columns names
    private static final String KEY_NAME = "name";
    private static final String KEY_SCORE = "score";
    private static final String KEY_WORD = "word";

	// Empty Constructor
	public Highscore(){

	}

	//	Constructor
	public Highscore(String name, Integer score, String word) {
		super();
		this.name = name;
		this.score = score;
		this.word = word;
	}

	//	Get/Set Name
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	//	Get/Set Score
	public Integer getScore() {
		return this.score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}

	//	Get/Set Word
	public String getWord() {
		return this.word;
	}
	public void setWord(String word) {
		this.word = word;
	}

	private void openConnection(){
		dbhandler.openDataBase();
		db = dbhandler.getReadableDatabase();
	}

	private void closeConnection(){
		db = null;
		dbhandler.close();
	}

	public void save(){
		this.openConnection();

		ContentValues values = new ContentValues();
    	values.put(KEY_NAME, getName());
    	values.put(KEY_SCORE, getScore());
    	values.put(KEY_WORD, getWord());

    	// Inserting Row
        db.insert(TABLE_HIGHSCORES, null, values);
        this.closeConnection(); // Closing database connection
	}

	public List<Highscore> getHighscores(){
		this.openConnection();

		List<Highscore> highscoreList = new ArrayList<Highscore>();

		// Select All from table highscore
        String selectQuery = "SELECT  * FROM " + TABLE_HIGHSCORES;

    	Cursor cursor = db.rawQuery(selectQuery, null);

    	// Loop through all rows and add to the List
    	cursor.moveToFirst();
    	while (cursor.moveToNext()){
    		Highscore highscore = new Highscore();
           	highscore.setName(cursor.getString(0)); //Name
    		highscore.setScore(Integer.parseInt(cursor.getString(1))); //Score
    		highscore.setWord(cursor.getString(2)); //Word
            highscoreList.add(highscore);
    	}
    	this.closeConnection(); // Closing database connection

    	return highscoreList;
	}
}
