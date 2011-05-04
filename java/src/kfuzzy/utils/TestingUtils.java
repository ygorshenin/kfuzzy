package kfuzzy.utils;

import java.util.*;
import junit.framework.TestCase;

import kfuzzy.math.Vector;
import kfuzzy.utils.MathUtils;


/**
 * Contains several common testing procedures and classes
 *
 * @author Yuri Gorshenin
 * @version 2011.0418
 * @since 1.6
 */
public class TestingUtils extends TestCase {
    /**
     * Lexical comparision of vectors
     *
     * @author Yuri Gorshenin
     * @version 2011.0418
     * @since 1.6
     */
    public static class LexicalVectorComparator implements Comparator<Vector> {
	@Override public int compare(Vector lhs, Vector rhs) {
	    assert lhs.getSize() == rhs.getSize() : "fail in vector comparision: different size";

	    final int size = lhs.getSize();

	    for (int i = 0; i < size; ++i) {
		if (lhs.get(i) < rhs.get(i))
		    return -1;
		else if (lhs.get(i) > rhs.get(i))
		    return +1;
	    }
	    return 0;
	}
    }
    /**
     * Checks, that vector is consist of array of components.
     * In the case of failure, raises AssertionError.
     *
     * @param v vector that will be checked
     * @param components expected array of components
     */
    public void checkVector(double[] components, Vector v) {
	assertEquals(components.length, v.getSize());
	for (int i = 0; i < components.length; ++i)
	    assertEquals(components[i], v.get(i), MathUtils.EPSILON);
    }
}
