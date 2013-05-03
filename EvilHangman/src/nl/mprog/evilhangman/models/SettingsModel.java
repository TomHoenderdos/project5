package nl.mprog.evilhangman.models;
/**
 * @author tom
 * Highscore is used for getting and saving higscores into the database
 */

public class SettingsModel {

	//	Private vars
	private boolean evilSetting;
	private int maxAttemptsSetting;
	private int wordCountSetting;

	//Empty Constructor
	public SettingsModel(){

	}

	// Constructor
	public SettingsModel(boolean evil, int maxAttempts, int wordCount) {
		super();
		evilSetting = evil;
		maxAttemptsSetting = maxAttempts;
		wordCountSetting = wordCount;
	}

	//	Get/Set Evil
	public boolean getEvil() {
		return evilSetting;
	}
	public void setEvil(boolean evil) {
		evilSetting = evil;
	}

	//	Get/Set MaxAttempts
	public int getMaxAttempts() {
		return maxAttemptsSetting;
	}
	public void setMaxAttempts(int maxAttempts) {
		maxAttemptsSetting = maxAttempts;
	}

	// Get/Set WordCount
	public int getWordCount() {
		return wordCountSetting;
	}
	public void setWordCount(int wordCount) {
		wordCountSetting = wordCount;
	}
}
