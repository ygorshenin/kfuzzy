package kfuzzy.math;

import junit.framework.*;


public class VectorTest extends TestCase {
    public void testConstructor() {
	final int size = 4;

	double[] content = new double[] { 0, 0, 0, 0 };
	Vector vector = new Vector(size);
	checkVector(vector, content);

	content = new double[] { 1.5, -2.2, 3.1415, 19.5 };
	vector = new Vector(content);
	checkVector(vector, content);

	content = new double[] { 1.0, 2.0, 3.0, 4.0 };
	vector = new Vector(content);
	checkVector(vector, content);
    }

    public void testAdd() {
	final int size = 3;

	double[] uContent = new double[] { 1.0, 3.14, 5.5 };
	double[] vContent = new double[] { 2.0, 1.1, -7.4 };
	double[] wContent = new double[] { 3.0, 4.24, -1.9 };

	Vector u = new Vector(uContent);
	Vector v = new Vector(vContent);
	Vector w = u.add(v);

	checkVector(u, uContent);
	checkVector(v, vContent);
	checkVector(w, wContent);
    }

    public void testSub() {
	final int size = 3;

	double[] uContent = new double[] { -5.12, 17.81, 3.33 };
	double[] vContent = new double[] { -7.18, 21.20, 4.11 };
	double[] wContent = new double[] { 2.06, -3.39, -0.78 };

	Vector u = new Vector(new double[] { -5.12, 17.81, 3.33 });
	Vector v = new Vector(new double[] { -7.18, 21.20, 4.11 });
	Vector w = u.sub(v);

	checkVector(u, uContent);
	checkVector(v, vContent);
	checkVector(w, wContent);
    }

    public void testMul() {
	final int size = 4;

	double[] uContent = new double[] { 1.0, 2.0, 3.0, 4.0 };
	double[] vContent = new double[] { 0.5, 1.0, 1.5, 2.0 };

	Vector u = new Vector(uContent);
	Vector v = u.mul(0.5);

	checkVector(u, uContent);
	checkVector(v, vContent);
    }

    public void testAbs() {
	assertEquals(5.0, new Vector(+3.0, +4.0).abs());
	assertEquals(5.0, new Vector(-3.0, +4.0).abs());
	assertEquals(5.0, new Vector(-3.0, -4.0).abs());
	assertEquals(5.0, new Vector(+3.0, -4.0).abs());
    }

    public void checkVector(Vector v, double[] components) {
	assertEquals(components.length, v.getSize());
	for (int i = 0; i < components.length; ++i)
	    assertEquals(components[i], v.get(i), Vector.EPSILON);
    }
}
