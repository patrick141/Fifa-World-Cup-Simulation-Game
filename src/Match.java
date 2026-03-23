import java.util.Scanner;

/**
 * Represents a group-stage match between two teams.
 *
 * Uses the Strategy pattern (MatchResultStrategy) to decouple score-entry
 * logic from the match itself — swapping Manual ↔ Auto mode is one call.
 */
public class Match {

    // ── Strategy (shared across all Match instances) ──────────────────────────

    private static MatchResultStrategy strategy =
            new ManualResultStrategy(new Scanner(System.in));

    public static void setStrategy(MatchResultStrategy s) { strategy = s; }

    /** Package-visible so KnockoutMatch can delegate to the same strategy. */
    static MatchResultStrategy getStrategy() { return strategy; }

    // ── Instance state ────────────────────────────────────────────────────────

    private static int matchCount = 0;

    private final Team team1;
    private final Team team2;
    protected int result1;
    protected int result2;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Match(Team team1, Team team2) {
        this.team1   = team1;
        this.team2   = team2;
        this.result1 = 0;
        this.result2 = 0;
        matchCount++;
    }

    // ── Core methods ──────────────────────────────────────────────────────────

    /**
     * Obtains scores via the current strategy, updates team records,
     * prints the result, and logs stats — in one atomic step.
     */
    public void setMatchresult() {
        System.out.println(ConsoleColors.BOLD + "    " + team1 + " vs " + team2 + ConsoleColors.RESET);
        int[] scores = strategy.getResult(team1, team2);
        result1 = scores[0];
        result2 = scores[1];
        setpoints();
        printResult();
        TournamentStats.getInstance().recordMatch(team1, result1, team2, result2);
    }

    /** Awards points and records goals for both teams. */
    public void setpoints() {
        team1.recordResult(result1, result2);
        team2.recordResult(result2, result1);
    }

    public void printResult() {
        String color = (result1 == result2) ? ConsoleColors.YELLOW : ConsoleColors.GREEN;
        System.out.printf(color + "    %s  %d - %d  %s%n" + ConsoleColors.RESET,
                team1, result1, result2, team2);
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    public Team getHome()     { return team1; }
    public Team getVisitor()  { return team2; }
    /** @deprecated Use {@link #getVisitor()} */
    @Deprecated
    public Team getVistor()   { return team2; }

    public Team getWinner() {
        if (result1 > result2) return team1;
        if (result2 > result1) return team2;
        return null; // draw
    }

    public Team getLoser() {
        if (result1 < result2) return team1;
        if (result2 < result1) return team2;
        return null; // draw
    }

    public static int getMatchCount() { return matchCount; }

    // ── Object ────────────────────────────────────────────────────────────────

    @Override
    public String toString() { return team1 + " vs " + team2; }
}
