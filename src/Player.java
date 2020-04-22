
public class Player {
	private String name;
	private int age;
	private int height;
	private String position;
	private int number;
	private double salary;
	private int goals;
	
	public Player(String name, int age, int height, String position, int number)
	{
		this.name = name;
		this.age = age;
		this.height = height;
		this.position = position;
		this.number = number;
		salary = 0;
		goals = 0;
	}
	public void setSalary(double ref)
	{
		salary = ref;
	}
	public void addgoals(int aa)
	{
		goals+=aa;
	}
	public int getPlayerGoals()
	{
		return goals;
	}
	public double getSalary()
	{
		return salary;
	}
	
	@Override
	public String toString()
	{
		return name + " " + number+ " " + position;
	}
}
