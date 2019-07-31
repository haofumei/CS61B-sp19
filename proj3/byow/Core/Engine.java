package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;
import java.math.BigInteger;
import java.util.*;


public class Engine {
    TERenderer ter = new TERenderer();
    Map<Integer,Room> rooms = new HashMap<>();

    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int widthPiece = 10; // divide width by 10 piece
    public static final int heightPiece = 5; // divide height by 5 piece
    private Random random;
    /* for phrase 2. */
    private boolean newGame = false;
    private boolean openBag = false;
    private boolean torch = false;
    private boolean getKey = false;
    private Player player;
    String inputNew;
    Map<TETile, Item> tileToItem = new HashMap<>();

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        setGUI();
        tileToItem();
        String inputN = inputGUI();
        TETile[][] world = interactWithInputString(inputN);
        renderWorld(world);

        inputControl(world);

    }

    /**
     * Rendering the part of world.
     * @param world
     */
    private void renderWorld(TETile[][] world) {
        TETile[][] newWorld;
        if (torch) {
            newWorld = torchWorld(world);
        } else {
            newWorld = makeDark(world);
        }
        ter.renderFrame(newWorld);
    }

    /**
     * Set the beginning GUI which show new game, load and quit.
     */
    private void setGUI() {
        StdDraw.setCanvasSize(WIDTH * 8, HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);

        StdDraw.setPenColor(Color.white);

        Font bigFont = new Font("Monaco", Font.BOLD, 50);
        Font smallFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(bigFont);
        StdDraw.text(WIDTH / 2, HEIGHT - 8, "CS61B: THE GAME");

        StdDraw.setFont(smallFont);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "New Game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, "Load Game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 4, "Quit (Q)");
        StdDraw.show();
    }

    /**
     * use a map to build relation between Tetile and item.
     */
    private void tileToItem() {
        tileToItem.put(Tileset.KEY, Itemset.KEY);
        tileToItem.put(Tileset.TORCH, Itemset.TORCH);
    }

    /**
     * Define interacting choices on the GUI.
     * @return
     */
    private String inputGUI() {
        String input = "";
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            } else {
                char ch =  Character.toLowerCase(StdDraw.nextKeyTyped());

                if (newGame) {
                    input += String.valueOf(ch);
                    drawFrame(input);
                }

                if (ch == 'n') {
                    newGame = true;
                    input += String.valueOf(ch);
                    drawFrame(input);
                }

                if (ch == 's') {
                    newGame = false;
                    return input;
                }

                if (ch == 'l') {
                    return load();
                }

                if (ch == 'q') {
                    System.exit(0);
                }
            }
        }
    }

    /**
     * Draw String s in the center.
     * If newGame is true, it will show other defining string.
     * @param s
     */
    private void drawFrame(String s) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.white);
        // Draw the hint to make a new game
        if (newGame) {
            Font smallFont = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(smallFont);
            StdDraw.text(WIDTH / 2, HEIGHT - 8, "Please enter an integer");
            StdDraw.text(WIDTH / 2, HEIGHT - 10, "Then press S to begin");
        }

        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, s);
        StdDraw.show();
    }

    /**
     * Set the player and item on the world.
     * @param world
     */
    private void setPlayerAndItem(TETile[][] world) {
        Set<Integer> randomNum = new HashSet();

        int wasBornedInRoom = uniqueRandom(randomNum);
        Room room = rooms.get(wasBornedInRoom);
        int x = Math.round((room.rightUpper().x() + room.leftLower().x()) / 2);
        int y = Math.round((room.rightUpper().y() + room.leftLower().y()) / 2);
        Position p = new Position(x, y);
        player = new Player(p, world[p.x()][p.y()]);
        world[x][y] = Tileset.AVATAR;

        int key = uniqueRandom(randomNum);
        set(key, Tileset.KEY, world);

        int lockDoor = uniqueRandom(randomNum);
        set(lockDoor, Tileset.LOCKED_DOOR, world);

        int torch = uniqueRandom(randomNum);
        set(torch, Tileset.TORCH, world);
    }

    /**
     * if load the game, set the action which had happened.
     * @param s
     * @param world
     */
    private void loadPlayAndItem(String s, TETile[][] world) {
        for (int i = 0; i < s.length(); i ++) {
            action(s.charAt(i), world);
        }
    }

    /**
     * set the t on the center of room num.
     * @param num the room number
     * @param t
     * @param world
     * @return
     */
    private Position set(int num, TETile t, TETile[][] world) {
        Room room = rooms.get(num);
        int x = Math.round((room.rightUpper().x() + room.leftLower().x()) / 2);
        int y = Math.round((room.rightUpper().y() + room.leftLower().y()) / 2);
        world[x][y] = t;
        return new Position(x, y);
    }

    /**
     * create unique random int.
     * @param s
     * @return
     */
    private int uniqueRandom(Set s) {
        Object[] roomNum = rooms.keySet().toArray();
        int i = (int)roomNum[random.nextInt(roomNum.length)];
        while (s.contains(i)) {
            i = (int)roomNum[random.nextInt(roomNum.length)];
        }
        s.add(i);
        return i;
    }

    /**
     * define some input choice when game runs.
     * @param world
     * @return
     */
    private String inputControl(TETile[][] world) {
        String input = "";

        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char ch = Character.toLowerCase(StdDraw.nextKeyTyped());
            input += String.valueOf(ch);

            if (ch == 'w' || ch == 's' || ch == 'a' || ch == 'd' || ch == 'z') {
                action(ch, world);
                renderWorld(world);
            }

            if (ch == 'e') {
                if (!openBag) {
                    openBag = true;
                    open();
                } else {
                    openBag = false;
                    renderWorld(world);
                }
            }

            if (ch == 'q') {
                save(inputNew + input);
                System.exit(0);
            }
        }
    }

    /**
     * define some actions.
     * @param ch
     * @param world
     */
    private void action(char ch, TETile[][] world) {
        int x = player.position().x();
        int y = player.position().y();
        switch (ch) {
            case 'w':
                move(x, y + 1, world);
                break;
            case 's':
                move(x, y - 1, world);
                break;
            case 'a':
                move(x - 1, y, world);
                break;
            case 'd':
                move(x + 1, y, world);
                break;
            case 'z':
                pick();
                break;
            default:
                break;
        }
    }

    /**
     * open player's bag.
     */
    private void open() {
        Item[][] bag = new Item[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                bag[x][y] = Itemset.NOTHING;
            }
        }

        StdDraw.clear(Color.BLACK);
        int n = 0;
        int firstX = WIDTH / 4;
        int firstY = HEIGHT / 3 * 2;
        while (n < player.bag().num()) {
            bag[firstX][firstY] = player.bag().item(n);
            bag[firstX][firstY].drawItem(firstX, firstY + 3);
            n += 1;
            firstX += 2;
            firstY += 2;
        }

        StdDraw.show();
    }

    /**
     * use to move player in the game.
     * @param x
     * @param y
     * @param world
     */
    private void move(int x, int y, TETile[][] world) {
        if (canPass(x, y, world)) {
            TETile temp = world[x][y];
            world[x][y] = Tileset.AVATAR;
            world[player.position().x()][player.position().y()] = player.stand();
            player.changeStand(temp);
            player.go(new Position(x, y));
        }
    }

    /**
     * pick some items if they can be picked.
     */
    private void pick() {
        if (canPick()) {
            if (tileToItem.get(player.stand()).equal(Itemset.TORCH)) {
                torch = true;
            }
            if (tileToItem.get(player.stand()).equal(Itemset.KEY)) {
                getKey = true;
            }
            player.bag().add(tileToItem.get(player.stand()));
            player.changeStand(Tileset.FLOOR);
        }
    }

    /**
     * define some TEtile that can be passed.
     * @param x
     * @param y
     * @param world
     * @return
     */
    private boolean canPass(int x, int y, TETile[][] world) {
        String teTile = world[x][y].description();
        switch (teTile) {
            case "you": return true;
            case "nothing": return false;
            case "wall": return false;
            case "floor": return true;
            case "unlocked door": return true;
            case "locked door":
                if (getKey) {
                    drawFrame("You win");
                    StdDraw.pause(2000);
                    System.exit(0);
                }
                return true;
            case "torch": return true;
            case "key": return true;
            default: return false;
        }
    }

    /**
     * define some items can be picked.
     * @return
     */
    private boolean canPick() {
        if (player.bag().isFull()) {
            return false;
        }
        String item = player.stand().description();
        switch (item) {
            case "key": return true;
            case "torch": return true;
            default: return false;
        }
    }

    /**
     * make a world where player only can see one Tetile near him.
     * @param world
     * @return
     */
    private TETile[][] makeDark(TETile[][] world) {
        TETile[][] darkWorld = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                darkWorld[x][y] = Tileset.NOTHING;
            }
        }

        // copy the TeTile around player
        int playerX = player.position().x();
        int playerY = player.position().y();
        for (int x = playerX - 1; x <= playerX + 1; x ++) {
            for (int y = playerY - 1; y <= playerY + 1; y ++) {
                darkWorld[x][y] = world[x][y];
            }
        }
        return darkWorld;
    }

    /**
     * make a world when player get the torch.
     * @param world
     * @return
     */
    private TETile[][] torchWorld(TETile[][] world) {
        TETile[][] torchWorld = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                torchWorld[x][y] = Tileset.NOTHING;
            }
        }

        // copy the TeTile around player
        int playerX = player.position().x();
        int playerY = player.position().y();
        for (int x = playerX; x <= WIDTH; x ++) {
            if (world[x][playerY] == Tileset.WALL) {
                torchWorld[x][playerY] = world[x][playerY];
                break;
            }
            torchWorld[x][playerY] = world[x][playerY];
        }
        for (int x = playerX; x >= 0; x --) {
            if (world[x][playerY] == Tileset.WALL) {
                torchWorld[x][playerY] = world[x][playerY];
                break;
            }
            torchWorld[x][playerY] = world[x][playerY];
        }
        for (int y = playerY; y <= HEIGHT; y ++) {
            if (world[playerX][y] == Tileset.WALL) {
                torchWorld[playerX][y] = world[playerX][y];
                break;
            }
            torchWorld[playerX][y] = world[playerX][y];
        }
        for (int y = playerY; y >= 0; y --) {
            if (world[playerX][y] == Tileset.WALL) {
                torchWorld[playerX][y] = world[playerX][y];
                break;
            }
            torchWorld[playerX][y] = world[playerX][y];
        }
        return torchWorld;
    }

    private void save(String input) {
        File f = new File("./game_save.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(input);
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }


    private String load() {
        File f = new File("./game_save.txt");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                return (String) os.readObject();
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
        return null;
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
        ter.initialize(WIDTH, HEIGHT + 6, 0, 3);

        // Set a new game
        inputNew = input;
        long seed = dealWithInputNew(input);
        random = new Random(seed);
        addRooms(finalWorldFrame, random, rooms);
        connectRooms(rooms,finalWorldFrame);
        setPlayerAndItem(finalWorldFrame);

        // load the game
        String controlInput = dealWithInputLoad(input);
        if (controlInput != null) {
            loadPlayAndItem(controlInput, finalWorldFrame);
        }
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
                world[p.x() + w][p.y() + h] = Tileset.FLOOR;
            }
        }
        // make outer tiles become wall.
        for (int w = 0; w < roomWidth; w ++) {
            world[p.x() + w][p.y()] = Tileset.WALL;
            world[p.x() + w][p.y() + roomHeight - 1] = Tileset.WALL;
        }
        for (int h = 0; h < roomHeight; h ++) {
            world[p.x()][p.y() + h] = Tileset.WALL;
            world[p.x() + roomWidth - 1][p.y() + h] = Tileset.WALL;
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
            int startX = room.rightUpper().x() + 1;
            int startY = room.leftLower().y();
            int endY = room.rightUpper().y();
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
            int startY = room.rightUpper().y() + 1;
            int startX = room.leftLower().x();
            int endX = room.rightUpper().x();
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
        int nw = WIDTH / widthPiece; //divide width into 10 piece.
        int nh = HEIGHT / heightPiece; //divide height into 5 piece.
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
        while (upperNum % (heightPiece) != 0) {
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
        int rightNum = num + heightPiece;
        while (rightNum <= (widthPiece * heightPiece - 1)) {
            if (roomNum.contains(rightNum)) {
                return rightNum;
            }
            rightNum += heightPiece;
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
            leftLower = new Position(room.leftLower().x(),
                    room.rightUpper().y());
            rightUpper = new Position(room.leftLower().x() + 2,
                    upperRoom.leftLower().y());
        } else { // upper room is on the right
            // if room is too small ,enlarge its width
            if (lackWidth != -34) {
                enlargeRoom(world, room, -lackWidth, 0);
                room.enlargeRoom(-lackWidth, 0);
            }
            leftLower = new Position(upperRoom.leftLower().x(),
                    room.rightUpper().y());
            rightUpper = new Position(upperRoom.leftLower().x() + 2,
                    upperRoom.leftLower().y());
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
            leftLower = new Position(room.rightUpper().x(),
                    room.leftLower().y());
            rightUpper = new Position(rightRoom.leftLower().x(),
                    room.leftLower().y() + 2);
        } else { // upper room is on the right
            // if room is too small ,enlarge its width
            if (lackHeight != -34) {
                enlargeRoom(world, room, 0, -lackHeight);
                room.enlargeRoom(0, -lackHeight);
            }
            leftLower = new Position(room.rightUpper().x(),
                    rightRoom.leftLower().y());
            rightUpper = new Position(rightRoom.leftLower().x(),
                    rightRoom.leftLower().y() + 2);
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
        for (int y = leftLower.y() + 1; y < rightUpper.y(); y ++) {
            if (world[leftLower.x()][y] == Tileset.FLOOR) {
                // if upper and right hallway are interconnect, make unlocked door
                world[leftLower.x()][y] = Tileset.UNLOCKED_DOOR;
            } else {
                world[leftLower.x()][y] = Tileset.WALL;
            }
        }
        for (int y = leftLower.y() + 1; y < rightUpper.y(); y ++) {
            if (world[leftLower.x() + 2][y] == Tileset.FLOOR) {
                // if upper and right hallway are interconnect, make unlocked door
                world[leftLower.x() + 2][y] = Tileset.UNLOCKED_DOOR;
            } else {
                world[leftLower.x() + 2][y] = Tileset.WALL;
            }
        }
        for (int y = leftLower.y(); y <= rightUpper.y(); y ++) {
                world[leftLower.x() + 1][y] = Tileset.FLOOR;
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
        for (int x = leftLower.x() + 1; x < rightUpper.x(); x ++) {
            if (world[x][leftLower.y()] == Tileset.FLOOR) {
                // if upper and right hallway are interconnect, make unlocked door
                world[x][leftLower.y()] = Tileset.UNLOCKED_DOOR;
            } else {
                world[x][leftLower.y()] = Tileset.WALL;
            }
        }
        for (int x = leftLower.x() + 1; x < rightUpper.x(); x ++) {
            if (world[x][leftLower.y() + 2] == Tileset.FLOOR) {
                // if upper and right hallway are interconnect, make unlocked door
                world[x][leftLower.y() + 2] = Tileset.UNLOCKED_DOOR;
            } else {
                world[x][leftLower.y() + 2] = Tileset.WALL;
            }
        }
        for (int x = leftLower.x(); x <= rightUpper.x(); x ++) {
            world[x][leftLower.y() + 1] = Tileset.FLOOR;
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
        if (room.leftLower().x() >= upperRoom.leftLower().x()) {
            int reduceWidth = 1 + upperRoom.rightUpper().x() - room.leftLower().x();
            if (reduceWidth < 3) {
                lackWidth = 3 - reduceWidth;
            } else {
                lackWidth = 34;
            }
        } else { // upper room is on th right
            int reduceWidth =1 + room.rightUpper().x() - upperRoom.leftLower().x();
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
    private int canBuildRightHallway(Room room, Room rightRoom) {
        int lackHeight ;
        // room is on the top
        if (room.leftLower().y() >= rightRoom.leftLower().y()) {
            // make lackHeight be positive
            int reduceHeight = 1 + rightRoom.rightUpper().y() - room.leftLower().y();
            if (reduceHeight < 3) {
                lackHeight = 3 - reduceHeight;
            } else {
                lackHeight = 34;
            }
        } else { // upper room is on the top
            // make lackHeight be negative
            int reduceHeight = 1 + room.rightUpper().y() - rightRoom.leftLower().y();
            if (reduceHeight < 3) {
                lackHeight = reduceHeight - 3;
            } else {
                lackHeight = -34;
            }
        }
        return lackHeight;
    }

    /**
     * Deal with the input and get the seed.
     * if it includes 'n', then open a new game with seed which is ended with "s".
     * input string can be upper or lower case and be able to accept either keypress
     * @param input
     * @return return lowercase string.
     */
    private long dealWithInputNew(String input) {
        String lowerInput = input.toLowerCase();
        for (int i = 0; i < lowerInput.length(); i ++) {
            if (lowerInput.charAt(i) == 'n') {
                return getSeed(lowerInput.substring(i + 1));
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Deal with the input and get the string after 's'.
     * Otherwise, return null.
     * @param input
     * @return return lowercase string.
     */
    private String dealWithInputLoad(String input) {
        String lowerInput = input.toLowerCase();
        for (int i = 0; i < lowerInput.length(); i++) {
            if (lowerInput.charAt(i) == 's') {
                return lowerInput.substring(i + 1);
            }
        }
        return null;
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
                    System.exit(0);
                } else {
                    return seed.longValue();
                }
            }
        }
        throw new IllegalArgumentException("input must include 's'");
    }


    private class Position {

        private int x, y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int x() {
            return this.x;
        }

        public int y() {
            return this.y;
        }
    }

    private class Room {

        private Position leftLower, rightUpper;
        private int width, height;

        public Room(Position p, int width, int height) {
            this.leftLower = p;
            this.width = width;
            this.height = height;
            this.rightUpper = new Position(p.x() + width - 1, p.y() + height - 1);
        }

        public Position leftLower() {
            return this.leftLower;
        }

        public Position rightUpper() {
            return this.rightUpper;
        }


        public void enlargeRoom(int w, int h) {
            this.width = this.width + w;
            this.height = this.height + h;
            this.rightUpper = new Position(leftLower.x() + width - 1, leftLower.y() + height - 1);
        }
    }

    private class Player {

        private Position p;
        private Bag b;
        private TETile stand;

        public Player(Position p, TETile t) {
            this.p = p;
            this.b = new Bag(9);
            this.stand = t;
        }

        public Position position() {
            return this.p;
        }

        public void go(Position p) {
            this.p = p;
        }

        public TETile stand() {
            return this.stand;
        }

        public void changeStand(TETile t) {
            this.stand = t;
        }

        public Bag bag() {
            return this. b;
        }

    }

    private class Bag {
        int volume;
        Item[] items;
        int num = 0;

        public Bag(int v) {
            this.volume = v;
            this.items = new Item[volume];
        }

        public void add(Item i) {
            items[num] = i;
            num += 1;
        }

        public Item item(int i) {
            return items[i];
        }

        public boolean isFull() {
            return volume <= num;
        }

        public int num() {
            return this.num;
        }
    }
}


