package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    /**
     * Adds a hexagon of side length s to a given position in the world.
     * @param world
     * @param p the higher left corner of hexagon.
     * @param s the size of hexagon.
     * @param t
     */
    public static void addHexagon(TETile[][] world, Position p, int s, TETile t) {
        if (s < 2) {
            throw new IllegalArgumentException();
        }

        // draw the upper part of hexagon
        Position start, end;
        start = p;
        for (int i = 0; i < s; i ++) {
            end = computeEndPosition(start, s, i);
            drawRows(start, end, t, world);
            start = computeNextUpperStart(start);
        }

        // draw the lower part of hexagon
        start = new Position(start.width + 1, start.height);
        for (int i = s - 1; i >= 0; i --) {
            end = computeEndPosition(start, s, i);
            drawRows(start, end, t, world);
            start = computeNextLowerStart(start);
        }
    }

    /**
     * Draw one row of hexagon from Position s to Position e.
     * @param s start position.
     * @param e end position.
     */
    private static void drawRows(Position s, Position e, TETile t, TETile[][] world) {
        int startX = s.width;
        int endX = e.width;
        for (int i = startX; i <= endX; i ++) {
            world[i][s.height] = t;
        }
    }

    private static Position computeNextUpperStart(Position p) {
        return new Position(p.width - 1, p.height + 1);
    }
    private static Position computeNextLowerStart(Position p) {
        return new Position(p.width + 1, p.height + 1);
    }

    private static Position computeEndPosition(Position start, int s, int row) {
        return new Position(start.width + (s -1) + row * 2, start.height);
    }

    private static class Position {

        private final int width, height;

        public Position(int x, int y) {
            this.width = x;
            this.height = y;
        }
    }

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        Position p = new Position(10, 10);
        TETile t = Tileset.FLOWER;
        addHexagon(world, p, 2, t);

        // draws the world to the screen
        ter.renderFrame(world);
    }
}
