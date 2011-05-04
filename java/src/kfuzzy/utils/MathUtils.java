package kfuzzy.utils;

import java.util.*;

/**
 * Contains several common math constants and functions
 *
 * @author Yuri Gorshenin
 * @version 2011.0505
 * @since 1.6
 */
public class MathUtils {
    /**
     * Power of floating-point comparator
     */
    public final static double EPSILON = 1e-9;
    /**
     * Checks that two floating-point numbers differ by no more than {@link #EPSILON}.
     * @param u first of the two numbers
     * @param v second of the two numbers
     * @return true, if difference between two numbers is no more than {@link #EPSILON} in absolute value.
     */
    public static boolean EQ(double u, double v) {
	return Math.abs(u - v) < EPSILON;
    }
}
