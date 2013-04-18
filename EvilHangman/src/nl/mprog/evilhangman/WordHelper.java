package nl.mprog.evilhangman;

import java.io.IOException;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WordHelper {
	
	private DatabaseHandler dbhandler = null;
	private SQLiteDatabase db = null;
	
	public WordHelper(Context context) {
		
		dbhandler = new DatabaseHandler(context.getApplicationContext());
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

	public String getWord(int WordLength){
			open();
				String selectQuery = "SELECT * From WordList WHERE length(name) == " + Integer.toString(WordLength) + " ORDER BY RANDOM() LIMIT 1";
				Cursor cursor = db.rawQuery(selectQuery, null);
				cursor.moveToFirst();
				String word = cursor.getString(0);
			close();
		return word;
	}

}
