import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SeparableEnemySolver {

    Graph g;

    /**
     * Creates a SeparableEnemySolver for a file with name filename. Enemy
     * relationships are biderectional (if A is an enemy of B, B is an enemy of A).
     */
    SeparableEnemySolver(String filename) throws java.io.FileNotFoundException {
        this.g = graphFromFile(filename);
    }

    /** Alterntive constructor that requires a Graph object. */
    SeparableEnemySolver(Graph g) {
        this.g = g;
    }

    /**
     * Returns true if input is separable, false otherwise.
     */
    public boolean isSeparable() {
        // TODO: Fix me
        Object[] nodes = g.labels().toArray();
        Set markSet = new HashSet();
        Map par = new HashMap();
        boolean[] isSeparable = new boolean[1];
        isSeparable[0] = true;
        for (Object o : nodes) {
            if (!markSet.contains(o)) {
                String start = (String)o;
                dfsCycle(start, start, markSet, par, isSeparable);
            }
        }
        return isSeparable[0];
    }


    private void dfsCycle(String u, String p, Set mark, Map<String, String> par, boolean[] isSeparable) {
        // marks visited node
       mark.add(u);
       for (String neighbor : g.neighbors(u)) {
           // check if neighbor is the parent of u
           if (neighbor == p) {
               continue;
           }
           // if the neighbor is not be marked, keep dfs
           if (!mark.contains(neighbor)) {
               par.put(neighbor, u);
               dfsCycle(neighbor, u, mark, par, isSeparable);
           } else {
               // find the cycle
               String cur = u;
               int nodeCount = 1;
               while (cur != null && !cur.equals(neighbor)) {
                   cur = par.get(cur);
                   nodeCount += 1;
               }
               if (nodeCount != 1 && nodeCount % 2 == 1) {
                   isSeparable[0] = false;
               }
           }
       }
    }

    /* HELPERS FOR READING IN CSV FILES. */

    /**
     * Creates graph from filename. File should be comma-separated. The first line
     * contains comma-separated names of all people. Subsequent lines each have two
     * comma-separated names of enemy pairs.
     */
    private Graph graphFromFile(String filename) throws FileNotFoundException {
        List<List<String>> lines = readCSV(filename);
        Graph input = new Graph();
        for (int i = 0; i < lines.size(); i++) {
            if (i == 0) {
                for (String name : lines.get(i)) {
                    input.addNode(name);
                }
                continue;
            }
            assert(lines.get(i).size() == 2);
            input.connect(lines.get(i).get(0), lines.get(i).get(1));
        }
        return input;
    }

    /**
     * Reads an entire CSV and returns a List of Lists. Each inner
     * List represents a line of the CSV with each comma-seperated
     * value as an entry. Assumes CSV file does not contain commas
     * except as separators.
     * Returns null if invalid filename.
     *
     * @source https://www.baeldung.com/java-csv-file-array
     */
    private List<List<String>> readCSV(String filename) throws java.io.FileNotFoundException {
        List<List<String>> records = new ArrayList<>();
        Scanner scanner = new Scanner(new File(filename));
        while (scanner.hasNextLine()) {
            records.add(getRecordFromLine(scanner.nextLine()));
        }
        return records;
    }

    /**
     * Reads one line of a CSV.
     *
     * @source https://www.baeldung.com/java-csv-file-array
     */
    private List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        Scanner rowScanner = new Scanner(line);
        rowScanner.useDelimiter(",");
        while (rowScanner.hasNext()) {
            values.add(rowScanner.next().trim());
        }
        return values;
    }

    /* END HELPERS  FOR READING IN CSV FILES. */

}
