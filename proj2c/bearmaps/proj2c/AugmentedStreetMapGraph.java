package bearmaps.proj2c;

import bearmaps.hw4.streetmap.Node;
import bearmaps.hw4.streetmap.StreetMapGraph;
import bearmaps.proj2ab.KDTree;
import bearmaps.proj2ab.Point;
import bearmaps.proj2ab.PointSet;
import bearmaps.proj2ab.WeirdPointSet;
import bearmaps.lab9.MyTrieSet;
import bearmaps.lab9.TrieSet61B;


import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {

    List<Node> locaNode;
    TrieSet61B locaName = new MyTrieSet();
    Map<String, String> cleanTofullName = new HashMap<>();
    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
        locaNode = this.getNodes();
        /*for (Node n : locaNode) {
            if (n.name() == null) {
                continue;
            }
            String cleanName = n.name();
            // Removes Special Characters and Digits
            cleanName = cleanName.replaceAll("[^a-zA-Z\\s]", "");
            // Remove spaces
            cleanName = cleanName.replaceAll("\\s+", " ");
            // to lowercase
            cleanName = cleanName.toLowerCase();
            locaName.add(cleanName);
            cleanTofullName.put(cleanName, n.name());
           System.out.println(cleanName);
        }*/
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        List<Point> locaPoint = new LinkedList<>();
        Map<Point, Node> pointToNode = new HashMap<>();
        // convert Node to Point
        for (Node n : locaNode) {
            Point toAdd = new Point(n.lon(), n.lat());
            locaPoint.add(toAdd);
            pointToNode.put(toAdd, n);
        }

        PointSet mapGraph = new KDTree(locaPoint);
        Point closest = mapGraph.nearest(lon, lat);
        long id = pointToNode.get(closest).id();
        while (neighbors(id).isEmpty()) {
            locaPoint.remove(closest);
            mapGraph = new KDTree(locaPoint);
            closest = mapGraph.nearest(lon, lat);
            id = pointToNode.get(closest).id();
        }
        return id;
    }



    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        List<String> toReturn = locaName.keysWithPrefix(prefix);
        return toReturn;
    }



    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        return new LinkedList<>();
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
