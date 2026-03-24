import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Runs the constrained group-stage draw given four seeding pots.
 *
 * FIFA confederation constraints enforced:
 *   - UEFA: at most 2 teams per group.
 *   - All other confederations: at most 1 team per group.
 *
 * Draw order:
 *   Pot 1 — the host nation (first entry in the pot) is fixed to Group A.
 *            The remaining seven Pot 1 teams are randomly assigned to Groups B-H.
 *            No confederation constraints are applied to Pot 1.
 *
 *   Pots 2-4 — uses the augmenting-path bipartite matching algorithm to find
 *              a valid assignment of all 8 teams to 8 groups in a single pass.
 *              The algorithm is randomized (teams and group exploration order
 *              are both shuffled) so each run produces a different valid draw.
 *
 *              Because the matching is computed before any team is added to a
 *              group, the engine never reaches a dead end mid-assignment and
 *              never needs to print a team twice. The retry wrapper is kept
 *              only as a safety net; it should never trigger for the 2018 data.
 *
 * Call draw() to get back the eight populated Group objects.
 */
public class DrawEngine {

    private static final int MAX_RETRIES   = 100;
    private static final int NUM_GROUPS    = 8;
    private static final String[] GROUP_NAMES = {"A","B","C","D","E","F","G","H"};

    private final Random random = new Random();

    public Group[] draw(Pot[] pots) {
        Group[] groups = createEmptyGroups();

        for (Pot pot : pots) {
            System.out.printf(ConsoleColors.BOLD_WHITE
                    + "%n  Drawing Pot %d...%n" + ConsoleColors.RESET, pot.getNumber());
            System.out.println(ConsoleColors.CYAN + "  " + "─".repeat(52) + ConsoleColors.RESET);

            List<List<Team>> snapshot = snapshotGroups(groups);

            boolean success = false;
            for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
                if (tryAssignPot(pot, groups, pot.getNumber() == 1)) {
                    success = true;
                    break;
                }
                restoreGroups(groups, snapshot);
            }

            if (!success) {
                throw new IllegalStateException(
                        "Draw engine could not place all Pot " + pot.getNumber()
                        + " teams after " + MAX_RETRIES + " attempts.");
            }
        }

        return groups;
    }

    // ── Pot assignment ────────────────────────────────────────────────────────

    private boolean tryAssignPot(Pot pot, Group[] groups, boolean isPotOne) {
        List<Team> teams = new ArrayList<>(pot.getTeams());

        if (isPotOne) {
            // The host nation is always the first entry in Pot 1 (per WC2018.buildPots).
            // Remove it before shuffling so it is always fixed to Group A.
            Team host = teams.remove(0);
            Collections.shuffle(teams, random);

            groups[0].AddGroupLeader(host);
            printAssignment(pot.getNumber(), host, groups[0]);

            for (int i = 0; i < teams.size(); i++) {
                groups[i + 1].AddGroupLeader(teams.get(i));
                printAssignment(pot.getNumber(), teams.get(i), groups[i + 1]);
            }
            return true;
        }

        Collections.shuffle(teams, random);

        // For Pots 2-4, compute a valid assignment via bipartite matching BEFORE
        // adding any team to a group. This guarantees every team is printed exactly
        // once and the assignment is always globally valid.
        int[] teamOf = new int[NUM_GROUPS]; // teamOf[groupIdx] = index in teams list
        Arrays.fill(teamOf, -1);
        int[] groupOf = new int[teams.size()];
        Arrays.fill(groupOf, -1);

        for (int ti = 0; ti < teams.size(); ti++) {
            boolean[] seen = new boolean[NUM_GROUPS];
            if (!augment(ti, teams, groups, groupOf, teamOf, seen)) {
                return false; // No valid matching; caller will retry with new shuffle.
            }
        }

        // Verify every group received a team.
        for (int j = 0; j < NUM_GROUPS; j++) {
            if (teamOf[j] == -1) return false;
        }

        // Apply the matching and print assignments in group order A-H.
        for (int j = 0; j < NUM_GROUPS; j++) {
            Team team = teams.get(teamOf[j]);
            groups[j].addTeam(team);
            printAssignment(pot.getNumber(), team, groups[j]);
        }
        return true;
    }

    /**
     * Augmenting-path step for team at index ti.
     * Explores compatible groups in a randomized order so each run of the
     * overall draw produces a different valid result.
     *
     * @param ti       index of the team being placed
     * @param teams    the full team list for this pot
     * @param groups   the eight tournament groups
     * @param groupOf  current matching: groupOf[teamIdx] = groupIdx
     * @param teamOf   current matching: teamOf[groupIdx] = teamIdx (-1 = free)
     * @param seen     visited flags for this augmentation call (prevents cycles)
     * @return true if a valid augmenting path was found
     */
    private boolean augment(int ti, List<Team> teams, Group[] groups,
                             int[] groupOf, int[] teamOf, boolean[] seen) {
        // Randomize group exploration order for draw variety.
        Integer[] order = new Integer[NUM_GROUPS];
        for (int k = 0; k < NUM_GROUPS; k++) order[k] = k;
        List<Integer> orderList = Arrays.asList(order);
        Collections.shuffle(orderList, random);

        for (int j : orderList) {
            if (!seen[j] && canPlace(teams.get(ti), groups[j])) {
                seen[j] = true;
                // If group j is free, or we can reroute its current occupant, take it.
                if (teamOf[j] == -1
                        || augment(teamOf[j], teams, groups, groupOf, teamOf, seen)) {
                    groupOf[ti] = j;
                    teamOf[j]   = ti;
                    return true;
                }
            }
        }
        return false;
    }

    // ── Constraint check ──────────────────────────────────────────────────────

    boolean canPlace(Team team, Group group) {
        if (group.size() >= 4) return false;
        Confederation conf = team.getConfederation();
        if (conf == null) return true;

        long sameConf = group.getTeams().stream()
                .filter(t -> t.getConfederation() == conf)
                .count();

        return conf == Confederation.UEFA ? sameConf < 2 : sameConf == 0;
    }

    // ── Snapshot helpers ──────────────────────────────────────────────────────

    private List<List<Team>> snapshotGroups(Group[] groups) {
        List<List<Team>> snapshot = new ArrayList<>();
        for (Group g : groups) snapshot.add(new ArrayList<>(g.getTeams()));
        return snapshot;
    }

    private void restoreGroups(Group[] groups, List<List<Team>> snapshot) {
        for (int i = 0; i < groups.length; i++) {
            groups[i].restoreTeams(snapshot.get(i));
        }
    }

    // ── Utilities ─────────────────────────────────────────────────────────────

    private Group[] createEmptyGroups() {
        Group[] groups = new Group[NUM_GROUPS];
        for (int i = 0; i < NUM_GROUPS; i++) groups[i] = new Group(GROUP_NAMES[i]);
        return groups;
    }

    private void printAssignment(int potNumber, Team team, Group group) {
        String conf = team.getConfederation() != null
                ? team.getConfederation().toString() : "";
        System.out.printf("  [Pot %d]  %-22s %-12s  ->  Group %s%n",
                potNumber,
                team.getCountry(),
                ConsoleColors.YELLOW + conf + ConsoleColors.RESET,
                ConsoleColors.BOLD_GREEN + group.getName() + ConsoleColors.RESET);
    }
}
