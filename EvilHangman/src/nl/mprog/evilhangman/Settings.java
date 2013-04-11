package nl.mprog.evilhangman;

public class Settings {

	//	Private vars
	private Boolean Evil;
	private Integer MinWordCount;
	private Integer MaxAttempts;
	private Integer MaxWordCount;

	// Constructor
	public Settings(Boolean evil, Integer maxAttempts, Integer minWordCount,
			Integer maxWordCount) {
		super();
		Evil = evil;
		MaxAttempts = maxAttempts;
		MinWordCount = minWordCount;
		MaxWordCount = maxWordCount;
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

	//	Get/Set MinWordCount
	public Integer getMinWordCount() {
		return MinWordCount;
	}

	public void setMinWordCount(Integer minWordCount) {
		MinWordCount = minWordCount;
	}

	//	Get/Set MaxWordCount
	public Integer getMaxWordCount() {
		return MaxWordCount;
	}

	public void setMaxWordCount(Integer maxWordCount) {
		MaxWordCount = maxWordCount;
	}
}
