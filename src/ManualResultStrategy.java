import java.util.Scanner;

/**
 * Obtains match scores through interactive user input.
 * Validates that all scores are non-negative integers.
 */
public class ManualResultStrategy implements MatchResultStrategy {

    private final Scanner scanner;

    public ManualResultStrategy(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public int[] getResult(Team home, Team away) {
        return new int[]{ readScore(home.getCountry()), readScore(away.getCountry()) };
    }

    private int readScore(String teamName) {
        int score = -1;
        while (score < 0) {
            System.out.print(ConsoleColors.CYAN + "    " + teamName + " goals: " + ConsoleColors.RESET);
            if (scanner.hasNextInt()) {
                score = scanner.nextInt();
                if (score < 0) {
                    System.out.println(ConsoleColors.RED + "    Score cannot be negative. Try again." + ConsoleColors.RESET);
                }
            } else {
                System.out.println(ConsoleColors.RED + "    Please enter a valid integer." + ConsoleColors.RESET);
                scanner.next(); // discard invalid token
            }
        }
        return score;
    }
}
