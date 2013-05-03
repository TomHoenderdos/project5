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
/**
 * @author tom
 * Highscore is used for getting and saving higscores into the database
 */
public class Highscore {

	//	private vars
	private String highscoreName;
	private int highscoreScore;
	private String highscoreWord;
	// Database vars

	private DatabaseHandler dbhandler = null;
	private SQLiteDatabase db = null;

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
		Log.w("Constructor Highscore", ""+ctx);
		dbhandler = new DatabaseHandler(ctx.getApplicationContext());
		try {
			dbhandler.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Open database for writing	
	private void openDatabase(){
		dbhandler.openDataBase();
		db = dbhandler.getReadableDatabase();
	}

	// Close the database when finished 
	private void closeDatabase() {
		dbhandler.close();
	}

	// Saves a new Highscore and insert into database	
	public void save(){
		this.openDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, this.getName());
		values.put(KEY_SCORE, this.getScore());
		values.put(KEY_WORD, this.getWord());

		// Inserting Row
		db.insert(TABLE_HIGHSCORES, null, values);
		this.closeDatabase(); // Closing database connection
	}

	// Get a list of Highscore models	
	public List<Highscore> getHighscores(){
		this.openDatabase();
		List<Highscore> highscoreList = new ArrayList<Highscore>();

		// Select All from table highscore
		String selectQuery = "SELECT * FROM " + TABLE_HIGHSCORES;
		Cursor cursor = db.rawQuery(selectQuery, null);

		// Loop through all results from query on db and add them to an ArrayList
		while (cursor.moveToNext()){
			Highscore highscore = new Highscore();
			highscore.setName(cursor.getString(0).replace("\n", "").replace("\r", "")); //Name
			highscore.setScore(Integer.parseInt(cursor.getString(1))); //Score
			highscore.setWord(cursor.getString(2)); //Word
			highscoreList.add(highscore);
		}

		this.closeDatabase(); // Closing database connection
		return highscoreList;
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
	public void setScore(int score) {
		highscoreScore = score;
	}

	//	Get/Set Word
	public String getWord() {
		return highscoreWord;
	}
	public void setWord(String word) {
		highscoreWord = word;
	}
}
