import java.util.Arrays;
import java.util.Scanner;

/**
 * Entry point for the 2018 FIFA World Cup Simulator.
 *
 * Run with the --auto flag to auto-simulate all 64 matches with realistic
 * random scores, or choose manual mode to enter every result yourself.
 *
 *   javac *.java
 *   java Driver           (interactive)
 *   java Driver --auto    (auto-simulation)
 */
public class Driver {

    public static void main(String[] args) {
        printBanner();

        Scanner scanner = new Scanner(System.in);
        boolean autoMode = Arrays.asList(args).contains("--auto");

        if (!autoMode) {
            System.out.print("  Select mode  (M)anual / (A)uto-simulate: ");
            String choice = scanner.nextLine().trim().toLowerCase();
            autoMode = choice.startsWith("a");
        }

        if (autoMode) {
            Match.setStrategy(new AutoResultStrategy());
            System.out.println(ConsoleColors.YELLOW
                    + "\n  AUTO-SIMULATION MODE  — all 64 matches will be generated."
                    + ConsoleColors.RESET);
        } else {
            Match.setStrategy(new ManualResultStrategy(scanner));
            System.out.println(ConsoleColors.CYAN
                    + "\n  MANUAL MODE  — enter each team's goals when prompted."
                    + ConsoleColors.RESET);
        }

        showTeamSpotlight();

        // ── GROUP STAGE ───────────────────────────────────────────────────────
        printSectionHeader("GROUP STAGE");
        Group[] groups = buildGroups();
        for (Group g : groups) {
            g.generateMatches();
            g.setMatches();
            g.arrangeStandings();
            g.printStandings();
        }

        // ── ROUND OF 16 ───────────────────────────────────────────────────────
        printSectionHeader("ROUND OF 16");
        KnockoutMatch[] ro16 = {
            new KnockoutMatch(groups[0].getGroupWinner(),   groups[1].getGroupRunnerUp()),  // A1 v B2
            new KnockoutMatch(groups[2].getGroupWinner(),   groups[3].getGroupRunnerUp()),  // C1 v D2
            new KnockoutMatch(groups[4].getGroupWinner(),   groups[5].getGroupRunnerUp()),  // E1 v F2
            new KnockoutMatch(groups[6].getGroupWinner(),   groups[7].getGroupRunnerUp()),  // G1 v H2
            new KnockoutMatch(groups[1].getGroupWinner(),   groups[0].getGroupRunnerUp()),  // B1 v A2
            new KnockoutMatch(groups[3].getGroupWinner(),   groups[2].getGroupRunnerUp()),  // D1 v C2
            new KnockoutMatch(groups[5].getGroupWinner(),   groups[4].getGroupRunnerUp()),  // F1 v E2
            new KnockoutMatch(groups[7].getGroupWinner(),   groups[6].getGroupRunnerUp()),  // H1 v G2
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
        printSectionHeader("2018 FIFA WORLD CUP FINAL");
        KnockoutMatch finalMatch = new KnockoutMatch(sf1.getWinner(), sf2.getWinner());
        finalMatch.setMatchresult();

        // ── BRACKET SUMMARY ───────────────────────────────────────────────────
        printBracketSummary(ro16, qf, sf1, sf2, thirdPlace, finalMatch);

        // ── CHAMPION PODIUM ───────────────────────────────────────────────────
        printChampionPodium(finalMatch.getWinner(), finalMatch.getLoser(),
                thirdPlace.getWinner(), thirdPlace.getLoser());

        // ── TOURNAMENT STATS ──────────────────────────────────────────────────
        TournamentStats.getInstance().printSummary();

        scanner.close();
    }

    // ── Group setup ───────────────────────────────────────────────────────────

    /**
     * Builds all eight 2018 World Cup groups.
     * Teams are in seeding order (group leader first).
     */
    private static Group[] buildGroups() {
        Group a = group("A", "Russia",    "Saudi Arabia", "Egypt",        "Uruguay");
        Group b = group("B", "Portugal",  "Spain",        "Morocco",      "Iran");
        Group c = group("C", "France",    "Australia",    "Peru",         "Denmark");
        Group d = group("D", "Argentina", "Iceland",      "Croatia",      "Nigeria");
        Group e = group("E", "Brazil",    "Switzerland",  "Costa Rica",   "Serbia");
        Group f = group("F", "Germany",   "Mexico",       "Sweden",       "South Korea");
        Group g = group("G", "Belgium",   "Panama",       "Tunisia",      "England");
        Group h = group("H", "Poland",    "Senegal",      "Colombia",     "Japan");
        return new Group[]{ a, b, c, d, e, f, g, h };
    }

    /** Convenience: creates a 4-team group from country name strings. */
    private static Group group(String name, String... countries) {
        Group g = new Group(name);
        g.AddGroupLeader(new Team(countries[0]));
        for (int i = 1; i < countries.length; i++) g.addTeam(new Team(countries[i]));
        return g;
    }

    // ── Team Spotlight ────────────────────────────────────────────────────────

    /**
     * Demonstrates the Player Builder pattern and Position enum by building
     * France's 2018 World Cup squad before the tournament begins.
     */
    private static void showTeamSpotlight() {
        System.out.println(ConsoleColors.BOLD_CYAN
                + "\n  ── TEAM SPOTLIGHT: France 2018 ──" + ConsoleColors.RESET);
        Team france = new Team("France");
        france.addPlayer(new Player.Builder("Hugo Lloris",       1).age(31).position(Position.GK ).salary(5_600_000).build());
        france.addPlayer(new Player.Builder("Benjamin Pavard",   2).age(22).position(Position.RB ).salary(1_800_000).build());
        france.addPlayer(new Player.Builder("Raphael Varane",    4).age(25).position(Position.CB ).salary(7_200_000).build());
        france.addPlayer(new Player.Builder("Samuel Umtiti",     5).age(24).position(Position.CB ).salary(3_000_000).build());
        france.addPlayer(new Player.Builder("Paul Pogba",        6).age(25).position(Position.CM ).salary(15_000_000).build());
        france.addPlayer(new Player.Builder("Antoine Griezmann", 7).age(27).position(Position.CAM).salary(12_000_000).build());
        france.addPlayer(new Player.Builder("Kylian Mbappe",    10).age(19).position(Position.ST ).salary(5_400_000).build());
        france.addPlayer(new Player.Builder("Olivier Giroud",    9).age(31).position(Position.ST ).salary(3_600_000).build());
        france.addPlayer(new Player.Builder("N'Golo Kante",     13).age(27).position(Position.CDM).salary(9_600_000).build());
        france.addPlayer(new Player.Builder("Lucas Hernandez",  21).age(22).position(Position.LB ).salary(1_800_000).build());
        france.addPlayer(new Player.Builder("Blaise Matuidi",   14).age(31).position(Position.CM ).salary(3_600_000).build());
        france.printRoster();
        System.out.println();
    }

    // ── Display helpers ───────────────────────────────────────────────────────

    private static void printBanner() {
        System.out.println(ConsoleColors.BOLD_YELLOW);
        System.out.println("  ╔══════════════════════════════════════════════════╗");
        System.out.println("  ║       2018 FIFA WORLD CUP SIMULATOR              ║");
        System.out.println("  ║       Russia  •  June 14 – July 15, 2018         ║");
        System.out.println("  ║       32 Teams  •  8 Groups  •  64 Matches       ║");
        System.out.println("  ╚══════════════════════════════════════════════════╝");
        System.out.println(ConsoleColors.RESET);
    }

    private static void printSectionHeader(String title) {
        System.out.println();
        System.out.println(ConsoleColors.BOLD_YELLOW + "  ╔══════════════════════════════════════╗" + ConsoleColors.RESET);
        System.out.printf (ConsoleColors.BOLD_YELLOW + "  ║  %-36s║%n" + ConsoleColors.RESET, title);
        System.out.println(ConsoleColors.BOLD_YELLOW + "  ╚══════════════════════════════════════╝" + ConsoleColors.RESET);
    }

    /**
     * Prints a compact bracket summary showing every knockout result in
     * round order — quick visual review of the entire knockout stage.
     */
    private static void printBracketSummary(KnockoutMatch[] ro16, KnockoutMatch[] qf,
            KnockoutMatch sf1, KnockoutMatch sf2,
            KnockoutMatch thirdPlace, KnockoutMatch finalMatch) {
        System.out.println();
        System.out.println(ConsoleColors.BOLD_WHITE + "  ══════════════  KNOCKOUT BRACKET  ══════════════" + ConsoleColors.RESET);

        System.out.println(ConsoleColors.BOLD_CYAN + "\n  ROUND OF 16:" + ConsoleColors.RESET);
        for (int i = 0; i < ro16.length; i++) {
            printMatchLine(ro16[i], i + 1);
        }

        System.out.println(ConsoleColors.BOLD_CYAN + "\n  QUARTER-FINALS:" + ConsoleColors.RESET);
        for (int i = 0; i < qf.length; i++) {
            printMatchLine(qf[i], i + 1);
        }

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
        String prefix = idx > 0 ? "  [" + idx + "]" : "   ";
        System.out.printf("  %s  %-22s %8s  %-22s   -> %s%n",
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
        System.out.println(ConsoleColors.BOLD_YELLOW + "  ║        2018 FIFA WORLD CUP  RESULTS          ║" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.BOLD_YELLOW + "  ╠══════════════════════════════════════════════╣" + ConsoleColors.RESET);
        System.out.printf (ConsoleColors.BOLD_GREEN  + "  ║  [1st]  CHAMPION  : %-25s║%n" + ConsoleColors.RESET, champion);
        System.out.printf (ConsoleColors.BOLD_WHITE  + "  ║  [2nd]  Runner-Up : %-25s║%n" + ConsoleColors.RESET, runnerUp);
        System.out.printf (ConsoleColors.CYAN        + "  ║  [3rd]  3rd Place : %-25s║%n" + ConsoleColors.RESET, third);
        System.out.printf (ConsoleColors.RESET       + "  ║  [4th]  4th Place : %-25s║%n",                       fourth);
        System.out.println(ConsoleColors.BOLD_YELLOW + "  ╚══════════════════════════════════════════════╝" + ConsoleColors.RESET);
    }
}
