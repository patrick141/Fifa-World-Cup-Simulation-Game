/**
 * 2022 FIFA World Cup — Qatar.
 *
 * Phase 2 — NOT YET IMPLEMENTED.
 *
 * TODO: Add 32-team group compositions from the official 2022 Qatar draw.
 * TODO: Add team spotlights (e.g. Argentina's Messi-era squad).
 */
public class WC2022 implements TournamentConfig {

    @Override public int              getYear()   { return 2022; }
    @Override public String           getHost()   { return "Qatar"; }
    @Override public TournamentFormat getFormat() { return TournamentFormat.THIRTY_TWO_TEAM; }

    @Override
    public boolean isImplemented() { return false; }

    @Override
    public String phaseNote() { return "Phase 2 — coming soon."; }

    /**
     * Not yet implemented.
     * @throws UnsupportedOperationException always
     */
    @Override
    public Group[] buildGroups() {
        throw new UnsupportedOperationException(
                "WC2022 group data has not been added yet. " + phaseNote());
    }
}
