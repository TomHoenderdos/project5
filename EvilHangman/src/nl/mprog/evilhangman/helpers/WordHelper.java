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
		//return "MAGNETRON";
	}
	
	/**
	 * Gets an evil word with current game context
	 * @param currentWord the current word
	 * @param matchPattern the pattern it has to match
	 * @param usedCharacters used characters
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

//		long m2 = System.currentTimeMillis();
//		System.out.println("getEvilWord glob:" + (m1-m2) + "ms");
//		m1 = System.currentTimeMillis();
		
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
		
//		m2 = System.currentTimeMillis();
//		System.out.println("getEvilWord largeglob:" + (m1-m2) + "ms");
//		m1 = System.currentTimeMillis();
		
		// execute query
		this.open();

		String selectQuery = "SELECT name, LENGTH(name) as nameLength " +
		"FROM words " +
		"WHERE nameLength == " + Integer.toString(currentWord.length()) + " " +
		"AND name LIKE '" + matchPattern + "' " +
		"AND name GLOB '" + large_glob + "' " +
		"GROUP BY rowid " +
		"HAVING nameLength - LENGTH(REPLACE(name, 'E', '')) = " +
			"(SELECT MIN(LENGTH(name) - LENGTH(REPLACE(name, 'E', ''))))" +
		"ORDER BY RANDOM() LIMIT 1";			

		Cursor cursor = db.rawQuery(selectQuery, null);
		cursor.moveToFirst();

		String word = null;
		if(cursor.getCount() > 0) { 
			word = cursor.getString(0);
		}

		cursor.close();
		this.close();

//		m2 = System.currentTimeMillis();
//		System.out.println("getEvilWord query:" + (m1-m2) + "ms");
		return word == null ? currentWord : word.toLowerCase();
	}
}