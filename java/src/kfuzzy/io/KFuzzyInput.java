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
     * Number of objects
     */
    private final int numObjects;
    /**
     * Number of clusters
     */
    private final int numClusters;
    /**
     * Number of dimensions
     */
    private final int numDimensions;
    /**
     * List of vectors
     */
    private final Vector[] vectors;
    /**
     * Constructor specifying number of clusters and list of vectors.
     *
     * @param numObjects number of objects
     * @param numDimensions number of dimensions
     * @param numClusters number of clusters
     * @param vectors list of vectors
     */
    public KFuzzyInput(int numObjects, Vector[] vectors, int numDimensions, int numClusters) {
	this.numObjects = numObjects;
	this.vectors = vectors;
	this.numDimensions = numDimensions;
	this.numClusters = numClusters;
    }
    /**
     * @return number of vectors
     */
    public int getNumObjects() {
	return numObjects;
    }
    /**
     * @return number of clusters
     */
    public int getNumClusters() {
	return numClusters;
    }
    /**
     * @return number of dimensions
     */
    public int getNumDimensions() {
	return numDimensions;
    }
    /**
     * @return list of vectors
     */
    public Vector[] getVectors() {
	return vectors;
    }
}
