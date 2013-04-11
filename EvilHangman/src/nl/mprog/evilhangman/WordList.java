package nl.mprog.evilhangman;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Random;

import android.content.Context;
import android.util.SparseArray;

public class WordList extends SparseArray<WordListItem>{
	
	private WordListItem current_word_list_model;
	
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
		}
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


