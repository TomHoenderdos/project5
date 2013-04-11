package nl.mprog.evilhangman;

public class Highscore {
	//	private vars
	private String Name;
	private Integer Score;

	// Empty Constructor
	public Highscore(){
		
	}
	
	//	Constructor
	public Highscore(String name, Integer score) {
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
