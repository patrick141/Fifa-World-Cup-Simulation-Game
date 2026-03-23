/**
 * ANSI escape-code constants for terminal colour output.
 * Utility class — cannot be instantiated.
 */
public final class ConsoleColors {
    public static final String RESET       = "\033[0m";
    public static final String BOLD        = "\033[1m";
    public static final String RED         = "\033[0;31m";
    public static final String GREEN       = "\033[0;32m";
    public static final String YELLOW      = "\033[0;33m";
    public static final String CYAN        = "\033[0;36m";
    public static final String BOLD_RED    = "\033[1;31m";
    public static final String BOLD_GREEN  = "\033[1;32m";
    public static final String BOLD_YELLOW = "\033[1;33m";
    public static final String BOLD_CYAN   = "\033[1;36m";
    public static final String BOLD_WHITE  = "\033[1;37m";

    private ConsoleColors() {} // utility class – no instances
}
