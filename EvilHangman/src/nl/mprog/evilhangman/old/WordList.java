package nl.mprog.evilhangman.old;


import java.util.Random;

import android.content.Context;
import android.util.SparseArray;

@Deprecated
/**
 * @author Jeroen Grootendorst
 * @deprecated Use WordHelper.java
 */
public class WordList extends SparseArray<WordListItem>{
	
	private WordListItem current_word_list_model;
	private Context context = null;
	
	public WordList(Context ctx) {
		
		this.parseWords(ctx);
		
	}
	
	public void parseWords(Context ctx) {
 
//	    try {			
//	    	BufferedInputStream bis = new BufferedInputStream(ctx.getResources().openRawResource(R.raw.words));
//	    	BufferedReader reader = new BufferedReader(new InputStreamReader(bis));
//	    	
//	    	try {
//	    		String line = null; 
//
//				while (bis.available() > 0) {
//					reader.readLine();
//				}
//				
//	    		while (( line = input.readLine()) != null){    				
//    				int key = line.length();
//    				if(this.get(key) == null) {
//    					this.put(line.length(), new WordListItem());
//    				}
//    				this.get(key).add(line.toLowerCase());
//	    		}
//	    	}
//	    	finally {
//	    		bis.close();
//	    		dis.close();
//	    	}
//	    }
//	    catch (FileNotFoundException ex) {
//	    }
//	    catch (IOException ex){
//	    }
		

    	/*WordHelper wh = new WordHelper(ctx);  
    	Cursor cursor = wh.getAllWords();
		while(cursor.moveToNext()) {
			String word = cursor.getString(0);
			int key = word.length();
			if(this.get(key) == null) {
				this.put(word.length(), new WordListItem());
			}
			this.get(key).add(word.toLowerCase());
		}*/
    	/*
		String[] words = ctx.getResources().getStringArray(R.array.word_list);
		for(String word : words) {
			int key = word.length();
			if(this.get(key) == null) {
				this.put(word.length(), new WordListItem());
			}
			this.get(key).add(word.toLowerCase());
		}
		for(int i = 0; i < this.size(); i++) {
		   Collections.sort(this.valueAt(i));
		}*/
	}
	
	public String getRandomWord() {
		Random rand = new Random();
		current_word_list_model = this.valueAt(rand.nextInt(this.size()));
		return current_word_list_model.get(rand.nextInt(current_word_list_model.size()));
	}

	public WordListItem getCurrent_word_list_model() {
		return current_word_list_model;
	}

	public void setCurrent_word_list_model(WordListItem current_word_list_model) {
		this.current_word_list_model = current_word_list_model;
	}	
	
}


