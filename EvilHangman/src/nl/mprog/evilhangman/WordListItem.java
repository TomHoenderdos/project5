package nl.mprog.evilhangman;

import java.util.ArrayList;
import java.util.Random;

public class WordListItem extends ArrayList<String> {

	private static final long serialVersionUID = 1L;

	public String getEvilWord(String old_word, String match_holder, ArrayList<Character> guessed_characters, char latest_char) {
		String new_word = old_word;				
		int new_score = (int) Double.NEGATIVE_INFINITY;
		char[] matches = match_holder.toCharArray();
		
		for (String s : this) {
			char[] words = s.toCharArray();
			int word_score = 0;
			
			int i = 0;
			for(; i < matches.length; i++) {
				if(matches[i] == '_') { 
					if(words[i] == latest_char) {
						word_score--;
						continue;
					}
					
					boolean outside_break = false;
					for(char c : guessed_characters) {
						if(words[i] == c) {
							outside_break = true;
							break;
						}
					}
					if(outside_break) break;
					
					continue;
				}
				
				if(match_holder.charAt(i) != words[i]) {
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
 