package kfuzzy.io;


public class KFuzzyOutput {
    private final int numClusters;
    private final int numObjects;
    private final double blending;
    private final int numIterations;
    private final int[] matching;

    public KFuzzyOutput(int numClusters, int numObjects, int[] matching, double blending, int numIterations) {
	this.numClusters = numClusters;
	this.numObjects = numObjects;
	this.matching = matching;
	this.blending = blending;
	this.numIterations = numIterations;
    }

    public int getNumClusters() {
	return numClusters;
    }

    public int getNumObjects() {
	return numObjects;
    }

    public int[] getMatching() {
	return matching;
    }

    public double getBlending() {
	return blending;
    }

    public int getNumIterations() {
	return numIterations;
    }
}
