import java.util.*;

/**
 * Singleton that accumulates statistics across every match in the tournament
 * and renders a formatted summary at the end.
 *
 * Singleton pattern ensures one shared stats object regardless of how many
 * Match instances are created.
 */
public class TournamentStats {

    // ── Singleton ─────────────────────────────────────────────────────────────

    private static TournamentStats instance;

    private TournamentStats() {}

    public static TournamentStats getInstance() {
        if (instance == null) instance = new TournamentStats();
        return instance;
    }

    // ── State ─────────────────────────────────────────────────────────────────

    private int totalGoals    = 0;
    private int totalMatches  = 0;
    private int biggestMargin = 0;
    private String biggestWin = "";

    /** Maps country name → goals scored across all rounds. */
    private final Map<String, Integer> teamGoals = new LinkedHashMap<>();

    // ── Public API ────────────────────────────────────────────────────────────

    public void recordMatch(Team home, int homeGoals, Team away, int awayGoals) {
        totalGoals  += homeGoals + awayGoals;
        totalMatches++;

        teamGoals.merge(home.getCountry(), homeGoals, Integer::sum);
        teamGoals.merge(away.getCountry(), awayGoals, Integer::sum);

        int margin = Math.abs(homeGoals - awayGoals);
        if (margin > biggestMargin) {
            biggestMargin = margin;
            boolean homeWon = homeGoals > awayGoals;
            String winner = homeWon ? home.getCountry() : away.getCountry();
            String loser  = homeWon ? away.getCountry() : home.getCountry();
            int ws = Math.max(homeGoals, awayGoals);
            int ls = Math.min(homeGoals, awayGoals);
            biggestWin = winner + " " + ws + "-" + ls + " " + loser;
        }
    }

    public void printSummary() {
        System.out.println();
        System.out.println(ConsoleColors.BOLD_YELLOW + "  TOURNAMENT STATISTICS" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.YELLOW + "  " + "─".repeat(42) + ConsoleColors.RESET);
        System.out.printf("    Total Matches Played : %d%n", totalMatches);
        System.out.printf("    Total Goals Scored   : %d%n", totalGoals);
        System.out.printf("    Avg Goals / Match    : %.2f%n",
                totalMatches > 0 ? (double) totalGoals / totalMatches : 0);
        if (!biggestWin.isEmpty())
            System.out.printf("    Biggest Win          : %s  (margin: %d)%n", biggestWin, biggestMargin);

        System.out.println();
        System.out.println(ConsoleColors.BOLD_CYAN + "    TOP 5 SCORING TEAMS:" + ConsoleColors.RESET);
        teamGoals.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .forEach(e -> System.out.printf("      %-22s  %2d goals%n", e.getKey(), e.getValue()));
        System.out.println();
    }
}
