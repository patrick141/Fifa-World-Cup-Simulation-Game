import java.util.ArrayList;

public class Pot{
	private String name;
	private ArrayList<Team> pothold;
	private ArrayList<Group> groups;

	
	public Pot(String name) 
	{
		this.name = name;
		pothold = new ArrayList<Team>(8);
		groups = new ArrayList<Group>(8);
	}
	
	public void addTeamPot(Team a)
	{
		pothold.add(a);
	}
	
	public void removeTeamPot(Team a)
	{
		pothold.remove(a);
	}
	
	public void generateGroups()
	{
		char c;
		for(c = 'A'; c <= 'H'; ++c)
		{
			String name = String.valueOf(c);
			Group AB = new Group(name);
			groups.add(AB);
		}
		
	}
	
	public Group getGroup(int i)
	{
		return groups.get(i);
	}
	
	public void drawPot(Group XX)
	{
		int Randomindex = (int) (Math.random() * pothold.size());
		System.out.println(pothold.get(Randomindex));
		try
		{
			XX.addTeam(pothold.get(Randomindex));
		}
		catch(Exception e)
		{
			System.out.println("No item");
		}
		pothold.remove(Randomindex);
	}
	
	public void printPot()
	{
		for(int i = 0;i<pothold.size();i++)
		{
			Team ab = pothold.get(i);
			System.out.println(ab +" Ranking:" +ab.getRanking());
		}
	}
	
	public void printGroups()
	{
		for(int i = 0;i<groups.size();i++)
		{
			System.out.println(groups.get(i));
		}
	}
}
