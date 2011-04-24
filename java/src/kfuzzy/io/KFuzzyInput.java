package kfuzzy.io;

import kfuzzy.math.Vector;


/**
 * Input to KFuzzy algorithm.
 *
 * @author Yuri Gorshenin
 * @version 2011.0424
 * @since 1.6
 */
public class KFuzzyInput {
    /**
     * Number of clusters
     */
    private final int numClusters;
    /**
     * List of vectors
     */
    private final Vector[] vectors;
    /**
     * Constructor specifying number of clusters and list of vectors.
     *
     * @param numClusters number of clusters
     * @param vectors list of vectors
     */
    public KFuzzyInput(Vector[] vectors, int numClusters) {
	this.numClusters = numClusters;
	this.vectors = vectors;
    }
    /**
     * @return number of vectors
     */
    public int getNumObjects() {
	return vectors.length;
    }
    /**
     * @return number of clusters
     */
    public int getNumClusters() {
	return numClusters;
    }
    /**
     * @return list of vectors
     */
    public Vector[] getVectors() {
	return vectors;
    }
}
