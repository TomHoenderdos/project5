package nl.mprog.evilhangman;

public class Highscores {
	//	private vars
	private String Name;
	private Integer Score;

	//	Constructor
	public Highscores(String name, Integer score) {
		super();
		Name = name;
		Score = score;
	}

	//	Get/Set Name
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}

	//	Get/Set Score
	public Integer getScore() {
		return Score;
	}
	public void setScore(Integer score) {
		Score = score;
	}

}
