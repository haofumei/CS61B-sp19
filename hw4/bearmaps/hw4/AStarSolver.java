package bearmaps.hw4;

import bearmaps.proj2ab.DoubleMapPQ;
import bearmaps.proj2ab.ExtrinsicMinPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * an artificial intelligence that can solve arbitrary state space traversal problems.
 * Specifically, given a graph of possible states,
 * your AI will find the optimal route from the start state to a goal state.
 */
// The algorithm starts with only the start vertex in the PQ.
// When relaxing an edge, if the relaxation is successful and the target vertex is not in the PQ, add it.
// If the algorithm takes longer than some timeout value, it stops running.
public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {

    private SolverOutcome outcome;
    private double solutionWeight;
    private LinkedList<Vertex> solution = new LinkedList<>();
    private double timeSpent;
    private int numStatesExplored = 0;
    private Map<Vertex, Vertex> getParent = new HashMap<>(); // use to keep tracking of the solution.

    /**
     * Constructor which finds the solution,
     * computing everything necessary for all other methods to return their results in constant time.
     * Note that timeout passed in is in seconds.
     */
    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        Stopwatch sw = new Stopwatch();
        // Insert the source vertex into the PQ.
        ExtrinsicMinPQ<Vertex> pq = new DoubleMapPQ<>();
        Map<Vertex, Double> distTo = new HashMap();

        double curPriority = input.estimatedDistanceToGoal(start, end);
        distTo.put(start, 0.0);
        pq.add(start, curPriority);
        // Repeat until the PQ is empty, PQ.getSmallest() is the goal, or timeout is exceeded.
        while (pq.size() != 0) {
            if (pq.getSmallest().equals(end)) {
                outcome = SolverOutcome.SOLVED;
                solutionWeight = distTo.get(end);
                timeSpent = sw.elapsedTime();

                solution.addFirst(end);
                Vertex curPar = getParent.get(end);
                while (!curPar.equals(start)) {
                    solution.addFirst(curPar);
                    curPar = getParent.get(curPar);
                }
                solution.addFirst(curPar);
                return;
            }
            if (sw.elapsedTime() > timeout) {
                outcome = SolverOutcome.TIMEOUT;
                solution.clear();
                timeSpent = sw.elapsedTime();
                return;
            } else {
                Vertex p = pq.removeSmallest();
                numStatesExplored += 1;
                for (WeightedEdge<Vertex> e : input.neighbors(p)) {
                    relax(e, input, end, distTo, pq);
                }
            }
        }
        outcome = SolverOutcome.UNSOLVABLE;
        solution.clear();
        timeSpent = sw.elapsedTime();
    }

    private void relax(WeightedEdge<Vertex> edge, AStarGraph<Vertex> input, Vertex goal,
                       Map<Vertex, Double> dist, ExtrinsicMinPQ<Vertex> pq ) {
        Vertex p = edge.from();
        Vertex q = edge.to();
        double w = edge.weight();
        if (!dist.containsKey(q) || dist.get(p) + w < dist.get(q)) {
            getParent.put(q, p); // Set p to q's parent.
            dist.put(q, dist.get(p) + w);
            double h = input.estimatedDistanceToGoal(q, goal);
            double d = dist.get(q);
            if (pq.contains(q)) {
                pq.changePriority(q, d + h);
            } else {
                pq.add(q, d + h);
            }
        }
    }

    /**
     * Returns one of SolverOutcome.SOLVED, SolverOutcome.TIMEOUT, or SolverOutcome.UNSOLVABLE.
     * Should be SOLVED if the AStarSolver was able to complete all work in the time given.
     * UNSOLVABLE if the priority queue became empty.
     * TIMEOUT if the solver ran out of time.
     * You should check to see if you have run out of time every time you dequeue.
     */
    @Override
    public SolverOutcome outcome() {
        return this.outcome;
    }

    /**
     * A list of vertices corresponding to a solution.
     * Should be empty if result was TIMEOUT or UNSOLVABLE.
     */
    @Override
    public List<Vertex> solution() {
        return this.solution;
    }

    /**
     * The total weight of the given solution, taking into account edge weights.
     * Should be 0 if result was TIMEOUT or UNSOLVABLE.
     */
    @Override
    public double solutionWeight() {
        return this.solutionWeight;
    }


    /**
     * The total number of priority queue dequeue operations.
     */
    @Override
    public int numStatesExplored() {
        return this.numStatesExplored;
    }


    /**
     * The total time spent in seconds by the constructor.
     */
    @Override
    public double explorationTime() {
        return this.timeSpent;
    }
}
