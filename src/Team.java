import java.util.ArrayList;
import java.util.List;

/**
 * Represents a national team in the tournament.
 *
 * Implements Displayable for a uniform console-output contract and
 * Comparable<Team> so collections of teams can be sorted naturally
 * using standard Java library utilities.
 */
public class Team implements Displayable, Comparable<Team> {

    // ── Fields ────────────────────────────────────────────────────────────────

    private final String country;
    private int points;
    private int goalsScored;
    private int goalsAllowed;
    private int ranking;
    private int wins;
    private int draws;
    private int losses;

    private static final int TEAM_SIZE = 23;
    private final List<Player> roster;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Team(String country, int ranking) {
        this.country = country;
        this.ranking = ranking;
        this.roster  = new ArrayList<>(TEAM_SIZE);
    }

    public Team(String country) {
        this(country, 0);
    }

    // ── Roster management ─────────────────────────────────────────────────────

    public void addPlayer(Player p)    { roster.add(p); }
    public void removePlayer(Player p) { roster.remove(p); }

    public void printRoster() {
        System.out.println(ConsoleColors.BOLD + "  " + country + " Roster:" + ConsoleColors.RESET);
        if (roster.isEmpty()) {
            System.out.println("    (No players added)");
        } else {
            roster.forEach(System.out::println);
        }
    }

    // ── Stats mutators ────────────────────────────────────────────────────────

    /**
     * Records a match result for this team, updating goals, points,
     * and W/D/L record atomically.
     *
     * @param scored  goals this team scored
     * @param allowed goals this team conceded
     */
    public void recordResult(int scored, int allowed) {
        goalsScored  += scored;
        goalsAllowed += allowed;
        if (scored > allowed) {
            points += 3;
            wins++;
        } else if (scored < allowed) {
            losses++;
        } else {
            points += 1;
            draws++;
        }
    }

    // Legacy setters kept so existing calling code compiles without changes.
    public void setPoints(int p)       { points += p; }
    public void setGoalsScored(int g)  { goalsScored  += g; }
    public void setGoalsAllowed(int g) { goalsAllowed += g; }

    // ── Accessors ─────────────────────────────────────────────────────────────

    public String getCountry()       { return country; }
    public int    getRanking()       { return ranking; }
    public void   setRanking(int r)  { ranking = r; }
    public int    getPoints()        { return points; }
    public int    getGoalsScored()   { return goalsScored; }
    public int    getGoalsAllowed()  { return goalsAllowed; }
    public int    getGoalDifference(){ return goalsScored - goalsAllowed; }
    public int    getWins()          { return wins; }
    public int    getDraws()         { return draws; }
    public int    getLosses()        { return losses; }

    // ── Displayable ───────────────────────────────────────────────────────────

    @Override
    public void display() {
        System.out.printf("  %-22s Pts:%-3d GF:%-3d GA:%-3d GD:%+3d  %d-%d-%d%n",
                country, points, goalsScored, goalsAllowed,
                getGoalDifference(), wins, draws, losses);
    }

    // ── Comparable ────────────────────────────────────────────────────────────

    /**
     * Natural ordering follows FIFA tiebreaker rules:
     *   1. Most points  2. Goal difference  3. Goals scored
     * Higher-ranked teams sort first (descending).
     */
    @Override
    public int compareTo(Team other) {
        if (this.points != other.points)
            return Integer.compare(other.points, this.points);
        if (this.getGoalDifference() != other.getGoalDifference())
            return Integer.compare(other.getGoalDifference(), this.getGoalDifference());
        return Integer.compare(other.goalsScored, this.goalsScored);
    }

    // ── Object ────────────────────────────────────────────────────────────────

    @Override
    public String toString() { return country; }
}
