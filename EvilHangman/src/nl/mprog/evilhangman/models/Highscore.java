package nl.mprog.evilhangman.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import nl.mprog.evilhangman.controllers.DatabaseHandler;


public class Highscore {
	//	private vars
	private String highscoreName;
	private int highscoreScore;
	private String highscoreWord;

	// Database vars
	private DatabaseHandler dbhandler = null;
	private SQLiteDatabase db = null;
	
	private Context myContext = null;
	
	// Table name
    private static final String TABLE_HIGHSCORES = "highscores";

	// Highscore Columns names
    private static final String KEY_NAME = "name";
    private static final String KEY_SCORE = "score";
    private static final String KEY_WORD = "word";

//    Empty constructor
    public Highscore(){
    	
    }
    
	// Highscore with Database connection Constructor
	public Highscore(Context ctx){
//		Log.w("Constructor Highscore", ""+ctx);
		this.myContext = ctx;
		dbhandler = new DatabaseHandler(ctx.getApplicationContext());
		try {
			dbhandler.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	//	Model Constructor
	public Highscore(String name, Integer score, String word) {
		super();
		highscoreName = name;
		highscoreScore = score;
		highscoreWord = word;
	}

	//	Get/Set Name
	public String getName() {
		return highscoreName;
	}
	public void setName(String name) {
		highscoreName = name;
	}

	//	Get/Set Score
	public Integer getScore() {
		return highscoreScore;
	}
	public void setScore(Integer score) {
		highscoreScore = score;
	}

	//	Get/Set Word
	public String getWord() {
		return highscoreWord;
	}
	public void setWord(String word) {
		highscoreWord = word;
	}

	private void open(){
		dbhandler.openDataBase();
		db = dbhandler.getReadableDatabase();
	}

	private void close() {
		dbhandler.close();
		db = null;
	}

	public void save(){
		this.open();
		
//		Log.w("Name", this.getName());
//		Log.w("Score", ""+this.getScore());
//		Log.w("Word", this.getWord());
		
		
		ContentValues values = new ContentValues();
    	values.put(KEY_NAME, this.getName());
    	values.put(KEY_SCORE, this.getScore());
    	values.put(KEY_WORD, this.getWord());

    	// Inserting Row
        db.insert(TABLE_HIGHSCORES, null, values);
        this.close(); // Closing database connection
	}

	public List<Highscore> getHighscores(){
		this.open();

		List<Highscore> highscoreList = new ArrayList<Highscore>();

		// Select All from table highscore
        String selectQuery = "SELECT * FROM " + TABLE_HIGHSCORES;

    	Cursor cursor = db.rawQuery(selectQuery, null);

    	// Loop through all rows and add to the List
//    	try {
    	cursor.moveToFirst();
//			Log.w("SQL NAME", cursor.getString(0));
//			Log.w("SQL SCORE", cursor.getString(1));
//			Log.w("SQL WORD", cursor.getString(2));
			
			while (cursor.moveToNext()){
				Highscore highscore = new Highscore();
				
//				Log.w("SQL NAME", cursor.getString(0));
//				Log.w("SQL SCORE", cursor.getString(1));
//				Log.w("SQL WORD", cursor.getString(2));
			   	
				highscore.setName(cursor.getString(0).replace("\n", "").replace("\r", "")); //Name
				highscore.setScore(Integer.parseInt(cursor.getString(1))); //Score
				highscore.setWord(cursor.getString(2)); //Word
			    
				highscoreList.add(highscore);
				highscore = null;
//				Log.w("HighscoreList(0)", ""+highscoreList.get(0).getName());
			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block		
//			Log.w("Name", Highscore.getName());
//			Log.w("Score", ""+Highscore.getScore());
//			Log.w("Word", Highscore.getWord());
//			
//			e.printStackTrace();
//			return highscoreList;
//			
//		}
    	this.close(); // Closing database connection

    	return highscoreList;
	}
}
