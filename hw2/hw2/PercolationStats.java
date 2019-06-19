package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;
import edu.princeton.cs.introcs.Stopwatch;

import java.util.HashSet;
import java.util.Set;

public class PercolationStats {

    private double[] fractions;
    private int time = 0;
    private int T;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("N and T should not be equal and smaller than 0");
        }
        this.T = T;
        Percolation p = pf.make(N); // Initialize all sites to be blocked.
        Set<Integer> count = new HashSet<>();
        fractions = new double[T];
        while (time < T) { // Repeat the following until the system percolates
            int row = StdRandom.uniform(N);
            int col = StdRandom.uniform(N);
            if (count.contains(row * N + col)) {
                continue;
            } else {
                p.open(row, col);
                count.add(row * N + col);
                if (p.percolates()) {
                    fractions[time] = (double)count.size() / (N * N);
                    time ++;
                }
            }
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(fractions);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(fractions);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - (1.96 * stddev() / Math.sqrt(T));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + (1.96 * stddev() / Math.sqrt(T));
    }

    // test
    public static void main(String[] args) {
        Stopwatch stopwatch = new Stopwatch();
        PercolationFactory testpf = new PercolationFactory();
        PercolationStats test = new PercolationStats(100, 100, testpf);
        System.out.println(stopwatch.elapsedTime());
        System.out.println(test.fractions[0]);
        System.out.println(test.fractions[50]);
        System.out.println(test.fractions[99]);
        System.out.println(test.mean());
        System.out.println(test.stddev());
        System.out.println(test.confidenceLow());
        System.out.println(test.confidenceHigh());
        System.out.println(stopwatch.elapsedTime());
        PercolationStats test2 = new PercolationStats(100, 200, testpf);
        PercolationStats test3 = new PercolationStats(100, 300, testpf);
        System.out.println(stopwatch.elapsedTime());
    }

}
