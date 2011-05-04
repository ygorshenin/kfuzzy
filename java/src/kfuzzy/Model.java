package kfuzzy;

import java.util.*;

import kfuzzy.algo.KFuzzyAlgorithm;
import kfuzzy.io.KFuzzyOutput;
import kfuzzy.math.Vector;


public class Model {
    private KFuzzyAlgorithm algorithm;
    private Vector[] points;
    private int[] clusters;
    private KFuzzyOutput output;


    public Model() {
	algorithm = new KFuzzyAlgorithm();
	points = new Vector[] {};
	clusters = new int[] {};
	output = null;
    }

    public void setPoints(Vector[] points) {
	this.points = points;
	clusters = new int[points.length];
	Arrays.fill(clusters, -1);
    }

    public void clusterize(int numClusters, KFuzzyAlgorithm.Options options) {
	clusters = algorithm.clusterize(points, numClusters, options);
	output = new KFuzzyOutput(numClusters, points.length, clusters, options.blending, options.maxIterations);
    }

    public Vector[] getPoints() {
	return points;
    }

    public int[] getClusters() {
	return clusters;
    }

    public KFuzzyOutput getOutput() {
	return output;
    }
}
