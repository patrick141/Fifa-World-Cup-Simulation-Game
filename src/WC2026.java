/**
 * 2026 FIFA World Cup — USA / Canada / Mexico.
 *
 * Phase 3 — NOT YET IMPLEMENTED.
 *
 * This edition uses the expanded 48-team format (12 groups of 4,
 * followed by a Round of 32). WorldCup will need to branch on
 * TournamentFormat.FORTY_EIGHT_TEAM to build the correct bracket.
 *
 * TODO: Add 48-team group compositions once the official draw is held.
 * TODO: Implement Round of 32 bracket in WorldCup for FORTY_EIGHT_TEAM format.
 * TODO: Add team spotlights.
 */
public class WC2026 implements TournamentConfig {

    @Override public int              getYear()   { return 2026; }
    @Override public String           getHost()   { return "USA / Canada / Mexico"; }
    @Override public TournamentFormat getFormat() { return TournamentFormat.FORTY_EIGHT_TEAM; }

    @Override
    public boolean isImplemented() { return false; }

    @Override
    public String phaseNote() { return "Phase 3 — coming soon (expanded 48-team format)."; }

    /**
     * Not yet implemented.
     * @throws UnsupportedOperationException always
     */
    @Override
    public Group[] buildGroups() {
        throw new UnsupportedOperationException(
                "WC2026 group data has not been added yet. " + phaseNote());
    }
}
