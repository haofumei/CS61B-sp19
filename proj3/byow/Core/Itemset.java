package byow.Core;

import byow.TileEngine.TETile;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Itemset {
    public static final Map<TETile, Item> tileToItem = new HashMap<>();

    public static final Item KEY = new Item('❀', Color.yellow, Color.black, "key");
    public static final Item TORCH = new Item('♠', Color.red, Color.black, "torch");
    public static final Item NOTHING = new Item(' ', Color.black, Color.black, "nothing");

}
