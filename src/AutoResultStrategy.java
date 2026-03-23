import java.util.Random;

/**
 * Generates realistic random match scores automatically.
 *
 * Goal distribution is weighted to reflect real World Cup statistics:
 *   0 goals → 30%,  1 → 30%,  2 → 20%,  3 → 12%,  4 → 6%,  5 → 2%
 */
public class AutoResultStrategy implements MatchResultStrategy {

    private static final int[] WEIGHTS = { 30, 30, 20, 12, 6, 2 };
    private final Random random = new Random();

    @Override
    public int[] getResult(Team home, Team away) {
        int h = weightedGoal();
        int a = weightedGoal();
        System.out.printf(ConsoleColors.YELLOW + "    [AUTO]  %-22s %d - %-2d %s%n" + ConsoleColors.RESET,
                home.getCountry(), h, a, away.getCountry());
        return new int[]{ h, a };
    }

    private int weightedGoal() {
        int roll = random.nextInt(100);
        int cumulative = 0;
        for (int i = 0; i < WEIGHTS.length; i++) {
            cumulative += WEIGHTS[i];
            if (roll < cumulative) return i;
        }
        return 0;
    }
}
