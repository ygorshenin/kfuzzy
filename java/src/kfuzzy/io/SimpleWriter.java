package kfuzzy.io;

import java.io.*;
import java.util.*;

/**
 * An implementation of WriterInterface.
 */
public class SimpleWriter implements WriterInterface {
    private void appendInterval(StringBuilder sb, int first, int last) {
	if (first == last)
	    sb.append(first);
	else
	    sb.append(String.format("%d - %d", first, last));
    }

    private String getSequence(ArrayList<Integer> list) {
	if (list.isEmpty())
	    return "";

	Collections.sort(list);

	StringBuilder sb = new StringBuilder();
	int first = list.get(0), last = list.get(0);

	for (int i = 1; i < list.size(); ++i) {
	    int current = list.get(i);
	    if (current == last + 1)
		last = current;
	    else {
		appendInterval(sb, first, last);
		sb.append(", ");
		first = last = current;
	    }
	}
	appendInterval(sb, first, last);

	return sb.toString();
    }

    private String[] getMatching(int numClusters, int numObjects, int[] matching) {
	ArrayList<ArrayList<Integer>> buckets = new ArrayList<ArrayList<Integer>>();

	for (int i = 0; i < numClusters; ++i)
	    buckets.add(new ArrayList<Integer>());
	for (int i = 0; i < numObjects; ++i)
	    buckets.get(matching[i]).add(i);

	String[] result = new String[numClusters];
	for (int i = 0; i < numClusters; ++i)
	    result[i] = getSequence(buckets.get(i));
	return result;
    }

    public boolean write(OutputStreamWriter outputStream, KFuzzyOutput output) {
	try {
	    PrintWriter out = new PrintWriter(outputStream);

	    out.printf("Number of clusters: %d\n", output.getNumClusters());
	    out.printf("Number of objects: %d\n", output.getNumObjects());
	    out.printf("Blending: %f\n", output.getBlending());
	    out.printf("Number of performed iterations: %d\n", output.getNumIterations());
	    out.println();

	    String[] matching = getMatching(output.getNumClusters(), output.getNumObjects(), output.getMatching());
	    for (int i = 0; i < output.getNumClusters(); ++i)
		out.printf("Cluster %d: %s\n", i, matching[i]);

	    return true;
	} catch (Exception e) {
	    e.printStackTrace();
	    return false;
	}
    }
}
