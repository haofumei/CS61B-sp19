import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by hug.
 */
public class Experiments {

    static BST bst = new BST();
    static Random r = new Random();
    static List<Double> rdValues = new ArrayList<>();
    static List<Double> opValues = new ArrayList<>();
    static List<Integer> nValues = new ArrayList<>();


    static List<Integer> keys = new ArrayList<>(500);
    static List<Double> yValues = new ArrayList<>();
    static List<Integer> xValues = new ArrayList<>();

    public static void experiment1() {

        for (int i = 1; i < 5001; i ++) {
            bst.add(r.nextInt(5001));
            double thisrd = bst.averageDepth();

            nValues.add(i); // add x axe
            rdValues.add(thisrd); // add rdValues

            double thisop = ExperimentHelper.optimalAverageDepth(i); // add opValues
            opValues.add(thisop);
        }

    }

    public static void experiment2() {
        for (int i = 0; i < 500; i ++) { // initiate the bst
            int key = r.nextInt(10000);
            bst.add(key);
            keys.add(key);
        }

        yValues.add(bst.averageDepth()); // record the start depth
        xValues.add(0);

        for (int i = 0; i < 1000; i ++) {
            int indexItem = (int)(Math.random() * keys.size());
            int deleteKey = keys.get(indexItem);
            int insertKey = (int)(Math.random() * 10000);
            while (bst.contains(insertKey)) {
                insertKey = (int)(Math.random() * 10000);
            }
            ExperimentHelper.scsDeleteAndInsert(bst, deleteKey, insertKey);
            yValues.add(bst.averageDepth()); // record i time depth
            xValues.add(i);
            keys.remove(indexItem); // update the keys
            keys.add(insertKey);
        }
    }

    public static void experiment3() {
        for (int i = 0; i < 500; i ++) { // initiate the bst
            int key = r.nextInt(10000);
            bst.add(key);
            keys.add(key);
        }

        yValues.add(bst.averageDepth()); // record the start depth
        xValues.add(0);

        for (int i = 0; i < 100000; i ++) {
            int indexItem = (int)(Math.random() * keys.size());
            int deleteKey = keys.get(indexItem);
            int insertKey = (int)(Math.random() * 10000);
            while (bst.contains(insertKey)) {
                insertKey = (int)(Math.random() * 10000);
            }
            ExperimentHelper.rdDeleteAndInsert(bst, deleteKey, insertKey);
            yValues.add(bst.averageDepth()); // record i time depth
            xValues.add(i);
            keys.remove(indexItem); // update the keys
            keys.add(insertKey);
        }
    }

    public static void main(String[] args) {
        XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("x label").yAxisTitle("y label").build();

    /**    experiment1();
        chart.addSeries("random BST", nValues, rdValues);
        chart.addSeries("optimal BST", nValues, opValues);*/

    /**    experiment2();
        chart.addSeries("experiments2", xValues, yValues);*/

        experiment3();
        chart.addSeries("experiments3", xValues, yValues);

        new SwingWrapper(chart).displayChart();
    }
}
