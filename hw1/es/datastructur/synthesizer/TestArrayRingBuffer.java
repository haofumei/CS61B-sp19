package es.datastructur.synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer arb = new ArrayRingBuffer(3);
        //test capacity
        assertEquals(3,arb.capacity() );
        //test enqueue
        arb.enqueue(1);
        arb.enqueue(2);
        assertEquals(2, arb.fillCount());
        // test dequeue
        assertEquals(1, arb.dequeue());
        assertEquals(1, arb.fillCount());
        // test peek
        assertEquals(2, arb.peek());
        assertEquals(1,arb.fillCount());





    }
}
