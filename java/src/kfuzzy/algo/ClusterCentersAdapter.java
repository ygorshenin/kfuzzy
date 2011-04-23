package kfuzzy.algo;

import kfuzzy.math.Vector;

/**
 * ClusterCentersAdapter finds some number of the most distant
 * vectors. Actually it transforms output, returned by any
 * MostDistantInterface implementor to an array of vectors.
 *
 * @author Yuri Gorshenin
 * @version 2011.0423
 * @since 1.6
 */
public class ClusterCentersAdapter implements ClusterCentersInterface {
    /**
     * An implementation of MostDistantInterface
     */
    private MostDistantInterface adaptee;
    /**
     * Class constructor, by default an instance of the
     * MostDistantSlow will be used as algorithm.
     */
    public ClusterCentersAdapter() {
	adaptee = new MostDistantSlow();
    }
    /**
     * Class constructor specifying implementation of the MostDistantInterface
     *
     * @param adaptee an implementation of the MostDistantInterface
     */
    public ClusterCentersAdapter(MostDistantInterface adaptee) {
	this.adaptee = adaptee;
    }

    public Vector[] findClusterCenters(Vector[] vectors, int m) {
	int[] indexes = adaptee.findMostDistant(vectors, m);
	Vector[] result = new Vector[m];
	for (int i = 0; i < m; ++i)
	    result[i] = vectors[indexes[i]];
	return result;
    }
}
