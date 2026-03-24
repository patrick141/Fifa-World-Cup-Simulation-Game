/**
 * 2018 FIFA World Cup — Russia.
 * Full implementation: 32 teams across 8 groups.
 *
 * Group compositions match the official 2018 draw results.
 * Overrides showSpotlight() to demo the Player Builder and Position enum
 * using France's actual 2018 squad before the tournament begins.
 */
public class WC2018 implements TournamentConfig {

    @Override public int              getYear()   { return 2018; }
    @Override public String           getHost()   { return "Russia"; }
    @Override public TournamentFormat getFormat() { return TournamentFormat.THIRTY_TWO_TEAM; }

    @Override
    public Group[] buildGroups() {
        return new Group[]{
            createGroup("A", "Russia",    "Saudi Arabia", "Egypt",      "Uruguay"),
            createGroup("B", "Portugal",  "Spain",        "Morocco",    "Iran"),
            createGroup("C", "France",    "Australia",    "Peru",       "Denmark"),
            createGroup("D", "Argentina", "Iceland",      "Croatia",    "Nigeria"),
            createGroup("E", "Brazil",    "Switzerland",  "Costa Rica", "Serbia"),
            createGroup("F", "Germany",   "Mexico",       "Sweden",     "South Korea"),
            createGroup("G", "Belgium",   "Panama",       "Tunisia",    "England"),
            createGroup("H", "Poland",    "Senegal",      "Colombia",   "Japan"),
        };
    }

    @Override public boolean supportsSimulatedDraw() { return true; }

    /**
     * Builds the four seeding pots used in the 2018 draw ceremony.
     * Teams are assigned their real FIFA confederations so DrawEngine
     * can enforce the no-same-confederation-per-group constraint.
     */
    @Override
    public Pot[] buildPots() {
        Pot p1 = new Pot(1);
        p1.add(new Team("Russia",      Confederation.UEFA));
        p1.add(new Team("Germany",     Confederation.UEFA));
        p1.add(new Team("Brazil",      Confederation.CONMEBOL));
        p1.add(new Team("Portugal",    Confederation.UEFA));
        p1.add(new Team("Argentina",   Confederation.CONMEBOL));
        p1.add(new Team("Belgium",     Confederation.UEFA));
        p1.add(new Team("Poland",      Confederation.UEFA));
        p1.add(new Team("France",      Confederation.UEFA));

        Pot p2 = new Pot(2);
        p2.add(new Team("Spain",       Confederation.UEFA));
        p2.add(new Team("Peru",        Confederation.CONMEBOL));
        p2.add(new Team("Switzerland", Confederation.UEFA));
        p2.add(new Team("England",     Confederation.UEFA));
        p2.add(new Team("Colombia",    Confederation.CONMEBOL));
        p2.add(new Team("Mexico",      Confederation.CONCACAF));
        p2.add(new Team("Uruguay",     Confederation.CONMEBOL));
        p2.add(new Team("Croatia",     Confederation.UEFA));

        Pot p3 = new Pot(3);
        p3.add(new Team("Denmark",     Confederation.UEFA));
        p3.add(new Team("Iceland",     Confederation.UEFA));
        p3.add(new Team("Costa Rica",  Confederation.CONCACAF));
        p3.add(new Team("Sweden",      Confederation.UEFA));
        p3.add(new Team("Tunisia",     Confederation.CAF));
        p3.add(new Team("Egypt",       Confederation.CAF));
        p3.add(new Team("Senegal",     Confederation.CAF));
        p3.add(new Team("Iran",        Confederation.AFC));

        Pot p4 = new Pot(4);
        p4.add(new Team("Serbia",      Confederation.UEFA));
        p4.add(new Team("Nigeria",     Confederation.CAF));
        p4.add(new Team("Australia",   Confederation.AFC));
        p4.add(new Team("Japan",       Confederation.AFC));
        p4.add(new Team("Morocco",     Confederation.CAF));
        p4.add(new Team("Panama",      Confederation.CONCACAF));
        p4.add(new Team("South Korea", Confederation.AFC));
        p4.add(new Team("Saudi Arabia",Confederation.AFC));

        return new Pot[]{ p1, p2, p3, p4 };
    }

    /**
     * Demonstrates the Player Builder pattern and Position enum using
     * France's actual 2018 World Cup squad (11 key players shown).
     */
    @Override
    public void showSpotlight() {
        System.out.println(ConsoleColors.BOLD_CYAN
                + "\n  ── TEAM SPOTLIGHT: France 2018 ──" + ConsoleColors.RESET);
        Team france = new Team("France");
        france.addPlayer(new Player.Builder("Hugo Lloris",       1).age(31).position(Position.GK ).salary(5_600_000).build());
        france.addPlayer(new Player.Builder("Benjamin Pavard",   2).age(22).position(Position.RB ).salary(1_800_000).build());
        france.addPlayer(new Player.Builder("Raphael Varane",    4).age(25).position(Position.CB ).salary(7_200_000).build());
        france.addPlayer(new Player.Builder("Samuel Umtiti",     5).age(24).position(Position.CB ).salary(3_000_000).build());
        france.addPlayer(new Player.Builder("Paul Pogba",        6).age(25).position(Position.CM ).salary(15_000_000).build());
        france.addPlayer(new Player.Builder("Antoine Griezmann", 7).age(27).position(Position.CAM).salary(12_000_000).build());
        france.addPlayer(new Player.Builder("Olivier Giroud",    9).age(31).position(Position.ST ).salary(3_600_000).build());
        france.addPlayer(new Player.Builder("Kylian Mbappe",    10).age(19).position(Position.ST ).salary(5_400_000).build());
        france.addPlayer(new Player.Builder("N'Golo Kante",     13).age(27).position(Position.CDM).salary(9_600_000).build());
        france.addPlayer(new Player.Builder("Blaise Matuidi",   14).age(31).position(Position.CM ).salary(3_600_000).build());
        france.addPlayer(new Player.Builder("Lucas Hernandez",  21).age(22).position(Position.LB ).salary(1_800_000).build());
        france.printRoster();
        System.out.println();
    }
}
