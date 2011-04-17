package kfuzzy.math;

import java.util.*;


public class Vector {
    public final static double EPSILON = 1e-9;
    public final static double ZERO = 0.0;

    private final static boolean EQ(double u, double v) {
	return Math.abs(u - v) < EPSILON;
    }

    private int size;
    private double[] components;

    public Vector(int size) {
	assert size > 0 : "size: " + size;

	this.size = size;
	this.components = new double[size];
	Arrays.fill(this.components, ZERO);
    }

    public Vector(double ... components) {
	assert components != null : "components must not be null";
	assert components.length > 0 : "components must not be empty";

	this.size = components.length;
	this.components = Arrays.copyOf(components, components.length);
    }

    public double get(int index) {
	return components[index];
    }

    public int getSize() {
	return size;
    }

    public Vector add(Vector other) {
	assert getSize() == other.getSize(): "current size: " + getSize() + ", other size: " + other.getSize();

	final int n = getSize();
	double[] buffer = new double[n];
	for (int i = 0; i < n; ++i)
	    buffer[i] = get(i) + other.get(i);
	return new Vector(buffer);
    }

    public Vector sub(Vector other) {
	assert getSize() == other.getSize(): "current size: " + getSize() + ", other size: " + other.getSize();

	final int n = getSize();
	double[] buffer = new double[n];
	for (int i = 0; i < n; ++i)
	    buffer[i] = get(i) - other.get(i);
	return new Vector(buffer);
    }

    public Vector mul(double m) {
	final int n = getSize();
	double[] buffer = new double[n];
	for (int i = 0; i < n; ++i)
	    buffer[i] = m * get(i);
	return new Vector(buffer);
    }

    public double abs() {
	double largest = Double.NEGATIVE_INFINITY, result = 0;
	for (double d : components)
	    largest = Math.max(largest, Math.abs(d));

	if (!EQ(ZERO, largest)) {
	    double t;
	    for (double d : components) {
		t = d / largest;
		result += t * t;
	    }
	    result = largest * Math.sqrt(result);
	}
	return result;
    }
}
