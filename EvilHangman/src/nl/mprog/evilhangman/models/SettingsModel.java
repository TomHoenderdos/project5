package nl.mprog.evilhangman.models;

public class SettingsModel {

	//	Private vars
	private Boolean Evil;
	private Integer MaxAttempts;
	private Integer WordCount;
	
	//Empty Constructor
	public SettingsModel(){
		
	}
	
	// Constructor
	public SettingsModel(Boolean evil, Integer maxAttempts, Integer wordCount) {
		super();
		Evil = evil;
		MaxAttempts = maxAttempts;
		WordCount = wordCount;
	}

	//	Get/Set Evil
	public Boolean getEvil() {
		return Evil;
	}
	public void setEvil(Boolean evil) {
		Evil = evil;
	}

	//	Get/Set MaxAttempts
	public Integer getMaxAttempts() {
		return MaxAttempts;
	}
	public void setMaxAttempts(Integer maxAttempts) {
		MaxAttempts = maxAttempts;
	}

	// Get/Set WordCount
	public Integer getWordCount() {
		return WordCount;
	}

	public void setWordCount(Integer wordCount) {
		WordCount = wordCount;
	}
}
