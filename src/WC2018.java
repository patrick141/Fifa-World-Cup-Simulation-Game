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
