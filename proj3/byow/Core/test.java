package byow.Core;

import byow.TileEngine.TETile;

public class test {
    public static void main(String[] args) {
        Engine engine = new Engine();
        TETile[][] world = engine.interactWithInputString("efn-92233585623s");
        engine.ter.renderFrame(world);
    }
}
