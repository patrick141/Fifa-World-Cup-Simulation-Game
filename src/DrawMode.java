/**
 * Controls how tournament groups are determined before play begins.
 *
 * OFFICIAL  uses the real historical draw results for that edition.
 *           Groups are fixed exactly as they happened.
 *
 * SIMULATED runs the DrawEngine with the edition's four pots and
 *           confederation constraints, producing a different valid
 *           grouping each time.
 */
public enum DrawMode {
    OFFICIAL,
    SIMULATED
}
