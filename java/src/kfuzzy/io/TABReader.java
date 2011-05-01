package kfuzzy.io;

import java.io.*;
import java.util.*;

import kfuzzy.math.Utils;
import kfuzzy.math.Vector;


public class TABReader extends BasicReader implements ReaderInterface {
    public KFuzzyInput read(InputStreamReader in) throws IOException {
	initialize(in);

	int numDimensions = nextInt(), numTypes = nextInt(), fakeZero = nextInt();

	for (int i = 0; i < numTypes; ++i)
	    nextInt();
	double unknownValue = nextDouble();

	ArrayList<String> lines = new ArrayList<String>();
	while (true) {
	    String line = super.in.readLine().trim();
	    if (line == null || line.isEmpty())
		break;
	    lines.add(line);
	}

	int numClusters = lines.size();

	double[][] values = new double[numClusters][numDimensions];
	int[] totalKnown = new int[numDimensions];
	double[] sumComponents = new double[numDimensions];

	for (int i = 0; i < numClusters; ++i) {
	    eat(lines.get(i));
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

	Vector[] vectors = new Vector[numClusters];

	for (int i = 0; i < numClusters; ++i) {
	    for (int j = 0; j < numDimensions; ++j)
		if (Utils.EQ(values[i][j], unknownValue))
		    values[i][j] = sumComponents[j];
	    vectors[i] = new Vector(values[i]);
	}

	return new KFuzzyInput(numClusters, vectors, numDimensions, numClusters);
    }
}
