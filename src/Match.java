import java.util.*;

public class Match {
	private int number;
	private static int numMatches;
	private Team team1;
	private Team team2;
	private int result1;
	private int result2;
	Scanner input = new Scanner(System.in);
	
	public Match(Team team1,int result1, Team team2, int result2)
	{
		this.team1 = team1;
		this.result1 = result1;
		this.team2 = team2;
		this.result2 = result2;
		numMatches++;
	}
	
	public Match(Team team1, Team team2)
	{
		this.team1 = team1;
		this.team2 = team2;
		result1 = 0;
		result2 = 0;
		numMatches++;
	}
	
	public void setMatchresult()
	{
		System.out.println(team1.getCountry() + ": ");
		result1 = input.nextInt();
		System.out.println(team2.getCountry() + ": ");
		result2 = input.nextInt();
	}
	
	public Team getHome()
	{
		return team1;
	}
	
	public Team getVistor()
	{
		return team2;
	}
	
	public Team getWinner()
	{
		if(result1 > result2)
		{
			return team1;
		}
		else if(result2 > result1)
		{
			return team2;
		}
		else
		{
			System.out.println("They drew");
			return null;
		}
	}
	
	public Team getLoser()
	{
		if(result1 < result2)
		{
			return team1;
		}
		else if(result2 < result1)
		{
			return team2;
		}
		else
		{
			System.out.println("They drew");
			return null;
		}
	}
	
	public void setpoints()
	{
		if(result1 > result2)
		{
			team1.setPoints(3);
			team2.setPoints(0);
		}
		else if(result2 > result1)
		{
			team1.setPoints(0);
			team2.setPoints(3);
		}
		else
		{
			team1.setPoints(1);
			team2.setPoints(1);
		}
		
		team1.setGoalsScored(result1);
		team1.setGoalsAllowed(result2);
		team2.setGoalsScored(result2);
		team2.setGoalsAllowed(result1);
	}
	
	public void printResult()
	{
		System.out.println(team1.getCountry() + " " + result1 + "-" + result2 + " " + team2.getCountry());
	}
	
	@Override
	public String toString()
	{
		return team1 + " vs " + team2;
	}
}
