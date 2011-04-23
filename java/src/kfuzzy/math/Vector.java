package kfuzzy.math;

import java.util.*;


/**
 * Class Vector represents multidimensional vector in the Eucldian space.
 * Vector is an immutible object.
 *
 * @author Yuri Gorshenin
 * @version 2011.0418
 * @since 1.6
 */
public class Vector {
    /**
     * Maximum difference between two distinct floating-point values for which they are considered as equal.
     */
    public final static double EPSILON = 1e-9;
    /**
     * A constant that represents a zero floating-point value.
     */
    public final static double ZERO = 0.0;
    /**
     * Checks that two floating-point numbers differ by no more than {@link #EPSILON}.
     * @param u first of the two numbers
     * @param v second of the two numbers
     * @return true, if difference between two numbers is no more than {@link #EPSILON} in absolute value.
     */
    private final static boolean EQ(double u, double v) {
	return Math.abs(u - v) < EPSILON;
    }
    /**
     * Number of dimensions of the current vector.
     */
    private int size;
    /**
     * Array of components of the current vector.
     */
    private double[] components;
    /**
     * Class constructor specifying number of dimensions. All components of the vector will equal to {@link #ZERO}.
     * @param size number of dimensions
     */
    public Vector(int size) {
	assert size > 0 : "size: " + size;

	this.size = size;
	this.components = new double[size];
	Arrays.fill(this.components, ZERO);
    }
    /**
     * Class constructor specifying list of components. Number of dimensions will be equal to the number of components.
     * @param components list of components
     */
    public Vector(double ... components) {
	assert components != null : "components must not be null";
	assert components.length > 0 : "components must not be empty";

	this.size = components.length;
	this.components = Arrays.copyOf(components, components.length);
    }
    /**
     * Returns required component of the vector.
     * @param index index of required component
     * @return value of required component
     */
    public double get(int index) {
	return components[index];
    }
    /**
     * Returns number of components of the vector.
     * @return number of components of the vector
     */
    public int getSize() {
	return size;
    }
    /**
     * Adds vector to current vector.
     * @param other second argument of addition
     * @return new vector, which is the sum of that two vectors
     */
    public Vector add(Vector other) {
	assert getSize() == other.getSize(): "current size: " + getSize() + ", other size: " + other.getSize();

	final int n = getSize();
	double[] buffer = new double[n];
	for (int i = 0; i < n; ++i)
	    buffer[i] = get(i) + other.get(i);
	return new Vector(buffer);
    }
    /**
     * Subtracts from current vector other vector.
     * @param other second argument of subtraction
     * @return new vector, which is the difference between that two vectors
     */
    public Vector sub(Vector other) {
	assert getSize() == other.getSize(): "current size: " + getSize() + ", other size: " + other.getSize();

	final int n = getSize();
	double[] buffer = new double[n];
	for (int i = 0; i < n; ++i)
	    buffer[i] = get(i) - other.get(i);
	return new Vector(buffer);
    }
    /**
     * Multiplies current vector by the number.
     * @param m value by which vector is multiplied
     * @return new vector, which is the current vector multiplied by the number
     */
    public Vector mul(double m) {
	final int n = getSize();
	double[] buffer = new double[n];
	for (int i = 0; i < n; ++i)
	    buffer[i] = m * get(i);
	return new Vector(buffer);
    }
    /**
     * Returns absolute value of the vector.
     * @return absolute value of the vector
     */
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
