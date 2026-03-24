/**
 * A knockout-round match that supports a penalty shootout when teams are level
 * after ninety minutes.
 *
 * Extends Match (inheritance) and overrides setMatchresult(), getWinner(),
 * and getLoser() to handle the extra-time / penalty logic.
 * Points are intentionally not updated in knockout matches. Only group-stage
 * points count toward team records.
 */
public class KnockoutMatch extends Match {

    private int score1; // home goals (regular time)
    private int score2; // away goals (regular time)
    private int pen1;   // home penalty score
    private int pen2;   // away penalty score

    public KnockoutMatch(Team home, Team away) {
        super(home, away);
    }

    // ── Core override ─────────────────────────────────────────────────────────

    @Override
    public void setMatchresult() {
        System.out.printf(ConsoleColors.BOLD + "%n    %-22s vs  %s%n" + ConsoleColors.RESET,
                getHome(), getVisitor());

        int[] scores = Match.getStrategy().getResult(getHome(), getVisitor());
        score1 = scores[0];
        score2 = scores[1];

        if (score1 == score2) {
            System.out.println(ConsoleColors.YELLOW
                    + "    Draw after 90 min — Penalty Shootout!" + ConsoleColors.RESET);
            do {
                int[] pens = Match.getStrategy().getResult(getHome(), getVisitor());
                pen1 = pens[0];
                pen2 = pens[1];
                if (pen1 == pen2)
                    System.out.println(ConsoleColors.RED
                            + "    Penalties cannot be level — reshoot." + ConsoleColors.RESET);
            } while (pen1 == pen2);
        }

        printResult();
        TournamentStats.getInstance().recordMatch(getHome(), score1, getVisitor(), score2);
    }

    // ── Result display ────────────────────────────────────────────────────────

    @Override
    public void printResult() {
        String scoreStr = getHome() + "  " + score1 + " - " + score2 + "  " + getVisitor();
        if (score1 == score2) {
            scoreStr += ConsoleColors.YELLOW + "  (pens: " + pen1 + "-" + pen2 + ")" + ConsoleColors.RESET;
        }
        System.out.println(ConsoleColors.BOLD_GREEN + "    " + scoreStr + ConsoleColors.RESET);
        System.out.println(ConsoleColors.BOLD + "    Winner: " + getWinner().getCountry() + ConsoleColors.RESET);
    }

    // ── Winner / loser resolution ─────────────────────────────────────────────

    @Override
    public Team getWinner() {
        if (score1 > score2) return getHome();
        if (score2 > score1) return getVisitor();
        return pen1 > pen2 ? getHome() : getVisitor();
    }

    @Override
    public Team getLoser() {
        if (score1 < score2) return getHome();
        if (score2 < score1) return getVisitor();
        return pen1 < pen2 ? getHome() : getVisitor();
    }

    // ── Formatted result string (used by bracket display) ────────────────────

    public String getResultString() {
        if (score1 == score2) {
            return score1 + "-" + score2 + " (p:" + pen1 + "-" + pen2 + ")";
        }
        return score1 + "-" + score2;
    }
}
