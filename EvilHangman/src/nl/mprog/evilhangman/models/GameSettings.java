package nl.mprog.evilhangman.models;

/**
 * GameSettings is a singleton class that holds all game settings. These settings can
 * be changed by pressing the settings button in the menu dialog.
 * @author Jeroen Grootendorst
 *
 */
public enum GameSettings {
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
