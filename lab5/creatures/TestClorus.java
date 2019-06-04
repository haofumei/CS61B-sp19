package creatures;

import huglife.*;
import org.junit.Test;

import java.awt.*;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestClorus {

    @Test
    public void testBasics() {
        Clorus p = new Clorus(1);
        assertEquals(1, p.energy(), 0.01);
        assertEquals(new Color(34, 0, 231), p.color());
        p.move();
        assertEquals(0.97, p.energy(), 0.01);
        p.move();
        assertEquals(0.94, p.energy(), 0.01);
        p.stay();
        assertEquals(0.93, p.energy(), 0.01);
        p.stay();
        assertEquals(0.92, p.energy(), 0.01);
    }

    @Test
    public void testReplicate() {
        Clorus p = new Clorus(2);
        Clorus baby = p.replicate();
        assertEquals(1, baby.energy(), 0.01);
        assertEquals(1, p.energy(), 0.01);
        assertNotEquals(p, baby);
    }

    @Test
    public void testChoose() {

        // No empty adjacent spaces; stay.
        Clorus p = new Clorus(0.5);
        HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>();
        surrounded.put(Direction.TOP, new Plip());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        Action actual = p.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);


        // see Plip; attack towards an empty space.
        p = new Clorus(1.2);
        HashMap<Direction, Occupant> attackEmpty = new HashMap<Direction, Occupant>();
        attackEmpty.put(Direction.TOP, new Plip());
        attackEmpty.put(Direction.BOTTOM, new Empty());
        attackEmpty.put(Direction.LEFT, new Impassible());
        attackEmpty.put(Direction.RIGHT, new Impassible());

        actual = p.chooseAction(attackEmpty);
        expected = new Action(Action.ActionType.ATTACK, Direction.TOP);

        assertEquals(expected, actual);


        // Energy >= 1; replicate towards an empty space.
        p = new Clorus(1.2);
        HashMap<Direction, Occupant> allEmpty = new HashMap<Direction, Occupant>();
        allEmpty.put(Direction.TOP, new Empty());
        allEmpty.put(Direction.BOTTOM, new Empty());
        allEmpty.put(Direction.LEFT, new Empty());
        allEmpty.put(Direction.RIGHT, new Empty());

        actual = p.chooseAction(allEmpty);
        Action unexpected = new Action(Action.ActionType.STAY);

        assertNotEquals(unexpected, actual);


        // Energy < 1; move.
        p = new Clorus(.99);

        actual = p.chooseAction(allEmpty);
        unexpected = new Action(Action.ActionType.STAY);

        assertNotEquals(unexpected, actual);




    }
}
