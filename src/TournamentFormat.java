/**
 * Describes the structural format of a FIFA World Cup edition.
 * Used by WorldCup to determine which bracket to build.
 *
 * 2026 introduced the expanded 48-team format. Modeling this as an enum
 * keeps the distinction explicit and type-safe rather than branching on year.
 */
public enum TournamentFormat {
    THIRTY_TWO_TEAM(32, 8,  "Round of 16"),
    FORTY_EIGHT_TEAM(48, 12, "Round of 32");

    public final int totalTeams;
    public final int totalGroups;
    public final String firstKnockoutRound;

    TournamentFormat(int totalTeams, int totalGroups, String firstKnockoutRound) {
        this.totalTeams         = totalTeams;
        this.totalGroups        = totalGroups;
        this.firstKnockoutRound = firstKnockoutRound;
    }
}
