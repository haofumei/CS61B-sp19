package creatures;

import huglife.Action;
import huglife.Creature;
import huglife.Direction;
import huglife.Occupant;
import static huglife.HugLifeUtils.randomEntry;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Deque;

public class Clorus extends Creature {
    /**
     * a fierce blue-colored predator that enjoys nothing more than snacking on hapless Plips.
     *
     * @author Austion
     */

    /**
     * red color.
     */
    private int r;
    /**
     * green color.
     */
    private int g;
    /**
     * blue color.
     */
    private int b;
    /**
     * possibility of taking a move when ample space available.
     */
    private double moveProbability = 0.5;

    /**
     * creates clorus with energy equal to E.
     */
    public Clorus(double e) {
        super("clorus");
        r = 0;
        g = 0;
        b = 0;
        energy = e;
    }

    /**
     * creates a clorus with energy equal to 1.
     */
    public Clorus() {
        this(.99);
    }
    /**
     * The color() method for Cloruses should always
     * return the color red = 34, green = 0, blue = 231.
     */
    public Color color() {
        r = 34;
        g = 0;
        b = 231;
        return color(r, g, b);
    }

    /**
     * If a Clorus attacks another creature,
     * it should gain that creature’s energy.
     */
    public void attack(Creature c) {
        this.energy += c.energy();
    }

    /**
     * Cloruses should lose 0.03 units of energy on a MOVE action.
     */
    public void move() {
        energy -= 0.03;
    }


    /**
     * Cloruses should lose 0.01 units of energy on a STAY action.
     */
    public void stay() {
        energy -= 0.01;
    }

    /**
     * Like a Plip, when a Clorus replicates, it keeps 50% of its energy.
     * The other 50% goes to its offspring.No energy is lost in the replication process.
     */
    public creatures.Clorus replicate() {
        energy /= 2;
        return new creatures.Clorus(energy);
    }

    /**
     * Cloruses should obey exactly the following behavioral rules:
     *
     * 1.If there are no empty squares, the Clorus will STAY
     * (even if there are Plips nearby they could attack
     * since plip squares do not count as empty squares).
     * 2.Otherwise, if any Plips are seen,
     * the Clorus will ATTACK one of them randomly.
     * 3.Otherwise, if the Clorus has energy greater than or equal to one,
     * it will REPLICATE to a random empty square.
     * 4.Otherwise, the Clorus will MOVE to a random empty square.
     */
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        Deque<Direction> emptyNeighbors = new ArrayDeque<>();
        Deque<Direction> PlipNeighbors = new ArrayDeque<>();
        boolean anyPlips = false;
        for (Direction d : neighbors.keySet()) {
            if (neighbors.get(d).name().equals("empty")) {
                emptyNeighbors.add(d);
            } else if (neighbors.get(d).name().equals("plip")) {
                anyPlips = true;
                PlipNeighbors.add(d);
            }
        }
        //Rule 1
        if (emptyNeighbors.size() == 0) {
            return new Action(Action.ActionType.STAY);
        }
        //Rule 2
        if (anyPlips) {
            return new Action(Action.ActionType.ATTACK, randomEntry(PlipNeighbors));
        }
        //Rule 3
        if (energy >= 1) {
            return new Action(Action.ActionType.REPLICATE, randomEntry(emptyNeighbors));
        }
     return new Action(Action.ActionType.MOVE, randomEntry(emptyNeighbors));
    }
}
