import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Solver for the Flight problem (#9) from CS 61B Spring 2018 Midterm 2.
 * Assumes valid input, i.e. all Flight start times are >= end times.
 * If a flight starts at the same time as a flight's end time, they are
 * considered to be in the air at the same time.
 */
public class FlightSolver {

    private Comparator<Flight> startTComparator = (i, j) -> i.startTime() - j.startTime();
    private Comparator<Flight> endTComparator = (i, j) -> i.endTime() - j.endTime();
    private PriorityQueue<Flight> minStartT;
    private PriorityQueue<Flight> minEndT;
    private int maxSum = 0;

    public FlightSolver(ArrayList<Flight> flights) {

        minStartT = new PriorityQueue<>(flights.size(), startTComparator);
        minEndT = new PriorityQueue<>(flights.size(), endTComparator);
        int sum = 0;

        for (Flight f : flights) { // N
            minStartT.add(f); // log(N)
            minEndT.add(f); // log(N)
        }

        while (!minStartT.isEmpty()) { // N
            Flight takeOff = minStartT.peek();
            Flight landing = minEndT.peek();
            if (takeOff.startTime() <= landing.endTime()) { // no flight lands
                sum += takeOff.passengers();
                minStartT.poll(); // log(N)
            }  else { // some flights land
                sum -= landing.passengers();
                minEndT.poll(); // log(N)
            }
            if (maxSum < sum) {
                maxSum = sum;
            }
        }
    }

    public int solve() {
        return maxSum;
    }

}
