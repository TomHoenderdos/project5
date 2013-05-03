package nl.mprog.evilhangman.helpers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import nl.mprog.evilhangman.controllers.DatabaseHandler;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 * WordHelper takes care of all queries on the database involved with getting words.
 * @author Jeroen Grootendorst
 *
 */
public enum WordHelper {
	instance;

	private DatabaseHandler dbhandler = null;
	private SQLiteDatabase db = null;	

	/**
	 * Initialize the database
	 * @param ctx
	 */
	public void initialize(Context ctx) {
		dbhandler = new DatabaseHandler(ctx);
		try {
			dbhandler.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Open the database
	 */
	private void openDatabase(){
		dbhandler.openDataBase();
		db = dbhandler.getReadableDatabase();
	}

	/**
	 * Close the database
	 */
	private void closeDatabase() {
		dbhandler.close();
	}	
	/**
	 * Gets a random word from the database
	 * @return String word
	 */
	public String getRandomWord() {
		this.openDatabase();

		String selectQuery = ("SELECT name FROM words WHERE len ==" + SettingsHelper.instance.getSettings().getWordCount() + " ORDER BY RANDOM() LIMIT 1");

		Cursor cursor = db.rawQuery(selectQuery, null);
		cursor.moveToFirst();
		String word = cursor.getString(0);


		cursor.close();
		this.closeDatabase();
		return word;
	}

	/**
	 * Gets an evil word with current game context
	 * @param currentWord the current word
	 * @param matchPattern the pattern it has to match
	 * @param usedCharacters used characters
	 * @param pressedKey the character that was just pressed
	 * @return
	 */
	public String getEvilWord(String currentWord, String matchPattern, ArrayList<Character> usedCharacters, char pressedKey){

		//		long m1 = System.currentTimeMillis();

		// generate globbing part for non guessed characters
		StringBuilder glob = new StringBuilder();

		glob.append('[');
		for(char c = 'a'; c <= 'z'; c++) {
			if(!usedCharacters.contains(c) && c != pressedKey) {
				glob.append(Character.toUpperCase(c));
			}
		}
		glob.append(']');

		// generate the globbing part of every character
		StringBuilder large_glob = new StringBuilder();

		for(int i = 0; i < currentWord.length(); i++) {
			StringBuilder newGlob = glob;
			char c = currentWord.charAt(i);
			if(c != '_') {
				int idx = newGlob.indexOf(String.valueOf(Character.toUpperCase(c)));
				if(idx != -1) {
					newGlob.replace(idx, idx+1, "");
				}
			}
			large_glob.append(newGlob);
		}

		// execute query
		this.openDatabase();
		String selectQuery = "SELECT name " +
				"FROM words " +
				"WHERE len == " + Integer.toString(currentWord.length()) + " " +
				"AND name GLOB '" + large_glob + "' " +
				"AND name LIKE '" + matchPattern + "' " +
				"GROUP BY rowid " +
				"HAVING len - LENGTH(REPLACE(name, 'E', '')) = " +
				"(SELECT MIN(len - LENGTH(REPLACE(name, 'E', ''))))" +
				"ORDER BY RANDOM() LIMIT 1";			
		Cursor cursor = db.rawQuery(selectQuery, null);
		cursor.moveToFirst();
		String word = null;
		if(cursor.getCount() > 0) { 
			word = cursor.getString(0);
		}
		cursor.close();
		this.closeDatabase();

		return word == null ? currentWord : word.toLowerCase(Locale.getDefault());
	}
}
