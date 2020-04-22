import java.util.Scanner;

public class KnockoutMatch extends Match{
	private int score1;
	private int score2;
	private int pen1;
	private int pen2;
	Scanner input = new Scanner(System.in);
	private static int numMatches;
	public KnockoutMatch(Team team1, Team team2) {
		super(team1, team2);
		score1 = 0;
		score2 = 0;
		pen1 = 0;
		pen2 = 0;
		numMatches++;
	}
	
	@Override
	public void setMatchresult()
	{
		System.out.println(super.getHome() + " vs " + super.getVistor());
		System.out.println(super.getHome() + ": ");
		score1 = input.nextInt();
		System.out.println(super.getVistor() + ": ");
		score2 = input.nextInt();
		if(score1 == score2)
		{
			System.out.println("Penalty Shootout:");
			System.out.println(super.getHome() + ": ");
			pen1 = input.nextInt();
			System.out.println(super.getVistor() + ": ");
			pen2 = input.nextInt();
			while(pen1 == pen2)
			{
				System.out.println("They cannot draw.");
				System.out.println(super.getHome() + ": ");
				pen1 = input.nextInt();
				System.out.println(super.getVistor() + ": ");
				pen2 = input.nextInt();
			}
		}
		else
		{
			return;
		}
		super.setpoints();
	}
	@Override
	public Team getWinner()
	{
		if(score1 > score2)
		{
			return super.getHome();
		}
		else if(score1 < score2)
		{
			return super.getVistor();
		}
		else
		{
			if(pen1 > pen2)
			{
				return super.getHome();
			}
			else
			{
				return super.getVistor();
			}
		}
	}
	
	@Override
	public Team getLoser()
	{
		if(score1 < score2)
		{
			return super.getHome();
		}
		else if(score1 > score2)
		{
			return super.getVistor();
		}
		else
		{
			if(pen1 < pen2)
			{
				return super.getHome();
			}
			else
			{
				return super.getVistor();
			}
		}
	}
	
	private static int getNumMatches()
	{
		return numMatches;
	}
}
