package kfuzzy.algo;

import kfuzzy.math.*;


public interface MostDistantInterface {
    /* Finds m most distant vectors. Returns an array of indexes of
     * corresponding vectors or null in the case of failure. */
    int[] findMostDistant(Vector[] vectors, int m);
}
