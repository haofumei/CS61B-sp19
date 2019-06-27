import org.junit.Test;
import static org.junit.Assert.*;

public class RabinKarpAlgorithmTests {
    @Test
    public void basic() {
        String input = "hello";
        String pattern = "ell";
        assertEquals(1, RabinKarpAlgorithm.rabinKarp(input, pattern));

        String input1 = "ellosellfsdf";
        String pattern1 = "ell";
        assertEquals(0, RabinKarpAlgorithm.rabinKarp(input1, pattern1));

        String input2 = "ellosellfsdf";
        String pattern2 = "wll";
        assertEquals(-1, RabinKarpAlgorithm.rabinKarp(input2, pattern2));
    }
}
