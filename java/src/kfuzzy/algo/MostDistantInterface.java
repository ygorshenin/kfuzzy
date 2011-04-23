package kfuzzy.algo;

import kfuzzy.math.Vector;


/**
 * Interface to algorithms, that finds some number of the most distant
 * vectors from the given set.
 *
 * @author Yuri Gorshenin
 * @version 2011.0423
 * @since 1.6
 */
public interface MostDistantInterface {
    /**
     * Finds indices of some number of most distant vectors in the given set.
     *
     * @param vectors given set of vectors, must not be null, all elements must have an equal number of dimensions and not be null.
     * @param m number of most distant vectors, that must be finded. Must be between zero and the number of given vectors (inclusive).
     * @return array of indices of the most distant vectors.
     */
    int[] findMostDistant(Vector[] vectors, int m);
}
