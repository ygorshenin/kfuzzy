package kfuzzy.algo;

import java.util.*;
import junit.framework.TestCase;

import kfuzzy.math.Vector;


public class MostDistantSlowTest extends TestCase {
    private MostDistantInterface strategy;

    public void setUp() {
	strategy = new MostDistantSlow();
    }

    public void testIncorrect() {
	assertNull(strategy.findMostDistant(new Vector[0], -1));
	assertNull(strategy.findMostDistant(new Vector[0], +1));
	assertNull(strategy.findMostDistant(new Vector[0], +2));

	final int size = 10, dimensions = 3;
	Vector[] vectors = new Vector[size];
	for (int i = 0; i < size; ++i)
	    vectors[i] = new Vector(dimensions);

	assertNull(strategy.findMostDistant(vectors, -10));
	assertNull(strategy.findMostDistant(vectors, size + 1));
	assertNull(strategy.findMostDistant(vectors, 2 * size));

	vectors = new Vector[] { new Vector(1), new Vector(2), new Vector(3) };
	assertNull(strategy.findMostDistant(vectors, 1));
	vectors = new Vector[] { new Vector(2), new Vector(2), new Vector(2), new Vector(3) };
	assertNull(strategy.findMostDistant(vectors, 3));
    }

    public void testZero() {
	int[] result;

	result = strategy.findMostDistant(new Vector[0], 0);
	assertNotNull(result);
	assertEquals(0, result.length);

	final int size = 10, dimensions = 4;
	Vector[] vectors = new Vector[size];
	for (int i = 0; i < size; ++i)
	    vectors[i] = new Vector(dimensions);

	result = strategy.findMostDistant(vectors, 0);
	assertNotNull(result);
	assertEquals(0, result.length);
    }

    public void testOne() {
	int[] result;

	result = strategy.findMostDistant(new Vector[] { new Vector(3) }, 1);
	assertNotNull(result);
	assertEquals(1, result.length);
	assertEquals(0, result[0]);

	result = strategy.findMostDistant(new Vector[] { new Vector(2), new Vector(2), new Vector(2) }, 1);
	assertNotNull(result);
	assertEquals(1, result.length);
	assertTrue(result[0] >= 0 && result[0] < 3);
    }

    public void testTwo() {
	int[] result;

	result = strategy.findMostDistant(new Vector[] { new Vector(3), new Vector(3) }, 2);
	assertNotNull(result);
	assertEquals(2, result.length);
	assertTrue((result[0] == 0 && result[1] == 1) || (result[0] == 1 && result[1] == 0));

	result = strategy.findMostDistant(new Vector[] { new Vector(0.0, 0.0, 0.0), new Vector(0.0, 3.0, 0.0), new Vector(0.0, 0.0, 4.0) }, 2);
	assertNotNull(result);
	assertEquals(2, result.length);
	assertTrue((result[0] == 1 && result[1] == 2) || (result[0] == 2 && result[1] == 1));
    }

    public void testManyAllEqual() {
	int[] result = strategy.findMostDistant(new Vector[] { new Vector(2), new Vector(2), new Vector(2), new Vector(2) }, 4);
	assertNotNull(result);
	assertEquals(4, result.length);
	Arrays.sort(result);

	assertEquals(0, result[0]);
	assertEquals(1, result[1]);
	assertEquals(2, result[2]);
	assertEquals(3, result[3]);
    }

    public void testFour() {
	final int size = 4;
	int[] result;

	Vector[] vectors = new Vector[] { new Vector(-1.0, +0.0), new Vector(+1.0, +2.0), new Vector(+4.0, +2.0), new Vector(-1.0, -1.0) };

	result = strategy.findMostDistant(vectors, 0);
	assertNotNull(result);
	assertEquals(0, result.length);

	result = strategy.findMostDistant(vectors, 1);
	assertNotNull(result);
	assertEquals(1, result.length);
	assertTrue(result[0] >= 0 && result[0] < size);

	result = strategy.findMostDistant(vectors, 2);
	assertNotNull(result);
	assertEquals(2, result.length);
	Arrays.sort(result);
	assertTrue(result[0] == 2 && result[1] == 3);

	result = strategy.findMostDistant(vectors, 3);
	assertNotNull(result);
	assertEquals(3, result.length);
	Arrays.sort(result);
	assertTrue(result[0] == 1 && result[1] == 2 && result[2] == 3);

	result = strategy.findMostDistant(vectors, 4);
	assertNotNull(result);
	assertEquals(4, result.length);
	Arrays.sort(result);
	assertTrue(result[0] == 0 && result[1] == 1 && result[2] == 2 && result[3] == 3);
    }
}
