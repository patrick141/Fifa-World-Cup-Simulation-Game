import java.util.Arrays;
import java.util.Scanner;

/**
 * Entry point for the FIFA World Cup Simulator.
 *
 * Responsibilities are intentionally narrow: parse arguments, prompt for
 * year and mode, then hand off to WorldCup. All tournament logic lives in
 * WorldCup; all edition-specific data lives in the TournamentConfig impls.
 *
 *   javac *.java
 *   java Driver           (prompts for year and mode)
 *   java Driver --auto    (auto-simulate; still prompts for year)
 */
public class Driver {

    private static final TournamentConfig[] EDITIONS = {
        new WC2018(),
        new WC2022(),
        new WC2026(),
    };

    public static void main(String[] args) {
        printBanner();

        Scanner scanner = new Scanner(System.in);
        boolean autoMode = Arrays.asList(args).contains("--auto");

        TournamentConfig config = selectEdition(scanner);

        if (!config.isImplemented()) {
            System.out.println(ConsoleColors.YELLOW
                    + "\n  " + config.getYear() + " is not available yet. "
                    + config.phaseNote()
                    + ConsoleColors.RESET);
            scanner.close();
            return;
        }

        if (!autoMode) {
            System.out.print("\n  Select mode  (M)anual / (A)uto-simulate: ");
            autoMode = scanner.nextLine().trim().toLowerCase().startsWith("a");
        }

        if (autoMode) {
            System.out.println(ConsoleColors.YELLOW
                    + "  AUTO-SIMULATION MODE — all matches will be generated."
                    + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.CYAN
                    + "  MANUAL MODE — enter each team's goals when prompted."
                    + ConsoleColors.RESET);
        }

        new WorldCup(config, autoMode).run();
        scanner.close();
    }

    private static TournamentConfig selectEdition(Scanner scanner) {
        System.out.println("\n  Select a tournament:");
        for (int i = 0; i < EDITIONS.length; i++) {
            TournamentConfig e = EDITIONS[i];
            String status = e.isImplemented()
                    ? ConsoleColors.BOLD_GREEN  + "available"    + ConsoleColors.RESET
                    : ConsoleColors.YELLOW      + e.phaseNote()  + ConsoleColors.RESET;
            System.out.printf("    [%d]  %d  %-24s  %s%n", i + 1, e.getYear(), e.getHost(), status);
        }

        int choice = -1;
        while (choice < 1 || choice > EDITIONS.length) {
            System.out.print("\n  Enter choice: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                if (choice < 1 || choice > EDITIONS.length)
                    System.out.println(ConsoleColors.RED + "  Invalid selection." + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED + "  Please enter a number." + ConsoleColors.RESET);
                scanner.nextLine();
            }
        }
        return EDITIONS[choice - 1];
    }

    private static void printBanner() {
        System.out.println(ConsoleColors.BOLD_YELLOW);
        System.out.println("  ╔══════════════════════════════════════════════════╗");
        System.out.println("  ║         FIFA WORLD CUP SIMULATOR                 ║");
        System.out.println("  ║         Predict. Simulate. Dominate.             ║");
        System.out.println("  ╚══════════════════════════════════════════════════╝");
        System.out.println(ConsoleColors.RESET);
    }
}
