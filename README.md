# Fifa-World-Cup-Simulation-Game

Predict and simulate FIFA World Cup results. Phase 1 covers the full 2018 tournament (32 teams, 8 groups, 64 matches). Phase 2 will add 2022 Qatar teams and Phase 3 will add 2026 USA/Canada/Mexico's expanded 48-team format.

## Run It

```bash
cd src
javac *.java

java Driver          # manual mode — you enter every scoreline
java Driver --auto   # auto-simulate the entire tournament instantly
```

Requires Java 11+. No external dependencies.

## What It Does

The program runs the complete tournament structure:

```
8 Groups (48 matches)  →  Round of 16  →  Quarters  →  Semis  →  Final
```

After every group finishes, standings print with each team's points, goals for/against, goal difference, and W-D-L record. Advancing teams are highlighted. After all knockout rounds, a full bracket summary, champion podium, and tournament statistics print automatically.

## Technical Highlights

**Strategy Pattern** — `MatchResultStrategy` is an interface with two implementations: `ManualResultStrategy` (Scanner-based input with validation) and `AutoResultStrategy` (weighted random). The active strategy is set once at startup and shared across all 64 `Match` instances. Swapping modes is a single method call.

**Builder Pattern** — `Player` uses a static inner `Builder` class for readable, flexible object construction. Optional fields (age, height, position, salary) chain fluently. The France 2018 squad is constructed this way at startup as a live demo.

**Singleton Pattern** — `TournamentStats` maintains one shared stats object across every match in the tournament, accumulating total goals, biggest win margin, and per-team goal tallies without being passed around explicitly.

**Enum** — `Position` (GK, CB, LB, RB, CDM, CM, CAM, LW, RW, ST) replaces raw `String` fields in `Player`, giving compile-time type safety and a clean `toString()` display name.

**Interfaces** — `Displayable` defines a `display()` contract implemented by both `Group` and `Team`. `MatchResultStrategy` defines the score-retrieval contract that decouples input mode from match logic.

**Streams and Comparator chain** — `Group.arrangeStandings()` uses a single stream pipeline with a chained `Comparator` (points → goal difference → goals scored) to sort group standings. This replaced over 100 lines of manual pairwise comparison code.

**Comparable** — `Team implements Comparable<Team>` defines natural ordering by the same FIFA tiebreaker rules, so any standard collection utility can sort teams without a separate comparator.

**Inheritance and Polymorphism** — `KnockoutMatch extends Match`, overriding `setMatchresult()`, `getWinner()`, and `getLoser()` to handle penalty shootouts. The parent's static strategy is reused via `Match.getStrategy()`.

**Input Validation** — `ManualResultStrategy` rejects negative integers and non-numeric input in a loop, giving a clear error message each time rather than throwing.

**ANSI Terminal Colors** — `ConsoleColors` is a non-instantiable utility class of `static final` escape-code constants. Advancing teams print in green, draws in yellow, auto-generated results in a distinct color, and section headers are bolded.

## Sample Output

```
  ╔══════════════════════════════════════════════════╗
  ║       2018 FIFA WORLD CUP SIMULATOR              ║
  ║       Russia  •  June 14 – July 15, 2018         ║
  ║       32 Teams  •  8 Groups  •  64 Matches       ║
  ╚══════════════════════════════════════════════════╝

  GROUP C STANDINGS
  ──────────────────────────────────────────────────────────
  Pos  Team                    Pts   GF   GA    GD  W-D-L
  ──────────────────────────────────────────────────────────
  1    France                    9    8    2    +6  3-0-0   ← advances
  2    Denmark                   4    2    3    -1  1-1-1   ← advances
  3    Australia                 3    3    5    -2  1-0-2
  4    Peru                      1    2    5    -3  0-1-2
  ──────────────────────────────────────────────────────────

  ROUND OF 16:
  [1]  France              3-1  Argentina        -> France
  [2]  Uruguay             2-0  Portugal         -> Uruguay
  ...

  TOURNAMENT STATISTICS
  ──────────────────────────────────────────
    Total Matches Played : 64
    Total Goals Scored   : 183
    Avg Goals / Match    : 2.86
    Biggest Win          : Germany 5-0 Brazil  (margin: 5)

    TOP 5 SCORING TEAMS:
      France                  16 goals
      Germany                 14 goals
      Belgium                 13 goals
```

## Project Structure

```
src/
  Driver.java               entry point, tournament orchestration
  Match.java                group-stage match with Strategy wiring
  KnockoutMatch.java        extends Match, adds penalty shootout
  Group.java                four-team round-robin group
  Team.java                 national team, implements Displayable + Comparable
  Player.java               player with Builder inner class
  Position.java             enum of player positions
  MatchResultStrategy.java  strategy interface
  ManualResultStrategy.java reads scores from stdin with validation
  AutoResultStrategy.java   weighted random score generation
  TournamentStats.java      Singleton accumulating tournament-wide stats
  ConsoleColors.java        ANSI escape code constants
  Displayable.java          display() interface
  Pot.java                  draw-pot utility for future random group generation
```

## Roadmap

Phase 2 — 2022 FIFA World Cup (Qatar): updated rosters, group compositions, and 32-team bracket.

Phase 3 — 2026 FIFA World Cup (USA / Canada / Mexico): expanded 48-team format, 12 groups, new round-of-32 knockout stage.
