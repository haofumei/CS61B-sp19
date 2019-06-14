public class BubbleGrid {
    private int[][] grid;
    private UnionFind stuck;
    int column;
    int row;

    public BubbleGrid(int c, int r) {
        column = c;
        row = r;
        grid = new int[c][r];
        for (int i = 0; i < c; i ++) {
            for (int j = 0; j < r; j ++) {
                grid[i][j] = 1;
            }
        }
        // create a UnionFind to present the stuck
        stuck = new UnionFind(c * r + 1);
        for (int i = 0; i < c; i ++) {
            for (int j = 1; j <= r; j++) {
                stuck.BGUnion((i + 1) * r + j, i * r + j );
            }
        }

    }
    public int[] popBubbles(int[][] darts) {
       int[] fallBubbles;
       for (int[] dart : darts) {
            int stuckNum = dart[0] * row + 1 + dart[1];
            stuck.reParent(stuckNum);
       }
    }
}
