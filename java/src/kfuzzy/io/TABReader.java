package kfuzzy.io;

import java.io.*;
import java.util.*;

import kfuzzy.math.Utils;
import kfuzzy.math.Vector;


public class TABReader extends BasicReader implements ReaderInterface {
    public KFuzzyInput read(InputStreamReader in) throws IOException {
	initialize(in);

	int numDimensions = nextInt(), numClusters = nextInt(), fakeZero = nextInt();

	int numObjects = 0;
	for (int i = 0; i < numClusters; ++i)
	    numObjects = nextInt();

	double unknownValue = nextDouble();

	double[][] values = new double[numObjects][numDimensions];
	int[] totalKnown = new int[numDimensions];
	double[] sumComponents = new double[numDimensions];

	for (int i = 0; i < numObjects; ++i) {
	    for (int j = 0; j < numDimensions; ++j) {
		values[i][j] = nextDouble();
		if (!Utils.EQ(values[i][j], unknownValue)) {
		    sumComponents[j] += values[i][j];
		    ++totalKnown[j];
		}
	    }
	}

	for (int j = 0; j < numDimensions; ++j) {
	    if (totalKnown[j] != 0) {
		sumComponents[j] /= totalKnown[j];
	    }
	}

	Vector[] vectors = new Vector[numObjects];

	for (int i = 0; i < numObjects; ++i) {
	    for (int j = 0; j < numDimensions; ++j)
		if (Utils.EQ(values[i][j], unknownValue))
		    values[i][j] = sumComponents[j];
	    vectors[i] = new Vector(values[i]);
	}

	return new KFuzzyInput(numObjects, numDimensions, vectors, numClusters);
    }
}
