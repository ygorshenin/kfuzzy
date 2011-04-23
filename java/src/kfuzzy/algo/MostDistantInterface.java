package kfuzzy.algo;

import kfuzzy.math.Vector;


/**
 * Interface to algorithms, that finds some number of most distant
 * vectors.
 *
 * @author Yuri Gorshenin
 * @version 2011.0423
 * @since 1.6
 */
public interface MostDistantInterface {
    /**
     * Finds indexes of some number of most distant vectors.
     *
     * @param vectors array of vectors. Must not be null, all elements must have equal number of dimensions and not be null.
     * @param m number of most distant vectors, that must be finded. Must be between zero and number of given vectors (inclusive).
     * @return array of indexes of most distant vectors.
     */
    int[] findMostDistant(Vector[] vectors, int m);
}
