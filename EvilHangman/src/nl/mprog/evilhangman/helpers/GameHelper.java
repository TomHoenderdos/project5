package nl.mprog.evilhangman.helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public enum GameHelper {
	instance;
	
	private int maxAttempts = 10;
	private int minimumWordLength = 1;
	private int maximumWordLength = 26;
	
	public int getMaxAttempts() {
		return maxAttempts;
	}
	
	public void setMaxAttempts(int maxAttempts) {
		this.maxAttempts = maxAttempts;
	}
	
	public int getMinimumWordLength() {
		return minimumWordLength;
	}
	
	public void setMinimumWordLength(int minimumWordLength) {
		this.minimumWordLength = minimumWordLength;
	}
	
	public int getMaximumWordLength() {
		return maximumWordLength;
	}
	
	public void setMaximumWordLength(int maximumWordLength) {
		this.maximumWordLength = maximumWordLength;
	}	
}
