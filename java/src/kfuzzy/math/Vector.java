package kfuzzy.math;


import java.util.*;

import kfuzzy.math.Utils;

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
     * A constant that represents a zero floating-point value.
     */
    public final static double ZERO = 0.0;
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
     * @param size number of dimensions. Must be greater or equal to zero.
     */
    public Vector(int size) {
	assert size >= 0 : "size: " + size;

	this.size = size;
	this.components = new double[size];
	Arrays.fill(this.components, ZERO);
    }
    /**
     * Class constructor specifying list of components. Number of dimensions will be equal to the number of components.
     * @param components list of components. Must not be null.
     */
    public Vector(double ... components) {
	assert components != null : "components must not be null";

	this.size = components.length;
	this.components = Arrays.copyOf(components, components.length);
    }
    /**
     * Returns required component of the vector.
     * @param index index of required component. Must be between zero and number of components - 1 (inclusive).
     * @return value of required component
     */
    public double get(int index) {
	assert index >= 0 && index < getSize() : "current size: " + getSize() + ", index = " + index;

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
     * @param other second argument of addition. Number of components in the other vector must be equal to number of components in the current vector.
     * @return new vector, which is the sum of that two vectors
     */
    public Vector add(Vector other) {
	assert getSize() == other.getSize() : "current size: " + getSize() + ", other size: " + other.getSize();

	final int n = getSize();
	double[] buffer = new double[n];
	for (int i = 0; i < n; ++i)
	    buffer[i] = get(i) + other.get(i);
	return new Vector(buffer);
    }
    /**
     * Subtracts from current vector other vector.
     * @param other second argument of subtraction. Number of components in other vector must be equal to number of components in the current vector.
     * @return new vector, which is the difference between that two vectors
     */
    public Vector sub(Vector other) {
	assert getSize() == other.getSize() : "current size: " + getSize() + ", other size: " + other.getSize();

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
	double largest = 0.0, result = 0;
	for (double d : components)
	    largest = Math.max(largest, Math.abs(d));

	if (!Utils.EQ(ZERO, largest)) {
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
