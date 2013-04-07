package nl.mprog.evilhangman;

import android.content.Context;
import android.util.SparseArray;

enum WordList {
	instance;
	
	private SparseArray<WordListModel> words = new SparseArray<WordListModel>();

	public SparseArray<WordListModel> getWords() {
		return words;
	}

	public void setWords(SparseArray<WordListModel> words) {
		this.words = words;
	}
	
	public void parseWords(Context ctx) {
		String[] words = ctx.getResources().getStringArray(R.array.word_list);
		for(String word : words) {
			int key = word.length();
			if(this.words.get(key) == null) {
				this.words.put(word.length(), new WordListModel());
			}
			this.words.get(key).add(word);
		}
	}
}
