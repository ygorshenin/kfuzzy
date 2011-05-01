package kfuzzy.algo;

import java.util.*;

import kfuzzy.math.Vector;


/**
 * An implementation of the KFuzzy algorithm.
 *
 * @author Yuri Gorshenin
 * @version 2011.0424
 * @since 1.6
 */
public class KFuzzyAlgorithm {
    /**
     * Options required by algorithm
     *
     * @author Yuri Gorshenin
     * @version 2011.0424
     * @since 1.6
     */
    public static class Options {
	/**
	 * Default value of blending
	 */
	public final static double DEFAULT_BLENDING = 0.0;
	/**
	 * Default number of iterations that will be performed
	 */
	public final static int DEFAULT_MAX_ITERATIONS = 10000;
	/**
	 * Value of blending
	 */
	public final double blending;
	/**
	 * Number of iterations that will be performed
	 */
	public final int maxIterations;
	/**
	 * Constructor, sets all fields to their default values.
	 */
	public Options() {
	    this.blending = DEFAULT_BLENDING;
	    this.maxIterations = DEFAULT_MAX_ITERATIONS;
	}
	/**
	 * Constructor specifying blending and number of iterations.
	 *
	 * @param blending blending
	 * @param maxIterations number of iterations that will be performed
	 */
	public Options(double blending, int maxIterations) {
	    this.blending = blending;
	    this.maxIterations = maxIterations;
	}
    }
    /**
     * If two values differ at most that value, they are considered as equal
     */
    public final static double EPSILON = 1e-9;
    /**
     * Checks two floating-point values for equality
     */
    public final static boolean EQ(double u, double v) {
	return Math.abs(u - v) < EPSILON;
    }
    /**
     * Reference to an implementation of the ClusterCentersInterface
     */
    private ClusterCentersInterface clusterCentersAlgorithm;
    /**
     * Finds possible centers of clusters by given set of vectors.
     *
     * @param vectors set of vectors on which clusterization will be performed
     * @param numClusters number of cluster centers
     * @return an array of possible cluster centers
     */
    private Vector[] findCenters(Vector[] vectors, int numClusters) {
	return clusterCentersAlgorithm.findClusterCenters(vectors, numClusters);
    }
    /**
     * Finds probabilities of the given vector to lie in each cluster
     *
     * @param Vector vector to which probabilities will be computed
     * @param numClusters number of clusters
     * @param centers an array of clusters centers
     * @param Options KFuzzy algorithm options
     * @param probabilities resulting array of probabilities
     */
    private void findProbabilities(Vector vector, int numClusters, Vector[] centers, Options options, double[] probabilities) {
	double power = 1.0 / (options.blending - 1.0);

	for (int i = 0; i < numClusters; ++i) {
	    double distance = vector.sub(centers[i]).abs();
	    if (EQ(distance, 0))
		probabilities[i] = Double.MAX_VALUE;
	    else
		probabilities[i] = Math.pow(1.0 / distance, power);
	}
	// normalization of computed probabilities
	double total = 0.0;
	for (double p : probabilities)
	    total += p;
	for (int i = 0; i < numClusters; ++i)
	    probabilities[i] /= total;
    }
    /**
     * For each vector finds probabilities to lie in each cluster
     *
     * @param numObjects number of vectors
     * @param vectors an array of vectors
     * @param numClusters number of clusters
     * @param centers an array of clusters centers
     * @param options KFuzzy algorithm options
     * @param probabilities resulting array of probabilities, ith row must correspond to ith vector
     */
    private void findProbabilities(int numObjects, Vector[] vectors, int numClusters, Vector[] centers, Options options, double[][] probabilities) {
	for (int i = 0; i < numObjects; ++i)
	    findProbabilities(vectors[i], numClusters, centers, options, probabilities[i]);
    }
    /**
     * By set of vectors and their probabilities to lie in a cluster recomputes cluster center.
     *
     * @param numDimensions number of dimensions in the space (must be equal to number of dimensions in vectors)
     * @param numObjects number of vectors
     * @param vectors an array of vectors
     * @param clusterNumber number of cluster for which center is recomputed (zero based)
     * @param probabilities an two dimensional array of probabilities,
     * ith row is correspond to ith vector and represents an array of
     * probabilities to lie in particular cluster
     * @param options KFuzzy algorithm options
     * @return center of cluster
     */
    private Vector recomputeCenter(int numDimensions, int numObjects, Vector[] vectors, int clusterNumber,
				   double[][] probabilities, Options options) {
	double norm = 0.0;
	for (int i = 0; i < numObjects; ++i)
	    norm += Math.pow(probabilities[i][clusterNumber], options.blending);

	Vector result = new Vector(numDimensions);
	for (int i = 0; i < numObjects; ++i)
	    result = result.add(vectors[i].mul(Math.pow(probabilities[i][clusterNumber], options.blending) / norm));
	return result;
    }
    /**
     * By set of vectors and their probabilities to lie in clusters recompute cluster centers.
     *
     * @param numDimensions number of dimensions in the space (must be equal to number of dimensions in vectors)
     * @param numObjects number of vectors
     * @param vectors an array of vectors
     * @param probabilities an two dimensional array of probabilities,
     * ith row is correspond to ith vector and represents an array of
     * probabilities to lie in particular cluster
     * @param options KFuzzy algorithm options
     * @param numClusters number of clusters
     * @param centers resulting array of clusters centers
     */
    private void recomputeCenters(int numDimensions, int numObjects, Vector[] vectors,
				  double[][] probabilities, Options options, int numClusters, Vector[] centers) {
	for (int i = 0; i < numClusters; ++i)
	    centers[i] = recomputeCenter(numDimensions, numObjects, vectors, i, probabilities, options);
    }
    /**
     * Finds assignment based on probabilities to lie in particular cluster.
     *
     * @param numObjects number of vectors that are clusterized
     * @param numClusters number of clusters to which objects are clusterized
     * @param probabilities probabilities of ith object to lie in jth cluster
     * @return array of assignment, where ith element is a number of a cluster of ith vector
     */
    private int[] findAssignment(int numObjects, int numClusters, double[][] probabilities) {
	int[] assignment = new int[numObjects];

	for (int i = 0; i < numObjects; ++i) {
	    for (int j = 0; j < numClusters; ++j)
		if (probabilities[i][j] > probabilities[i][assignment[i]])
		    assignment[i] = j;
	}
	return assignment;
    }
    /**
     * Constructor sets algorithm that finds cluster centers to the ClusterCentersAdapter implementation
     */
    public KFuzzyAlgorithm() {
	clusterCentersAlgorithm = new ClusterCentersAdapter(new MostDistantSlow());
    }
    /**
     * Constructor specifying an implementation of the ClusterCentersInterface
     */
    public KFuzzyAlgorithm(ClusterCentersInterface clusterCentersAlgorithm) {
	setClusterCentersAlgorithm(clusterCentersAlgorithm);
    }
    /**
     * Set algortihm that finds cluster centers to some implementation of the ClusterCentersInterface
     *
     * @param clusterCentersAlgorithm an realization of algorithm, must not be null
     */
    public void setClusterCentersAlgorithm(ClusterCentersInterface clusterCentersAlgorithm) {
	this.clusterCentersAlgorithm = clusterCentersAlgorithm;
    }
    /**
     * Clusterizes given set of vectors.
     *
     * @param vectors set of vectors, that will be clusterized, must not be null and all vectors must have an equal size (ant not be null)
     * @param numClusters number of clusters to which vectors will be
     * clusterized. Must be between zero and length of vectors array -
     * 1 (inclusive).
     * @param options KFuzzy algorithm options
     * @return an array of cluster indices, where ith index corresponds to ith vector
     */
    public int[] clusterize(Vector[] vectors, int numClusters, Options options) {
	final int numObjects = vectors.length;

	if (numObjects == 0)
	    return new int[] {};
	numClusters = Math.min(numObjects, numClusters);
	if (numClusters == 0)
	    return new int[] {};
	final int numDimensions = vectors[0].getSize();

	assert numObjects > 0 && numClusters > 0 && numClusters <= numObjects;

	Vector[] centers = findCenters(vectors, numClusters);
	double[][] probabilities = new double[numObjects][numClusters];
	findProbabilities(numObjects, vectors, numClusters, centers, options, probabilities);

	for (int iteration = 0; iteration < options.maxIterations; ++iteration) {
	    recomputeCenters(numDimensions, numObjects, vectors, probabilities, options, numClusters, centers);
	    findProbabilities(numObjects, vectors, numClusters, centers, options, probabilities);
	}
	int[] result = findAssignment(numObjects, numClusters, probabilities);
	return result;
    }
}
