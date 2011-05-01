package kfuzzy;

import java.util.*;

import kfuzzy.algo.KFuzzyAlgorithm;
import kfuzzy.math.Vector;


public class Model {
    private KFuzzyAlgorithm algorithm;
    private Vector[] points;
    private int[] clusters;


    public Model() {
	algorithm = new KFuzzyAlgorithm();
	points = new Vector[] {};
	clusters = new int[] {};
    }

    public void setPoints(Vector[] points) {
	this.points = points;
	clusters = new int[points.length];
	Arrays.fill(clusters, -1);
    }

    public void clusterize(int numClusters, KFuzzyAlgorithm.Options options) {
	clusters = algorithm.clusterize(points, numClusters, options);
    }

    public Vector[] getPoints() {
	return points;
    }

    public int[] getClusters() {
	return clusters;
    }
}
