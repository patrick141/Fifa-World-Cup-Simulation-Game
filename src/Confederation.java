/**
 * The six FIFA confederations that national teams belong to.
 * Used by DrawEngine to enforce group stage draw constraints:
 * no two teams from the same confederation in one group,
 * with UEFA as the exception allowing up to two per group.
 */
public enum Confederation {
    UEFA    ("UEFA",     "Europe"),
    CONMEBOL("CONMEBOL", "South America"),
    CONCACAF("CONCACAF", "North/Central America & Caribbean"),
    CAF     ("CAF",      "Africa"),
    AFC     ("AFC",      "Asia"),
    OFC     ("OFC",      "Oceania");

    public final String code;
    public final String region;

    Confederation(String code, String region) {
        this.code   = code;
        this.region = region;
    }

    @Override
    public String toString() { return code; }
}
