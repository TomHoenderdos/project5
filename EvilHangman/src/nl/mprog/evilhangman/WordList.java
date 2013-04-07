package nl.mprog.evilhangman;

import java.util.Collections;
import java.util.Random;

import android.content.Context;
import android.util.SparseArray;

public class WordList extends SparseArray<WordListModel>{
	
	private WordListModel current_word_list_model;
	
	public WordList(Context ctx) {
		this.parseWords(ctx);
	}
	
	public void parseWords(Context ctx) {
		String[] words = ctx.getResources().getStringArray(R.array.word_list);
		for(String word : words) {
			int key = word.length();
			if(this.get(key) == null) {
				this.put(word.length(), new WordListModel());
			}
			this.get(key).add(word);
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

	public WordListModel getCurrent_word_list_model() {
		return current_word_list_model;
	}

	public void setCurrent_word_list_model(WordListModel current_word_list_model) {
		this.current_word_list_model = current_word_list_model;
	}	
	
}


