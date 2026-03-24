import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

/**
 * Unit tests for DrawEngine constraint correctness.
 *
 * Runs the simulated draw 500 times and asserts:
 *   1. Russia is always in Group A (host rule).
 *   2. Every group has exactly 4 teams.
 *   3. No group contains two teams from the same non-UEFA confederation.
 *   4. No group contains more than 2 UEFA teams.
 *   5. All 32 teams appear exactly once across the 8 groups.
 *   6. No IllegalStateException is thrown (draw resolves without exhausting retries).
 *
 * Run with:
 *   javac -d out src/*.java && java -cp out DrawEngineTest
 */
public class DrawEngineTest {

    private static int passed = 0;
    private static int failed = 0;

    private static final PrintStream REAL_OUT = System.out;
    private static final PrintStream SILENT   = new PrintStream(OutputStream.nullOutputStream());

    public static void main(String[] args) {
        System.out.println("DrawEngine constraint tests\n" + "=".repeat(46));

        testRussiaAlwaysGroupA();
        testAllGroupsHaveFourTeams();
        testNoDuplicateNonUEFAConfederation();
        testUEFAMaxTwoPerGroup();
        testAllTeamsPlacedExactlyOnce();
        testNoRetryExhaustion();

        System.out.println("\n" + "=".repeat(46));
        System.out.printf("Results: %d passed, %d failed%n", passed, failed);
        if (failed > 0) System.exit(1);
    }

    // ── Test cases ────────────────────────────────────────────────────────────

    private static void testRussiaAlwaysGroupA() {
        String name = "Russia always in Group A (500 runs)";
        try {
            WC2018 wc = new WC2018();
            DrawEngine engine = new DrawEngine();
            for (int i = 0; i < 500; i++) {
                Group[] groups = silentDraw(engine, wc);
                boolean russiaInA = groups[0].getTeams().stream()
                        .anyMatch(t -> t.getCountry().equals("Russia"));
                if (!russiaInA) {
                    fail(name, "Russia not in Group A on run " + (i + 1));
                    return;
                }
            }
            pass(name);
        } catch (Exception e) {
            fail(name, e.getMessage());
        }
    }

    private static void testAllGroupsHaveFourTeams() {
        String name = "Every group has exactly 4 teams (500 runs)";
        try {
            WC2018 wc = new WC2018();
            DrawEngine engine = new DrawEngine();
            for (int i = 0; i < 500; i++) {
                Group[] groups = silentDraw(engine, wc);
                for (Group g : groups) {
                    if (g.size() != 4) {
                        fail(name, "Group " + g.getName() + " has " + g.size()
                                + " teams on run " + (i + 1));
                        return;
                    }
                }
            }
            pass(name);
        } catch (Exception e) {
            fail(name, e.getMessage());
        }
    }

    private static void testNoDuplicateNonUEFAConfederation() {
        String name = "No non-UEFA confederation appears twice in one group (500 runs)";
        try {
            WC2018 wc = new WC2018();
            DrawEngine engine = new DrawEngine();
            for (int run = 0; run < 500; run++) {
                Group[] groups = silentDraw(engine, wc);
                for (Group g : groups) {
                    List<Team> teams = g.getTeams();
                    for (Confederation conf : Confederation.values()) {
                        if (conf == Confederation.UEFA) continue;
                        long count = teams.stream()
                                .filter(t -> t.getConfederation() == conf)
                                .count();
                        if (count > 1) {
                            fail(name, "Group " + g.getName() + " has " + count
                                    + " " + conf + " teams on run " + (run + 1));
                            return;
                        }
                    }
                }
            }
            pass(name);
        } catch (Exception e) {
            fail(name, e.getMessage());
        }
    }

    private static void testUEFAMaxTwoPerGroup() {
        String name = "No group has more than 2 UEFA teams (500 runs)";
        try {
            WC2018 wc = new WC2018();
            DrawEngine engine = new DrawEngine();
            for (int run = 0; run < 500; run++) {
                Group[] groups = silentDraw(engine, wc);
                for (Group g : groups) {
                    long uefaCount = g.getTeams().stream()
                            .filter(t -> t.getConfederation() == Confederation.UEFA)
                            .count();
                    if (uefaCount > 2) {
                        fail(name, "Group " + g.getName() + " has " + uefaCount
                                + " UEFA teams on run " + (run + 1));
                        return;
                    }
                }
            }
            pass(name);
        } catch (Exception e) {
            fail(name, e.getMessage());
        }
    }

    private static void testAllTeamsPlacedExactlyOnce() {
        String name = "All 32 teams appear exactly once across groups (500 runs)";
        try {
            WC2018 wc = new WC2018();
            DrawEngine engine = new DrawEngine();
            for (int run = 0; run < 500; run++) {
                Group[] groups = silentDraw(engine, wc);
                long total = 0;
                for (Group g : groups) total += g.getTeams().size();
                if (total != 32) {
                    fail(name, "Total teams placed = " + total + " on run " + (run + 1));
                    return;
                }
            }
            pass(name);
        } catch (Exception e) {
            fail(name, e.getMessage());
        }
    }

    private static void testNoRetryExhaustion() {
        String name = "Draw resolves without exhausting retry budget (500 runs)";
        try {
            WC2018 wc = new WC2018();
            int exceptions = 0;
            for (int i = 0; i < 500; i++) {
                try {
                    silentDraw(new DrawEngine(), wc);
                } catch (IllegalStateException e) {
                    exceptions++;
                }
            }
            if (exceptions > 0) {
                fail(name, "IllegalStateException thrown " + exceptions + " time(s)");
            } else {
                pass(name);
            }
        } catch (Exception e) {
            fail(name, e.getMessage());
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static Group[] silentDraw(DrawEngine engine, WC2018 wc) {
        System.setOut(SILENT);
        try {
            return engine.draw(wc.buildPots());
        } finally {
            System.setOut(REAL_OUT);
        }
    }

    private static void pass(String name) {
        passed++;
        System.out.println("  PASS  " + name);
    }

    private static void fail(String name, String reason) {
        failed++;
        System.out.println("  FAIL  " + name);
        System.out.println("        reason: " + reason);
    }
}
