package kfuzzy.io;

import java.io.*;
import java.util.*;


public class BasicReader {
    protected BufferedReader in;
    protected StringTokenizer st;


    protected void eat(String line) {
	st = new StringTokenizer(line);
    }

    protected String next() throws IOException {
	while (!st.hasMoreTokens()) {
	    String line = in.readLine();
	    if (line == null)
		return null;
	    eat(line);
	}
	return st.nextToken();
    }

    protected void initialize(InputStreamReader in) {
	this.in = new BufferedReader(in);
	eat("");
    }

    protected int nextInt() throws IOException {
	return Integer.parseInt(next());
    }

    protected double nextDouble() throws IOException {
	return Double.parseDouble(next());
    }
}
