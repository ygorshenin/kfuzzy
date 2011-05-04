package kfuzzy.math;

import junit.framework.TestCase;
import kfuzzy.utils.MathUtils;
import kfuzzy.utils.TestingUtils;

/**
 * Class contains several tests for Vector class.
 *
 * @author Yuri Gorshenin
 * @version 2011.0418
 * @since 1.6
 */
public class VectorTest extends TestingUtils {
    /**
     * Tests construction of vectors.
     */
    public void testConstructor() {
	final int size = 4;

	double[] content = new double[] { 0, 0, 0, 0 };
	Vector vector = new Vector(size);
	checkVector(content, vector);

	content = new double[] { 1.5, -2.2, 3.1415, 19.5 };
	vector = new Vector(content);
	checkVector(content, vector);

	content = new double[] { 1.0, 2.0, 3.0, 4.0 };
	vector = new Vector(content);
	checkVector(content, vector);

	vector = new Vector(0);
	vector = new Vector();
    }
    /**
     * Tests addition of vectors.
     */
    public void testAdd() {
	final int size = 3;

	double[] uContent = new double[] { 1.0, 3.14, 5.5 };
	double[] vContent = new double[] { 2.0, 1.1, -7.4 };
	double[] wContent = new double[] { 3.0, 4.24, -1.9 };

	Vector u = new Vector(uContent);
	Vector v = new Vector(vContent);
	Vector w = u.add(v);

	checkVector(uContent, u);
	checkVector(vContent, v);
	checkVector(wContent, w);
    }
    /**
     * Tests subtraction of vectors.
     */
    public void testSub() {
	final int size = 3;

	double[] uContent = new double[] { -5.12, 17.81, 3.33 };
	double[] vContent = new double[] { -7.18, 21.20, 4.11 };
	double[] wContent = new double[] { 2.06, -3.39, -0.78 };

	Vector u = new Vector(new double[] { -5.12, 17.81, 3.33 });
	Vector v = new Vector(new double[] { -7.18, 21.20, 4.11 });
	Vector w = u.sub(v);

	checkVector(uContent, u);
	checkVector(vContent, v);
	checkVector(wContent, w);
    }
    /**
     * Tests multiplication of vectors by floating-point values.
     */
    public void testMul() {
	final int size = 4;

	double[] uContent = new double[] { 1.0, 2.0, 3.0, 4.0 };
	double[] vContent = new double[] { 0.5, 1.0, 1.5, 2.0 };

	Vector u = new Vector(uContent);
	Vector v = u.mul(0.5);

	checkVector(uContent, u);
	checkVector(vContent, v);
    }
    /**
     * Tests absolute values of vectors.
     */
    public void testAbs() {
	assertEquals(5.0, new Vector(+3.0, +4.0).abs(), MathUtils.EPSILON);
	assertEquals(5.0, new Vector(-3.0, +4.0).abs(), MathUtils.EPSILON);
	assertEquals(5.0, new Vector(-3.0, -4.0).abs(), MathUtils.EPSILON);
	assertEquals(5.0, new Vector(+3.0, -4.0).abs(), MathUtils.EPSILON);

	assertEquals(0.0, new Vector(0).abs(), MathUtils.EPSILON);
	assertEquals(0.0, new Vector(new double[] {}).abs(), MathUtils.EPSILON);
	assertEquals(0.0, new Vector(0.0, 0.0, 0.0, 0.0).abs(), MathUtils.EPSILON);
    }
    /**
     * Tests vectors for equality
     */
    public void testEquals() {
	assertFalse(new Vector(0).equals(new Vector(0.0)));
	assertTrue(new Vector(0).equals(new Vector(0)));
	assertTrue(new Vector(10).equals(new Vector(10)));

	assertTrue(new Vector(1, 2, 3).equals(new Vector(1, 2, 3)));
	assertFalse(new Vector(1, 2, 3).equals(new Vector(1, 2, 4)));
	assertFalse(new Vector(1, 2).equals(new Vector(1, 2, 3)));
    }
}
