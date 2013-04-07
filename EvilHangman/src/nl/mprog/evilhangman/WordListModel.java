package nl.mprog.evilhangman;

import java.util.ArrayList;
import java.util.Random;

public class WordListModel extends ArrayList<String> {

	private static final long serialVersionUID = 1L;

	public String GetEvilWord(String old_word, String match_holder, ArrayList<Character> guessed_characters) {
		String new_word = old_word;				
		int new_score = (int) Double.NEGATIVE_INFINITY;
		char[] matches = match_holder.toCharArray();
		
		for (String s : this) {
			char[] words = s.toCharArray();
			int word_score = 0;
			
			int i = 0;
			for(; i < matches.length; i++) {
				if(matches[i] == '_') { 
					for(char c : guessed_characters) {
						if(words[i] == c) {
							word_score--;
							break;
						}
					}
					continue;
				}
				
				if(matches[i] != words[i]) {
					break;
				}
			}
			if(i == matches.length) {
				if(word_score > new_score) {
					new_word = s;
					new_score = word_score;
				}
			}
		}
		return new_word;
	}	
}
 