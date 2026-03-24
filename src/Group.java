import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages one of the eight tournament groups (four teams, six round-robin matches).
 *
 * Implements Displayable for uniform console output.
 * Standing calculation uses Java Streams with a Comparator chain, replacing
 * the original 100-line brute-force logic with five readable lines.
 */
public class Group implements Displayable {

    // ── Fields ────────────────────────────────────────────────────────────────

    private final String      name;
    private Team              groupLeader;
    private final List<Team>  teams   = new ArrayList<>(4);
    private final List<Match> matches = new ArrayList<>(6);
    private List<Team>        sorted;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Group(String name, Team groupLeader) {
        this.name = name;
        this.groupLeader = groupLeader;
        teams.add(groupLeader);
    }

    public Group(String name) {
        this.name = name;
    }

    // ── Team management ───────────────────────────────────────────────────────

    public void AddGroupLeader(Team t) {
        groupLeader = t;
        teams.add(0, t);
    }

    public void addTeam(Team t)  { teams.add(t); }
    public Team getTeam(int i)   { return teams.get(i); }
    public String getName()      { return name; }
    public int size()            { return teams.size(); }

    public List<Team> getTeams() {
        return Collections.unmodifiableList(teams);
    }

    /**
     * Used by DrawEngine when it needs to retry a pot draw.
     * Keeps the Pot 1 seed in place and removes all other teams,
     * resetting the group to its state after Pot 1 was drawn.
     */
    public void resetToPotOneSeed() {
        if (groupLeader != null) {
            teams.clear();
            teams.add(groupLeader);
        }
    }

    /**
     * Restores the team list to a previously captured snapshot.
     * Used by DrawEngine to roll back a failed pot assignment
     * without losing earlier pots' placements.
     */
    public void restoreTeams(List<Team> saved) {
        teams.clear();
        teams.addAll(saved);
    }

    // ── Match generation ──────────────────────────────────────────────────────

    public void generateMatches() {
        if (teams.size() != 4) {
            System.out.println(ConsoleColors.RED
                    + "Group " + name + " needs " + (4 - teams.size()) + " more team(s)."
                    + ConsoleColors.RESET);
            return;
        }
        Team A = teams.get(0), B = teams.get(1), C = teams.get(2), D = teams.get(3);
        matches.add(new Match(A, B));
        matches.add(new Match(C, D));
        matches.add(new Match(A, C));
        matches.add(new Match(D, B));
        matches.add(new Match(B, C));
        matches.add(new Match(D, A));
    }

    // ── Playing matches ───────────────────────────────────────────────────────

    public void setMatches() {
        System.out.println(ConsoleColors.BOLD_CYAN
                + "\n  ┌─────────────────────────────┐"  + ConsoleColors.RESET);
        System.out.printf(ConsoleColors.BOLD_CYAN
                + "  │   GROUP  %s  MATCHES          │%n" + ConsoleColors.RESET, name);
        System.out.println(ConsoleColors.BOLD_CYAN
                + "  └─────────────────────────────┘"  + ConsoleColors.RESET);

        for (int i = 0; i < matches.size(); i++) {
            System.out.printf(ConsoleColors.CYAN + "%n  Match %d/%d:%n" + ConsoleColors.RESET,
                    i + 1, matches.size());
            matches.get(i).setMatchresult();
        }
    }

    // ── Standings ─────────────────────────────────────────────────────────────

    /**
     * Sorts group standings using a Comparator chain that mirrors FIFA rules:
     *   1. Most points
     *   2. Best goal difference
     *   3. Most goals scored
     *   (Head-to-head is the next tie-breaker but is omitted here for brevity)
     *
     * Replaces the original 100-line brute-force comparison block.
     */
    public void arrangeStandings() {
        sorted = teams.stream()
                .sorted(Comparator
                        .comparingInt(Team::getPoints).reversed()
                        .thenComparingInt(Team::getGoalDifference).reversed()
                        .thenComparingInt(Team::getGoalsScored).reversed())
                .collect(Collectors.toList());
    }

    public void printStandings() {
        String divider = "  " + "─".repeat(58);
        System.out.println();
        System.out.printf(ConsoleColors.BOLD_CYAN + "  GROUP %s STANDINGS%n" + ConsoleColors.RESET, name);
        System.out.println(ConsoleColors.CYAN + divider + ConsoleColors.RESET);
        System.out.printf("  %-4s %-22s %4s %4s %4s %5s  %s%n",
                "Pos", "Team", "Pts", "GF", "GA", "GD", "W-D-L");
        System.out.println(ConsoleColors.CYAN + divider + ConsoleColors.RESET);

        for (int i = 0; i < sorted.size(); i++) {
            Team t = sorted.get(i);
            // Highlight the two teams that advance
            String color = (i < 2) ? ConsoleColors.BOLD_GREEN : ConsoleColors.RESET;
            System.out.printf(color + "  %-4d %-22s %4d %4d %4d %+5d  %d-%d-%d%n" + ConsoleColors.RESET,
                    i + 1, t.getCountry(), t.getPoints(),
                    t.getGoalsScored(), t.getGoalsAllowed(), t.getGoalDifference(),
                    t.getWins(), t.getDraws(), t.getLosses());
        }
        System.out.println(ConsoleColors.CYAN + divider + ConsoleColors.RESET);
    }

    public Team getGroupWinner()   { return sorted.get(0); }
    public Team getGroupRunnerUp() { return sorted.get(1); }

    // ── Displayable ───────────────────────────────────────────────────────────

    @Override
    public void display() { printStandings(); }

    // ── Object ────────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return "Group " + name + ": "
                + teams.stream().map(Team::toString).collect(Collectors.joining(", "));
    }
}
