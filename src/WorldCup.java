/**
 * Facade that orchestrates a complete FIFA World Cup tournament.
 *
 * WorldCup knows nothing about which year it is running. All edition-specific
 * data (teams, groups, format, spotlight) comes from TournamentConfig.
 * This satisfies the Dependency Inversion Principle: high-level policy
 * (run a tournament) depends on an abstraction, not on WC2018 or WC2022.
 *
 * Currently supports TournamentFormat.THIRTY_TWO_TEAM (16-team knockout).
 * Support for FORTY_EIGHT_TEAM (Round of 32) is marked TODO for Phase 3.
 */
public class WorldCup {

    private final TournamentConfig config;

    public WorldCup(TournamentConfig config, boolean autoMode) {
        this.config = config;
        Match.setStrategy(autoMode
                ? new AutoResultStrategy()
                : new ManualResultStrategy(new java.util.Scanner(System.in)));
    }

    public void run() {
        printTournamentHeader();
        config.showSpotlight();

        // ── GROUP STAGE ───────────────────────────────────────────────────────
        printSectionHeader("GROUP STAGE");
        Group[] groups = config.buildGroups();
        for (Group g : groups) {
            g.generateMatches();
            g.setMatches();
            g.arrangeStandings();
            g.printStandings();
        }

        // ── ROUND OF 16 ───────────────────────────────────────────────────────
        // TODO Phase 3: branch on config.getFormat() == FORTY_EIGHT_TEAM to build
        //               a Round of 32 instead before this point.
        printSectionHeader("ROUND OF 16");
        KnockoutMatch[] ro16 = {
            new KnockoutMatch(groups[0].getGroupWinner(),   groups[1].getGroupRunnerUp()),
            new KnockoutMatch(groups[2].getGroupWinner(),   groups[3].getGroupRunnerUp()),
            new KnockoutMatch(groups[4].getGroupWinner(),   groups[5].getGroupRunnerUp()),
            new KnockoutMatch(groups[6].getGroupWinner(),   groups[7].getGroupRunnerUp()),
            new KnockoutMatch(groups[1].getGroupWinner(),   groups[0].getGroupRunnerUp()),
            new KnockoutMatch(groups[3].getGroupWinner(),   groups[2].getGroupRunnerUp()),
            new KnockoutMatch(groups[5].getGroupWinner(),   groups[4].getGroupRunnerUp()),
            new KnockoutMatch(groups[7].getGroupWinner(),   groups[6].getGroupRunnerUp()),
        };
        for (KnockoutMatch m : ro16) m.setMatchresult();

        // ── QUARTER-FINALS ────────────────────────────────────────────────────
        printSectionHeader("QUARTER-FINALS");
        KnockoutMatch[] qf = {
            new KnockoutMatch(ro16[0].getWinner(), ro16[1].getWinner()),
            new KnockoutMatch(ro16[2].getWinner(), ro16[3].getWinner()),
            new KnockoutMatch(ro16[4].getWinner(), ro16[5].getWinner()),
            new KnockoutMatch(ro16[6].getWinner(), ro16[7].getWinner()),
        };
        for (KnockoutMatch m : qf) m.setMatchresult();

        // ── SEMI-FINALS ───────────────────────────────────────────────────────
        printSectionHeader("SEMI-FINALS");
        KnockoutMatch sf1 = new KnockoutMatch(qf[0].getWinner(), qf[1].getWinner());
        KnockoutMatch sf2 = new KnockoutMatch(qf[2].getWinner(), qf[3].getWinner());
        sf1.setMatchresult();
        sf2.setMatchresult();

        // ── THIRD PLACE ───────────────────────────────────────────────────────
        printSectionHeader("THIRD PLACE MATCH");
        KnockoutMatch thirdPlace = new KnockoutMatch(sf1.getLoser(), sf2.getLoser());
        thirdPlace.setMatchresult();

        // ── FINAL ─────────────────────────────────────────────────────────────
        printSectionHeader(config.getYear() + " FIFA WORLD CUP FINAL");
        KnockoutMatch finalMatch = new KnockoutMatch(sf1.getWinner(), sf2.getWinner());
        finalMatch.setMatchresult();

        // ── RESULTS ───────────────────────────────────────────────────────────
        printBracketSummary(ro16, qf, sf1, sf2, thirdPlace, finalMatch);
        printChampionPodium(finalMatch.getWinner(), finalMatch.getLoser(),
                thirdPlace.getWinner(), thirdPlace.getLoser());
        TournamentStats.getInstance().printSummary();
    }

    // ── Display helpers ───────────────────────────────────────────────────────

    private void printTournamentHeader() {
        String year = String.valueOf(config.getYear());
        String host = config.getHost();
        System.out.println();
        System.out.println(ConsoleColors.BOLD_YELLOW + "  ╔══════════════════════════════════════════════════╗" + ConsoleColors.RESET);
        System.out.printf (ConsoleColors.BOLD_YELLOW + "  ║   %s FIFA WORLD CUP  %-24s║%n"              + ConsoleColors.RESET, year, "");
        System.out.printf (ConsoleColors.BOLD_YELLOW + "  ║   Host: %-41s║%n"                           + ConsoleColors.RESET, host);
        System.out.printf (ConsoleColors.BOLD_YELLOW + "  ║   %d Teams  •  %d Groups  •  %s%-14s║%n"   + ConsoleColors.RESET,
                config.getFormat().totalTeams, config.getFormat().totalGroups,
                config.getFormat().firstKnockoutRound, "");
        System.out.println(ConsoleColors.BOLD_YELLOW + "  ╚══════════════════════════════════════════════════╝" + ConsoleColors.RESET);
    }

    private static void printSectionHeader(String title) {
        System.out.println();
        System.out.println(ConsoleColors.BOLD_YELLOW + "  ╔══════════════════════════════════════╗" + ConsoleColors.RESET);
        System.out.printf (ConsoleColors.BOLD_YELLOW + "  ║  %-36s║%n"                             + ConsoleColors.RESET, title);
        System.out.println(ConsoleColors.BOLD_YELLOW + "  ╚══════════════════════════════════════╝" + ConsoleColors.RESET);
    }

    private static void printBracketSummary(KnockoutMatch[] ro16, KnockoutMatch[] qf,
            KnockoutMatch sf1, KnockoutMatch sf2,
            KnockoutMatch thirdPlace, KnockoutMatch finalMatch) {
        System.out.println();
        System.out.println(ConsoleColors.BOLD_WHITE + "  ══════════════  KNOCKOUT BRACKET  ══════════════" + ConsoleColors.RESET);

        System.out.println(ConsoleColors.BOLD_CYAN + "\n  ROUND OF 16:" + ConsoleColors.RESET);
        for (int i = 0; i < ro16.length; i++) printMatchLine(ro16[i], i + 1);

        System.out.println(ConsoleColors.BOLD_CYAN + "\n  QUARTER-FINALS:" + ConsoleColors.RESET);
        for (int i = 0; i < qf.length; i++) printMatchLine(qf[i], i + 1);

        System.out.println(ConsoleColors.BOLD_CYAN + "\n  SEMI-FINALS:" + ConsoleColors.RESET);
        printMatchLine(sf1, 1);
        printMatchLine(sf2, 2);

        System.out.println(ConsoleColors.BOLD_CYAN + "\n  THIRD PLACE:" + ConsoleColors.RESET);
        printMatchLine(thirdPlace, 0);

        System.out.println(ConsoleColors.BOLD_YELLOW + "\n  FINAL:" + ConsoleColors.RESET);
        printMatchLine(finalMatch, 0);

        System.out.println();
        System.out.println(ConsoleColors.BOLD_WHITE + "  ═════════════════════════════════════════════════" + ConsoleColors.RESET);
    }

    private static void printMatchLine(KnockoutMatch m, int idx) {
        String prefix = idx > 0 ? "[" + idx + "]" : "   ";
        System.out.printf("  %-4s %-22s %10s  %-22s  ->  %s%n",
                prefix,
                m.getHome().getCountry(),
                m.getResultString(),
                m.getVisitor().getCountry(),
                ConsoleColors.BOLD_GREEN + m.getWinner().getCountry() + ConsoleColors.RESET);
    }

    private static void printChampionPodium(Team champion, Team runnerUp,
            Team third, Team fourth) {
        System.out.println();
        System.out.println(ConsoleColors.BOLD_YELLOW + "  ╔══════════════════════════════════════════════╗" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.BOLD_YELLOW + "  ║          FINAL STANDINGS                     ║" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.BOLD_YELLOW + "  ╠══════════════════════════════════════════════╣" + ConsoleColors.RESET);
        System.out.printf (ConsoleColors.BOLD_GREEN  + "  ║  [1st]  CHAMPION  : %-25s║%n" + ConsoleColors.RESET, champion);
        System.out.printf (ConsoleColors.BOLD_WHITE  + "  ║  [2nd]  Runner-Up : %-25s║%n" + ConsoleColors.RESET, runnerUp);
        System.out.printf (ConsoleColors.CYAN        + "  ║  [3rd]  3rd Place : %-25s║%n" + ConsoleColors.RESET, third);
        System.out.printf (ConsoleColors.RESET       + "  ║  [4th]  4th Place : %-25s║%n",                       fourth);
        System.out.println(ConsoleColors.BOLD_YELLOW + "  ╚══════════════════════════════════════════════╝" + ConsoleColors.RESET);
    }
}
