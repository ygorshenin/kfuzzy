package kfuzzy.algo;

import java.util.Arrays;
import kfuzzy.math.*;
import kfuzzy.utils.*;


public class MostDistantSlow implements MostDistantInterface {
    // Returns indexes of the m most distant vectors from the given
    // set.  Returns null in the case of failure.
    public int[] findMostDistant(Vector[] vectors, int m) {
	final int n = vectors.length;

	if (m < 0 || m > n)
	    return null;

	for (int i = 1; i < n; ++i)
	    if (vectors[i].getSize() != vectors[i - 1].getSize())
		return null;

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
    // Relaxes distances for vectors int [from; to) interval from vector.
    private void relax(int from, int to, double[] distance, int[] indexes, Vector[] vectors, Vector vector) {
	for (int i = from; i < to; ++i) {
	    double d = vectors[indexes[i]].sub(vector).abs();
	    distance[i] = Math.min(distance[i], d);
	}
    }
    // Returns an index of the most distant vector from vectors in interval [from; to).
    // In the case of an empty interval, returns -1.
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

    private int[] internalFindMostDistant(int n, Vector[] vectors, int m) {
	Pair<Integer, Integer> twoMostDistant = findTwoMostDistantVectors(n, vectors);
	int u = twoMostDistant.first, v = twoMostDistant.second;
	// min distance from an ith object to a vector in resulting set
	double[] distance = new double[n];
	// index of a corresponding object
	int[] indexes = new int[n];

	indexes[0] = twoMostDistant.first;
	indexes[1] = twoMostDistant.second;

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

    // Finds two most distant vectors from the set. Returns their indexes.
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
