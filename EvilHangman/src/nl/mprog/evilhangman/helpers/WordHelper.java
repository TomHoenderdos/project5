package nl.mprog.evilhangman.helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import nl.mprog.evilhangman.controllers.DatabaseHandler;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
	private void open(){
		dbhandler.openDataBase();
		db = dbhandler.getReadableDatabase();
	}
	
	/**
	 * Close the database
	 */
	private void close() {
		dbhandler.close();
	}	

	/**
	 * Gets a random word from the database
	 * @return String word
	 */
	public String getRandomWord() {
		this.open();

		String selectQuery = "SELECT max(rowid) From words"; 
		Cursor cursor = db.rawQuery(selectQuery, null);
		cursor.moveToFirst();
		int max_rows = cursor.getInt(0);
		cursor.close();
		
		Random rand = new Random();
		int randomRow = rand.nextInt(max_rows);
		
		selectQuery = "SELECT * From words WHERE rowid = "+randomRow;
		cursor = db.rawQuery(selectQuery, null);
		cursor.moveToFirst();
		String word = cursor.getString(0);

		cursor.close();
		this.close();
		return word;
	}
	
	/**
	 * Gets an evil word with current game context
	 * @param currentWord the current word
	 * @param matchPattern the pattern it has to match
	 * @param usedCharacters used characters
	 * @return
	 */
	public String getEvilWord(String currentWord, String matchPattern, ArrayList<Character> usedCharacters, char pressedKey){
		
		StringBuilder glob = new StringBuilder();
		
		glob.append('[');
		for(char c = 'a'; c <= 'z'; c++) {
			if(!usedCharacters.contains(c) && c != pressedKey) {
				glob.append(c).append(Character.toUpperCase(c));
			}
		}
		glob.append(']');
		
		StringBuilder large_glob = new StringBuilder();
		for(int i = 0; i < currentWord.length(); i++) {
			large_glob.append(glob);
		}
		
		open();

		// to do:
		// seperate thread
		// sometimes adds characters that were guessed already
		String selectQuery = "SELECT name,MIN(LENGTH(name) - LENGTH(REPLACE(name, '" + Character.toUpperCase(pressedKey) + "', ''))) " +
				"From words " +
				"WHERE length(name) == " + Integer.toString(currentWord.length()) + 
				" AND name like '" + matchPattern + "'" + 
				(large_glob.length()!=0 ? " AND name glob '" + large_glob + "'" : "") + ";";
		Cursor cursor = db.rawQuery(selectQuery, null);
		cursor.moveToFirst();
		
		String word = cursor.getString(0);

		cursor.close();
		close();
		return word == null ? currentWord : word.toLowerCase();
	}
}
