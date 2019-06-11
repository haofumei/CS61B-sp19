import org.junit.Test;
import static org.junit.Assert.*;

public class TestUnionFind {
    UnionFind test1 = new UnionFind(10);
    //Test UnionFind(int n)
    @Test
    public void testUnionFind() {
        for (int i = 0; i < 10; i++) {
            assertEquals(-1, test1.parent(i));
            assertEquals(1, test1.sizeOf(i));
        }
        // test union
        test1.union(1, 2);
        test1.union(3, 4);
        test1.union(1, 3);
        assertEquals(4, test1.sizeOf(1));
        assertEquals(4, test1.parent(1));

    }


}
