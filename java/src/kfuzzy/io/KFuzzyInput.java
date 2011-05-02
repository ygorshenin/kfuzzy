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
     * @param vectors list of vectors, length must be equal to number
     * of objects and size of each vector must equal to numDimensions
     */
    public KFuzzyInput(int numObjects, int numDimensions, Vector[] vectors, int numClusters) {
	assert vectors.length == numObjects;

	this.numObjects = numObjects;
	this.numDimensions = numDimensions;

	this.vectors = vectors;

	for (Vector v : vectors)
	    assert numDimensions == v.getSize();

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
