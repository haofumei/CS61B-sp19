package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.introcs.StdRandom;

public class Percolation {

    private int N, top, bottom;
    private WeightedQuickUnionUF grid, noBottomGrid;
    private int openNum;
    private int[] valve; // 0 represents close, 1 represents open
    // create N-by-N grid, with all sites initially blocked
    // (N~2)
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("input should not be smaller than 0");
        }
        this.N = N;
        openNum = 0;
        top = N * N;
        bottom = N * N + 1;
        valve = new int[N * N]; // 0 represents close, 1 represents open
        grid = new WeightedQuickUnionUF(N * N + 2);
        noBottomGrid = new WeightedQuickUnionUF(N * N + 1);
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);
        if (isOpen(row, col)) {
            return;
        }
        openNum ++;
        valve[xyTo1D(row, col)] = 1; // turn on the valve
        if (row == 0) { // union to top
            grid.union(xyTo1D(row, col), top);
            noBottomGrid.union(xyTo1D(row, col), top);
        } else if (row == N - 1) { // else union to bottom
            grid.union(xyTo1D(row, col), bottom);
        } else if (col > 0 && isOpen(row, col - 1)) {
            grid.union(xyTo1D(row, col), xyTo1D(row, col - 1));
            noBottomGrid.union(xyTo1D(row, col), xyTo1D(row, col - 1));
        } if (col < N - 1 && isOpen(row, col + 1)) {
            grid.union(xyTo1D(row, col), xyTo1D(row, col + 1));
            noBottomGrid.union(xyTo1D(row, col), xyTo1D(row, col + 1));
        } if (row > 0 && isOpen(row - 1, col)) {
            grid.union(xyTo1D(row, col), xyTo1D(row - 1, col));
            noBottomGrid.union(xyTo1D(row, col), xyTo1D(row - 1, col));
        } if (row < N - 1 && isOpen(row + 1, col)) {
            grid.union(xyTo1D(row, col), xyTo1D(row + 1, col));
            noBottomGrid.union(xyTo1D(row, col), xyTo1D(row + 1, col));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return valve[xyTo1D(row, col)] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        return noBottomGrid.connected(xyTo1D(row, col), top);
    }

    // number of open sites
    // constant time
    public int numberOfOpenSites() {
        return openNum;
    }

    // does the system percolate?
    public boolean percolates() {
        return grid.connected(top, bottom);
    }

    // use for unit testing (not required, but keep this here for the autograder)
    public static void main(String[] args) {
        Percolation test = new Percolation(4);
        test.open(2, 2);
        test.open(1, 2);
        test.open(0, 2);
        System.out.println(test.isFull(2, 2) + " test isFull, should be true");
        System.out.println(test.numberOfOpenSites() + " test openNum, should be 3");

        System.out.println(test.percolates() + " test percolates, should be false");
        test.open(3, 2);
        System.out.println(test.percolates() + " test percolates, should be true");

        test.open(2, 0);
        test.open(3, 0);
        System.out.println(test.isFull(2, 0) + " test isFull, should be false");

        int[] rows = StdRandom.permutation(5);
        int[] cols = StdRandom.permutation(5);
        for (int i = 0; i < rows.length; i++) {
            System.out.print(rows[i]);
            System.out.println(cols[i]);
        }
    }

    // 2D to 1D
    private int xyTo1D (int r, int c) {
        return r * N + c;
    }

    private void validate(int row, int col) {
        if (row < 0 || row > N - 1) {
            throw new IndexOutOfBoundsException("row out of range from 0 to " + (N - 1));
        }
        if (col < 0 || col > N - 1) {
            throw new IndexOutOfBoundsException("col out of range from 0 to " + (N - 1));
        }
    }
}
