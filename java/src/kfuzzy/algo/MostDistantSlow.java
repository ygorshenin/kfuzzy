package kfuzzy.algo;

import java.util.Arrays;
import kfuzzy.math.*;
import kfuzzy.utils.*;


/**
 * MostDistantSlow is an algorithm to find most distant vectors in the given set.
 *
 * @author Yuri Gorshenin
 * @version 2011.0423
 * @since 1.6
 */
public class MostDistantSlow implements MostDistantInterface {
    public int[] findMostDistant(Vector[] vectors, int m) {
	final int n = vectors.length;

	assert m >= 0 && m <= n : "vectors size: " + n + ", required number of vectors: " + m;

	for (int i = 0; i < n; ++i)
	    assert vectors[i] != null : "vector[" + i + "] is null";
	for (int i = 1; i < n; ++i) {
	    assert vectors[i].getSize() == vectors[i - 1].getSize() :
	    String.format("size of vector[%d] is %d, vector[%d] is %d", i - 1, vectors[i - 1].getSize(), i, vectors[i].getSize());
	}

	if (m == 0)
	    return new int[] {};
	if (m == 1)
	    return new int[] { 0 };
	if (m == n) {
	    int[] result = new int[n];
	    for (int i = 0; i < n; ++i)
		result[i] = i;
	    return result;
	}
	return internalFindMostDistant(n, vectors, m);
    }
    /**
     * Relaxes distance for vectors from [from; to) interval to a given vector.
     *
     * @param from index of the left bound of interval, inclusive
     * @param to index of the right bound of interval, exclusive
     * @param distance array of distances
     * @param indexes maps index in the {@link #distance} to an index in the {@link #vectors} set
     * @param vectors set of vectors
     * @param vector vector, to which all distances will be relaxed
     */
    private void relax(int from, int to, double[] distance, int[] indexes, Vector[] vectors, Vector vector) {
	for (int i = from; i < to; ++i) {
	    double d = vectors[indexes[i]].sub(vector).abs();
	    distance[i] = Math.min(distance[i], d);
	}
    }
    /**
     * Returns an index of the most distant vector in interval [from; to).
     *
     * @param from index of the left bound of interval, inclusive
     * @param to index of the right bound of interval, exclusive
     * @param distance array of distances
     * @return index of the most distant vector, or -1, if given interval is empty
     */
    private int getMostDistant(int from, int to, double[] distance) {
	double best = Double.NEGATIVE_INFINITY;
	int result = -1;

	for (int i = from; i < to; ++i)
	    if (distance[i] > best) {
		best = distance[i];
		result = i;
	    }
	return result;
    }
    /**
     * Returns an array of indices of the most distant vectors from the given set.
     *
     * @param n size of the given set of vectors
     * @param vectors set of vectors
     * @param m number of most distant vectors, that must be finded
     * @return array of indexes of most distant {@link #m} vectors
     */
    private int[] internalFindMostDistant(int n, Vector[] vectors, int m) {
	Pair<Integer, Integer> twoMostDistant = findTwoMostDistantVectors(n, vectors);
	int u = twoMostDistant.first, v = twoMostDistant.second;
	// min distance from an ith object to a vector in resulting set
	double[] distance = new double[n];
	// index of a corresponding object
	int[] indexes = new int[n];

	indexes[0] = u;
	indexes[1] = v;

	for (int w = 0, pos = 2; w < n; ++w)
	    if (w != u && w != v) {
		distance[pos] = Double.POSITIVE_INFINITY;
		indexes[pos] = w;
		++pos;
	    }
	relax(2, n, distance, indexes, vectors, vectors[u]);
	relax(2, n, distance, indexes, vectors, vectors[v]);

	for (int i = 2; i < m; ++i) {
	    int index = getMostDistant(i, n, distance);

	    int intTmp = indexes[i];
	    indexes[i] = indexes[index];
	    indexes[index] = intTmp;

	    double doubleTmp = distance[i];
	    distance[i] = distance[index];
	    distance[index] = doubleTmp;
	}

	return Arrays.copyOf(indexes, m);
    }
    /**
     * Finds two most distant vectors in the given set.
     *
     * @param n size of the given set of vectors, must be greater of equal than 2
     * @param vectors set of vectors, all vectors must have an equal number of dimensions and not be null, must have exactly {@link #n} elements
     * @return pair of two indices of the most distant vectors
     */
    private Pair<Integer, Integer> findTwoMostDistantVectors(int n, Vector[] vectors) {
	int u = 0, v = 1;

	double best = Double.NEGATIVE_INFINITY;
	for (int i = 0; i < n; ++i)
	    for (int j = i + 1; j < n; ++j) {
		double tmp = vectors[j].sub(vectors[i]).abs();
		if (tmp > best) {
		    best = tmp;
		    u = i;
		    v = j;
		}
	    }
	return new Pair<Integer, Integer>(u, v);
    }
}
