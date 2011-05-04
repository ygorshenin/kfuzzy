package kfuzzy.io;

import java.io.*;
import java.util.*;

import kfuzzy.io.BasicReader;
import kfuzzy.math.Vector;


/**
 * An implementation of ReaderInterface. Reads simple format, that
 * looks like: "number of vectors" "number of dimensions of each
 * vector" "number of clusters" followed by an enumeration of vectors.
 *
 * For instance:
 *
 * 4 2 3
 * 1.0 2.0
 * 1.1 2.1
 * 5.0 1.0
 * 5.1 1.0
 *
 * Here are four vectors, each has two components and these vectors
 * must be clusterized into three clusters.
 *
 * @author Yuri Gorshenin
 * @version 2011.0424
 * @since 1.6
 */
public class SimpleReader extends BasicReader implements ReaderInterface {
    public KFuzzyInput read(InputStreamReader inputStream) throws IOException {
	initialize(inputStream);

	int numObjects = nextInt(), numDimensions = nextInt(), numClusters = nextInt();

	Vector[] vectors = new Vector[numObjects];
	double[] components = new double[numDimensions];
	for (int i = 0; i < numObjects; ++i) {
	    for (int j = 0; j < numDimensions; ++j)
		components[j] = nextDouble();
	    vectors[i] = new Vector(components);
	}
	return new KFuzzyInput(numObjects, numDimensions, vectors, numClusters);
    }
}
