package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Random;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        // initialize tiles
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                finalWorldFrame[x][y] = Tileset.NOTHING;
            }
        }

        Map<Integer,Room> rooms = new HashMap<>();
        ter.initialize(WIDTH, HEIGHT);
        long seed = dealWithInput(input);
        Random random = new Random(seed);
        addRooms(finalWorldFrame, random, rooms);
        connectRooms(rooms,finalWorldFrame);
        return finalWorldFrame;
    }

    /**
     *  Add one single room in the world.
     *  the outer tiles are wall and the inner tiles are hallway.
     * @param world
     * @param p the lower left corner tile of room.
     * @param roomWidth must be larger than 3.
     * @param roomHeight must be larger than 3.
     */
    private void addOneRoom(TETile[][] world, Position p, int roomWidth, int roomHeight) {
        // make all tiles become floor.
        for (int w = 0; w < roomWidth; w ++) {
            for (int h = 0; h < roomHeight; h ++) {
                world[p.getX() + w][p.getY() + h] = Tileset.FLOOR;
            }
        }
        // make outer tiles become wall.
        for (int w = 0; w < roomWidth; w ++) {
            world[p.getX() + w][p.getY()] = Tileset.WALL;
            world[p.getX() + w][p.getY() + roomHeight - 1] = Tileset.WALL;
        }
        for (int h = 0; h < roomHeight; h ++) {
            world[p.getX()][p.getY() + h] = Tileset.WALL;
            world[p.getX() + roomWidth - 1][p.getY() + h] = Tileset.WALL;
        }
    }

    /**
     * Enlarge room with width and height.
     * @param world
     * @param room
     * @param width the width that needed to be added.
     * @param height the height that needed to be added.
     */
    private void enlargeRoom(TETile[][] world, Room room, int width, int height) {
        // enlarge width
        if (height == 0) {
            int startX = room.getRightUpperPosition().getX() + 1;
            int startY = room.getLeftLowerPosition().getY();
            int endY = room.getRightUpperPosition().getY();
            for (int x = startX; x < startX + width; x++) {
                for (int y = startY; y <= endY; y++) {
                    world[x][y] = Tileset.WALL;
                }
            }
            for (int x = startX - 1; x < startX + width - 1; x++) {
                for (int y = startY + 1; y <= endY - 1; y++) {
                    world[x][y] = Tileset.FLOOR;
                }
            }
        }
        // enlarge height
        if (width == 0) {
            int startY = room.getRightUpperPosition().getY() + 1;
            int startX = room.getLeftLowerPosition().getX();
            int endX = room.getRightUpperPosition().getX();
            for (int x = startX; x <= endX; x ++) {
                for (int y = startY; y < startY + height; y ++) {
                    world[x][y] = Tileset.WALL;
                }
            }
            for (int x = startX + 1; x <= endX - 1; x ++) {
                for (int y = startY - 1; y < startY + height - 1; y ++) {
                    world[x][y] = Tileset.FLOOR;
                }
            }
        }

    }

    /**
     * Add rooms randomly in world. Every room's width and height are random, but are larger than 3.
     * Divide world into 50 pieces. Every piece may and only has one room.
     * @param world
     * @param random pseudorandom is guaranteed to output the same random numbers every time.
     */
    private void addRooms(TETile[][] world, Random random, Map<Integer, Room> rooms) {
        int nw = WIDTH / 10; //divide width into 10 piece.
        int nh = HEIGHT / 5; //divide height into 5 piece.
        int roomNum = -1;
        for (int w = 0; w < WIDTH; w += nw) {
            for (int h = 0; h < HEIGHT; h += nh) {
                roomNum += 1;
                boolean add = random.nextBoolean();
                if (add) {
                    int randomX = random.nextInt(nw - 2);
                    int randomY = random.nextInt(nh - 2);
                    int x = randomX + w;
                    int y = randomY + h;
                    Position p = new Position(x, y);
                    int width = random.nextInt(nw - randomX - 2) + 3; // every width must be over 3
                    int height = random.nextInt(nh - randomY - 2) + 3; // every height must be over 3
                    addOneRoom(world, p, width, height);
                    rooms.put(roomNum, new Room(p, width, height));
                }
            }
        }
    }

    /**
     * Connect the rooms in the Maproom with hallway.
     * @param rooms
     * @param world
     */
    private void connectRooms(Map<Integer, Room> rooms, TETile[][] world){
        Set<Integer> roomNum = rooms.keySet();
        for (int num : roomNum) {
            int upperRoomNum = getUpperRoom(num, roomNum);
            if (upperRoomNum > 0) {
                Room upperRoom = rooms.get(upperRoomNum);
                connectUpperRooms(rooms.get(num), upperRoom, world);
            }

            int rightRoomNum = getRightRoom(num, roomNum);
            if (rightRoomNum > 0) {
                Room rightRoom = rooms.get(rightRoomNum);
                connectRightRooms(rooms.get(num), rightRoom, world);
            }
        }
    }

    /**
     * Get the upper room of No.num room.
     * @param num the No. of the room which is needed to find its upper room.
     * @param roomNum the set of number of rooms.
     * @return the upper room's number.
     */
    private int getUpperRoom(int num, Set<Integer> roomNum) {
        int upperNum = num + 1;
        while (upperNum % 5 != 0) {
            if (roomNum.contains(upperNum)) {
                return upperNum;
            }
            upperNum += 1;
        }
        return -1;
    }

    /**
     * Get the right room of No.num room.
     * @param num the No. of the room which is needed to find its right room.
     * @param roomNum the set of number of rooms.
     * @return the right room's number.
     */
    private int getRightRoom(int num, Set<Integer> roomNum) {
        int rightNum = num + 5;
        while (rightNum <= 49) {
            if (roomNum.contains(rightNum)) {
                return rightNum;
            }
            rightNum += 5;
        }
        return -1;
    }

    /**
     * Connect upper room with hallway.
     * @param room
     * @param upperRoom
     * @param world
     */
    private void connectUpperRooms(Room room, Room upperRoom, TETile[][] world) {
        int lackWidth = canBuildUpperHallway(room, upperRoom);
        Position leftLower, rightUpper;
        if (lackWidth > 0) { // upper room is on the left
            // if upper room is too small, enlarge its width
            if (lackWidth != 34) {
                enlargeRoom(world, upperRoom, lackWidth, 0);
                upperRoom.enlargeRoom(lackWidth, 0);
            }
            leftLower = new Position(room.getLeftLowerPosition().getX(),
                    room.getRightUpperPosition().getY());
            rightUpper = new Position(room.getLeftLowerPosition().getX() + 2,
                    upperRoom.getLeftLowerPosition().getY());
        } else { // upper room is on the right
            // if room is too small ,enlarge its width
            if (lackWidth != -34) {
                enlargeRoom(world, room, -lackWidth, 0);
                room.enlargeRoom(-lackWidth, 0);
            }
            leftLower = new Position(upperRoom.getLeftLowerPosition().getX(),
                    room.getRightUpperPosition().getY());
            rightUpper = new Position(upperRoom.getLeftLowerPosition().getX() + 2,
                    upperRoom.getLeftLowerPosition().getY());
        }
        buildUpperHallway(leftLower, rightUpper, world);
    }

    /**
     * Connect the right room with hallway.
     * @param room
     * @param rightRoom
     * @param world
     */
    private void connectRightRooms(Room room, Room rightRoom, TETile[][] world) {
        int lackHeight = canBuildRightHallway(room, rightRoom);
        Position leftLower, rightUpper;
        if (lackHeight > 0) { // room is on the top
            // if right room is too small, enlarge its height
            if (lackHeight != 34) {
                enlargeRoom(world, rightRoom, 0, lackHeight);
                rightRoom.enlargeRoom(0, lackHeight);
            }
            leftLower = new Position(room.getRightUpperPosition().getX(),
                    room.getLeftLowerPosition().getY());
            rightUpper = new Position(rightRoom.getLeftLowerPosition().getX(),
                    room.getLeftLowerPosition().getY() + 2);
        } else { // upper room is on the right
            // if room is too small ,enlarge its width
            if (lackHeight != -34) {
                enlargeRoom(world, room, 0, -lackHeight);
                room.enlargeRoom(0, -lackHeight);
            }
            leftLower = new Position(room.getRightUpperPosition().getX(),
                    rightRoom.getLeftLowerPosition().getY());
            rightUpper = new Position(rightRoom.getLeftLowerPosition().getX(),
                    rightRoom.getLeftLowerPosition().getY() + 2);
        }
        buildRightHallway(leftLower, rightUpper, world);
    }

    /**
     * Build an hallway between room and upper room,
     * the hallway must be 3 width.
     * @param leftLower the left lower position of hallway.
     * @param rightUpper the right upper position of hallway.
     * @param world
     */
    private void buildUpperHallway(Position leftLower, Position rightUpper, TETile[][] world) {
        for (int y = leftLower.getY() + 1; y < rightUpper.getY(); y ++) {
            world[leftLower.getX()][y] = Tileset.WALL;
        }
        for (int y = leftLower.getY() + 1; y < rightUpper.getY(); y ++) {
            world[leftLower.getX() + 2][y] = Tileset.WALL;
        }
        for (int y = leftLower.getY(); y <= rightUpper.getY(); y ++) {
            world[leftLower.getX() + 1][y] = Tileset.FLOOR;
        }
    }

    /**
     * Build an hallway between room and right room ,
     * the hallway must be 3 height.
     * @param leftLower the left lower position of hallway.
     * @param rightUpper the right upper position of hallway.
     * @param world
     */
    private void buildRightHallway(Position leftLower, Position rightUpper, TETile[][] world) {
        for (int x = leftLower.getX() + 1; x < rightUpper.getX(); x ++) {
            world[x][leftLower.getY()] = Tileset.WALL;
        }
        for (int x = leftLower.getX() + 1; x < rightUpper.getX(); x ++) {
            world[x][leftLower.getY() + 2] = Tileset.WALL;
        }
        for (int x = leftLower.getX(); x <= rightUpper.getX(); x ++) {
            world[x][leftLower.getY() + 1] = Tileset.FLOOR;
        }
    }

    /**
     * Return an integer if can build upper hallway.
     * return integer is positive if upper room is on the left,
     * return 34 if upper room is enough to build hallway,
     * or return an integer which indices that upper room is needed to enlarge its width.
     * Return integer is negative if upper room is on the right,
     * return -34 if upper room is enough to build hallway,
     * or return an negative integer which indices that room is needed to enlarge its width.
     * @param room
     * @param upperRoom
     * @return
     */
    private int canBuildUpperHallway(Room room, Room upperRoom) {
        int lackWidth ;
        // upper room is on the left
        if (room.getLeftLowerPosition().getX() >= upperRoom.getLeftLowerPosition().getX()) {
            int reduceWidth = 1 + upperRoom.getRightUpperPosition().getX() - room.getLeftLowerPosition().getX();
            if (reduceWidth < 3) {
                lackWidth = 3 - reduceWidth;
            } else {
                lackWidth = 34;
            }
        } else { // upper room is on th right
            int reduceWidth =1 + room.getRightUpperPosition().getX() - upperRoom.getLeftLowerPosition().getX();
            if (reduceWidth < 3) {
                lackWidth = reduceWidth - 3;
            } else {
                lackWidth = -34;
            }
        }
        return lackWidth;
    }

    /**
     * Return an integer if can build right hallway.
     * return integer is positive if room is on the top,
     * return 34 if right room is enough to build hallway,
     * or return an integer which indices that right room is needed to enlarge its height.
     * Return integer is negative if right room is on the top,
     * return -34 if room is enough to build hallway,
     * or return an negative integer which indices that room is needed to enlarge its height.
     * @param room
     * @param rightRoom
     * @return
     */
    public int canBuildRightHallway(Room room, Room rightRoom) {
        int lackHeight ;
        // room is on the top
        if (room.getLeftLowerPosition().getY() >= rightRoom.getLeftLowerPosition().getY()) {
            // make lackHeight be positive
            int reduceHeight = 1 + rightRoom.getRightUpperPosition().getY() - room.getLeftLowerPosition().getY();
            if (reduceHeight < 3) {
                lackHeight = 3 - reduceHeight;
            } else {
                lackHeight = 34;
            }
        } else { // upper room is on the top
            // make lackHeight be negative
            int reduceHeight = 1 + room.getRightUpperPosition().getY() - rightRoom.getLeftLowerPosition().getY();
            if (reduceHeight < 3) {
                lackHeight = reduceHeight - 3;
            } else {
                lackHeight = -34;
            }
        }
        return lackHeight;
    }

    /**
     * Deal with the input.
     * if it includes 'n', then open a new game with seed which is ended with "s".
     * if it includes 'l', then load the past game.
     * if it includes 'q;, quit the game and save.
     * input string can be upper or lower case and be able to accept either keypress
     * @param input
     * @return
     */
    private long dealWithInput(String input) {
        String lowerInput = input.toLowerCase();
        for (int i = 0; i < lowerInput.length(); i ++) {
            switch (lowerInput.charAt(i)) {
                case 'n' : return getSeed(lowerInput.substring(i + 1));
                case 'l' : return 1;
                case 'q' : return 0;
                default : continue;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Collect the integer in restInput and ended with 's'.
     * @param restInput
     * @return
     */
    private long getSeed(String restInput) {
        StringBuilder seedBuilder = new StringBuilder();
        for (int i = 0; i < restInput.length(); i ++) {
            char ch = restInput.charAt(i);
            if (Character.isDigit(ch)) {
                seedBuilder.append(ch);
            } else if (ch == 's') {
                BigInteger seed = new BigInteger(seedBuilder.toString());
                BigInteger max = new BigInteger("9223372036854775807");
                int cmp = seed.compareTo(max);
                if (cmp > 0) {
                    throw new IllegalArgumentException("input is too large");
                } else {
                    return seed.longValue();
                }
            }
        }
        throw new IllegalArgumentException("input must include 's'");
    }


    private class Position {

        private int width, height;

        public Position(int x, int y) {
            this.width = x;
            this.height = y;
        }

        public int getX() {
            return this.width;
        }

        public int getY() {
            return this.height;
        }
    }

    private class Room {

        private Position leftLower, rightUpper;
        private int width, height;

        public Room(Position p, int width, int height) {
            this.leftLower = p;
            this.width = width;
            this.height = height;
            this.rightUpper = new Position(p.getX() + width - 1, p.getY() + height - 1);
        }

        public Position getLeftLowerPosition() {
            return this.leftLower;
        }

        public Position getRightUpperPosition() {
            return this.rightUpper;
        }


        public void enlargeRoom(int w, int h) {
            this.width = this.width + w;
            this.height = this.height + h;
            this.rightUpper = new Position(leftLower.getX() + width - 1, leftLower.getY() + height - 1);
        }

        public int getWidth() {
            return this.width;
        }

        public int getHeight() {
            return this.height;
        }
    }

}


