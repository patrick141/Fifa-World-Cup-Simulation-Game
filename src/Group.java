import java.util.*;
public class Group {
	private String name;
	private Team groupLeader;
	private ArrayList<Team> teams;
	private ArrayList<Team> sorted;
	private ArrayList<Match> matches;
	Scanner insert = new Scanner(System.in);
	
	public Group(String name, Team groupLeader)
	{
		this.name = name;
		teams = new ArrayList<Team>(4);
		matches = new ArrayList<Match>(6);
		this.groupLeader = groupLeader;
		teams.add(groupLeader);
	}
	
	public Group(String name)
	{
		this.name = name;
		teams = new ArrayList<Team>(4);
		matches = new ArrayList<Match>(6);
	}
	
	public void AddGroupLeader(Team AB)
	{
		groupLeader = AB;
		teams.add(0,groupLeader);
	}
	
	public Team getTeam(int a)
	{
		return teams.get(a);
	}
	
	public String getName()
	{
		return name;
	}
	
	public void addTeam(Team e)
	{
		teams.add(e);
	}
	
	public void generateMatches()
	{
		if(teams.size() == 4)
		{
			Team A = teams.get(0);
		    Team B = teams.get(1);
		    Team C = teams.get(2);
		    Team D = teams.get(3);
		    Match AB = new Match(A,B);
		    Match CD = new Match(C,D);
		    Match AC = new Match(A,C);
		    Match DB = new Match(D,B);
		    Match BC = new Match(B,C);
		    Match DA = new Match(D,A);
		    matches.add(AB);
		    matches.add(CD);
		    matches.add(AC);
		    matches.add(DB);
		    matches.add(BC);
		    matches.add(DA);
		}
		else
		{
			System.out.println("Need " + (4-teams.size()) + " more teams to create a group.");
		}
	}
	
	public void setMatches()
	{
		if(matches.size() == 6)
		{
			for(int i = 0;i<matches.size();i++)
			{
				Match temp = matches.get(i);
				System.out.println("Match #" + (i+1) + ": " + temp.getHome() + " vs " + temp.getVistor());
				temp.setMatchresult();
				temp.setpoints();
			}
		}
		else
		{
			System.out.println("Need to fill Group");
		}
	}
	
	
	
	public void printTeams()
	{
		System.out.println("Group " + name + "'s Results:" );
		for(int i = 0; i < teams.size(); i++)
		{
			Team refer = teams.get(i);
			System.out.println(refer + "  Pts: " + refer.getPoints() + " GD: " + refer.getGoalDifference());
		}
	}
	
	public void arrangeStandings()
	{
		sorted = new ArrayList<Team>(4);
		Team p = teams.get(0);
		Team q = teams.get(1);
		Team r = teams.get(2);
		Team s = teams.get(3);
		Team a = compareMe(p,q);
		Team b = compareMe(p,r);
		Team c = compareMe(p,s);
		Team d = compareMe(q,r);
		Team e = compareMe(q,s);
		Team f = compareMe(r,s);
		if(a == b && a == c) //P is 1
		{
			sorted.add(0,p);
			if(d == e)
			{
				sorted.add(1,q);
				sorted.add(2,f);
				sorted.add(3,loser(r,s));
			}
			else if(d == f)
			
			{
				sorted.add(1,r);
				sorted.add(2,e);
				sorted.add(3,loser(q,s));
			}
			else if(e == f)
			{
				sorted.add(1,s);
				sorted.add(2,d);
				sorted.add(3,loser(q,r));
			}
		}
		if(a == d && d == e) //Q is 1
		{
			sorted.add(0,q);
			if(b == c)
			{
				sorted.add(1,p);
				sorted.add(2,f);
				sorted.add(3,loser(r,s));
			}
			else if(b == f)
			{
				sorted.add(1,r);
				sorted.add(2,c);
				sorted.add(3,loser(p,s));
			}
			else if(c == f)
			{
				sorted.add(1,s);
				sorted.add(2,b);
				sorted.add(3,loser(p,r));
				
			}
		}
		if(b == d && b == f) //R is 1
		{
			sorted.add(r);
			if(a == c)
			{
				sorted.add(1,p);
				sorted.add(2,e);
				sorted.add(3,loser(q,s));
			}
			else if(a == e)
			{
				sorted.add(1,q);
				sorted.add(2,c);
				sorted.add(3,loser(p,s));
			}
			else if(c == e)
			{
				sorted.add(1,s);
				sorted.add(2,a);
				sorted.add(3,loser(p,q));
			}
			
		}
		if(c == e && c == f)
		{
			sorted.add(0,s); //S is 1
			if(a == b)
			{
				sorted.add(1,p);
				sorted.add(2,d);
				sorted.add(3,loser(q,r));
			}
			else if(a == d)
			{
				sorted.add(1,q);
				sorted.add(2,b);
				sorted.add(3,loser(p,r));
			}
			else if(c == e)
			{
				sorted.add(1,r);
				sorted.add(2,a);
				sorted.add(3,loser(p,q));
			}
		}
	}
	
	public Team compareMe(Team a, Team b)
	{
		if(a.getPoints() > b.getPoints())
		{
			return a;
		}
		else if(a.getPoints() < b.getPoints())
		{
			return b;
		}
		else
		{
			if(a.getGoalDifference() > b.getGoalDifference() )
			{
				return a;
			}
			else if(a.getGoalDifference() < b.getGoalDifference() )
			{
				return b;
			}
			else
			{
				if(a.getGoalsScored() > b.getGoalsScored() )
				{
					return a;
				}
				else if(a.getGoalsScored() < b.getGoalsScored() )
				{
					return b;
				}
				else
				{
					for(int i = 0; i < matches.size();i++)
					{
						Match test = matches.get(i);
						if(test.getHome() == a && test.getVistor() == b)
						{
							Team xx = test.getWinner();
							if(xx == null)
							{
								ArrayList<Team> comparison = new ArrayList<Team>();
								comparison.add(a);
								comparison.add(b);
								int Randomindex = (int) (Math.random() * comparison.size());
								return comparison.get(Randomindex);
							}
							else
							{
								return xx;
							}
						}
						else if(test.getHome() == b && test.getVistor() == a)
						{
							Team xy = test.getWinner();
							if(xy == null)
							{
								ArrayList<Team> comparison = new ArrayList<Team>();
								comparison.add(a);
								comparison.add(b);
								int Randomindex = (int) (Math.random() * comparison.size());
								return comparison.get(Randomindex);
							}
							else
							{
								return xy;
							}
						}
						else
						{
							System.out.println("I don't know how won so first Team gets this.");
							return null;
						}
					}
				}
			}
		}
		System.out.println("Hmm, I can't compare.");
		return null;
	}
	
	public Team loser(Team a, Team b)
	{
		Team temp = compareMe(a,b);
		if(temp == a)
		{
			return b;
		}
		else
		{
			return a;
		}
	}
	
	public void printStandings()
	{
		try 
		{
		System.out.println("Group " + name + "'s Standings:" );
		for(int i = 0; i<sorted.size();i++)
		{
			Team refer = sorted.get(i);
			System.out.println((i+1) + "." + refer + "  Pts: " + refer.getPoints() + " GD: " + refer.getGoalDifference());
		}
		}catch(Exception e)
		{
			System.out.println("Need to arrange standings");
		}
		
	}
	
	public Team getGroupWinner()
	{
		return sorted.get(0);
	}
	
	
	public Team getGroupRunnerUp()
	{
		return sorted.get(1);
	}
	
	public String toString()
	{
		String count = "Group: " + name + ": ";
		for(int i = 0;i<teams.size();i++ )
		{
			count += teams.get(i) + " ";
		}
		return count;
	}
}

