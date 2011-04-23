package kfuzzy.algo;

import kfuzzy.math.Vector;


/**
 * Interface to algorithms, that finds some number of the most distant
 * vectors from the given set.
 *
 * @author Yuri Gorshenin
 * @version 2011.0424
 * @since 1.6
 */
public interface ClusterCentersInterface {
    /**
     * Finds some set of the most distant vectors from the given set.
     *
     * @param vectors an array of vectors, in which the most distant
     * vectors will be finded. Must not be null, all vectors must have
     * the same size and not be null.
     * @param m the number of the most distant vectors, that will be finded. Must be between zero and length of vectors array - 1 (inclusive).
     * @return an array of the most distant vectors.
     */
    Vector[] findClusterCenters(Vector[] vectors, int m);
}
