/**
 * Enumeration of standard soccer positions.
 * Replaces raw String fields in Player for compile-time type safety.
 */
public enum Position {
    GK("Goalkeeper"),
    CB("Centre-Back"),
    LB("Left-Back"),
    RB("Right-Back"),
    CDM("Defensive Mid"),
    CM("Central Mid"),
    CAM("Attacking Mid"),
    LW("Left Wing"),
    RW("Right Wing"),
    ST("Striker");

    private final String displayName;

    Position(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
