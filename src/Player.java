/**
 * Represents a soccer player on a national team roster.
 *
 * Demonstrates the Builder pattern for readable, flexible object construction
 * and the Position enum for compile-time type safety.
 */
public class Player {

    private final String   name;
    private final int      age;
    private final int      height;   // cm
    private final Position position;
    private final int      number;
    private double salary;
    private int    goals;

    // ── Builder ───────────────────────────────────────────────────────────────

    public static class Builder {
        // Required
        private final String name;
        private final int    number;
        // Optional, sensible defaults
        private int      age      = 0;
        private int      height   = 0;
        private Position position = Position.CM;
        private double   salary   = 0.0;

        public Builder(String name, int number) {
            this.name   = name;
            this.number = number;
        }

        public Builder age(int age)            { this.age      = age;      return this; }
        public Builder height(int height)      { this.height   = height;   return this; }
        public Builder position(Position pos)  { this.position = pos;      return this; }
        public Builder salary(double salary)   { this.salary   = salary;   return this; }

        public Player build() { return new Player(this); }
    }

    private Player(Builder b) {
        this.name     = b.name;
        this.number   = b.number;
        this.age      = b.age;
        this.height   = b.height;
        this.position = b.position;
        this.salary   = b.salary;
        this.goals    = 0;
    }

    // ── Legacy constructor (kept for source compatibility) ────────────────────

    public Player(String name, int age, int height, String positionStr, int number) {
        this.name   = name;
        this.age    = age;
        this.height = height;
        this.number = number;
        this.salary = 0;
        this.goals  = 0;
        Position parsed;
        try {
            parsed = Position.valueOf(positionStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            parsed = Position.CM;
        }
        this.position = parsed;
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    public String   getName()        { return name; }
    public int      getNumber()      { return number; }
    public Position getPosition()    { return position; }
    public int      getAge()         { return age; }
    public int      getHeight()      { return height; }
    public double   getSalary()      { return salary; }
    public int      getPlayerGoals() { return goals; }

    public void setSalary(double ref) { salary = ref; }
    public void addGoals(int n)       { goals += n; }
    /** Legacy alias kept for compatibility. */
    public void addgoals(int n)       { goals += n; }

    // ── Object ────────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return String.format("  #%-2d  %-22s  %-15s  Age: %d",
                number, name, position, age);
    }
}
