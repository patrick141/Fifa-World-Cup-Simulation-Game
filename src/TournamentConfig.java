/**
 * Contract that every World Cup edition must satisfy.
 *
 * WorldCup depends on this interface, not on any concrete year, satisfying
 * the Dependency Inversion Principle. Adding a new edition (e.g. 2030) means
 * writing one new implementation. Nothing else changes.
 *
 * Two default methods are included as shared utilities:
 *   showSpotlight() is an optional hook for year-specific pre-tournament content
 *                   (e.g. WC2018 uses it to print France's roster).
 *   createGroup()   is a helper so every config builds groups the same way
 *                   without needing a base class.
 */
public interface TournamentConfig {

    int             getYear();
    String          getHost();
    TournamentFormat getFormat();
    Group[]         buildGroups();

    /** Returns false for editions that are not yet implemented. */
    default boolean isImplemented() { return true; }

    /** Phase label shown when isImplemented() is false. */
    default String phaseNote() { return ""; }

    /**
     * Optional pre-tournament hook. Override to display year-specific
     * content (team spotlights, fun facts, etc.) before group play begins.
     */
    default void showSpotlight() {}

    /**
     * Shared helper: builds a 4-team group from country name strings.
     * The first country is the group seed (leader).
     *
     * Defined as a default method so all configs share this logic without
     * a separate utility class or base class.
     */
    default Group createGroup(String name, String... countries) {
        Group g = new Group(name);
        g.AddGroupLeader(new Team(countries[0]));
        for (int i = 1; i < countries.length; i++) {
            g.addTeam(new Team(countries[i]));
        }
        return g;
    }

    /**
     * Returns the four seeding pots used for a simulated draw.
     * Returns null by default; override in editions that support it.
     */
    default Pot[] buildPots() { return null; }

    /** Returns true if this edition supports a simulated draw via DrawEngine. */
    default boolean supportsSimulatedDraw() { return false; }
}
