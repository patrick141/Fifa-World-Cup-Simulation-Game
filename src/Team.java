import java.util.ArrayList;

public class Team {
	private String country;
	private int points;
	private int goalsScored;
	private int goalsAllowed;
	private int ranking;
	private final static int teamSize = 23;
	private ArrayList<Player> roster;
	
	public Team(String country, int ranking)
	{
		this.country = country;
		this.ranking = ranking;
		points = 0;
		goalsScored = 0;
		goalsAllowed = 0;
//		goalDifferential = 0;
		roster = new ArrayList<Player>(teamSize);
	}
	
	public Team(String country)
	{
		this.country = country;
		points = 0;
		goalsScored = 0;
		goalsAllowed = 0;
		roster = new ArrayList<Player>(teamSize);
	}
	
	public void addPlayer(Player a)
	{
		roster.add(a);
	}
	
	public void removePlayer(Player a)
	{
		for(int i = 0; i < roster.size(); i++)
		{
			if(roster.get(i) == a)
			{
				roster.remove(i);
				break;
			}
		}
	}
	
	public String getCountry()
	{
		return country;
	}
	
	public int getRanking()
	{
		return ranking;
	}
	
	public void setRanking(int newRank)
	{
		ranking = newRank;
	}
	
	public int getPoints()
	{
		return points;
	}
	
	public void setPoints(int point)
	{
		points+= point;
	}
	
	public int getGoalsScored()
	{
		return goalsScored;
	}
	
	public void setGoalsScored(int goals)
	{
		goalsScored += goals;
	}
	
	public int getGoalsAllowed()
	{
		return goalsAllowed;
	}
	
	public void setGoalsAllowed(int goals)
	{
		goalsAllowed += goals;
	}
	
	public int getGoalDifference() {
		return goalsScored - goalsAllowed;
	}
	
	
	public void printRoster()
	{
		for(int i = 0; i < roster.size(); i++)
		{
			System.out.println(roster.get(i));
		}
	}
	
	@Override
	public String toString()
	{
		return country;
	}
	
}
