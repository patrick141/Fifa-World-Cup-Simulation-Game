/**
 * Strategy interface for obtaining the score of a match.
 *
 * Concrete implementations:
 *   - ManualResultStrategy  : reads scores from standard input
 *   - AutoResultStrategy    : generates realistic random scores
 *
 * Using the Strategy pattern decouples score-entry logic from Match,
 * making it trivial to swap modes at runtime (e.g. via a CLI flag).
 */
public interface MatchResultStrategy {

    /**
     * Returns the match result as a two-element array {homeGoals, awayGoals}.
     *
     * @param home the home team
     * @param away the away team
     * @return int[]{homeGoals, awayGoals}
     */
    int[] getResult(Team home, Team away);
}
