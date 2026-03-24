import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * One of the four seeding pots used in the World Cup draw.
 * Each pot holds eight teams ranked by FIFA seeding for that edition.
 * DrawEngine pulls from these pots when simulating the group stage draw.
 */
public class Pot {

    private final int number;
    private final List<Team> teams;

    public Pot(int number) {
        this.number = number;
        this.teams  = new ArrayList<>(8);
    }

    public void add(Team t)        { teams.add(t); }
    public int  getNumber()        { return number; }
    public int  size()             { return teams.size(); }

    public List<Team> getTeams() {
        return Collections.unmodifiableList(teams);
    }

    public void display() {
        System.out.printf(ConsoleColors.BOLD_CYAN + "  POT %d%n" + ConsoleColors.RESET, number);
        System.out.println(ConsoleColors.CYAN + "  " + "─".repeat(38) + ConsoleColors.RESET);
        for (Team t : teams) {
            String conf = t.getConfederation() != null
                    ? t.getConfederation().toString()
                    : "?";
            System.out.printf("  %-22s %s%n", t.getCountry(), conf);
        }
        System.out.println();
    }
}
