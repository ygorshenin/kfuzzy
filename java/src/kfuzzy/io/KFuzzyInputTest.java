package kfuzzy.io;

import java.util.*;

import kfuzzy.math.Vector;
import kfuzzy.utils.TestingUtils;


public class KFuzzyInputTest extends TestingUtils {
    private void checkKFuzzyInput(KFuzzyInput current, KFuzzyInput expected) {
	assertEquals(current.getNumObjects(), expected.getNumObjects());
	assertEquals(current.getNumDimensions(), expected.getNumDimensions());
	assertEquals(current.getNumClusters(), expected.getNumClusters());

	Vector[] a = current.getVectors(), b = expected.getVectors();
	assertEquals(a.length, b.length);
	Arrays.sort(a, new LexicalVectorComparator());
	Arrays.sort(b, new LexicalVectorComparator());
	for (int i = 0; i < a.length; ++i)
	    assertTrue(a[i].equals(b[i]));
    }

    public void testSimple() {
	KFuzzyInput current = new KFuzzyInput(3, 2, new Vector[] { new Vector(1.0, -1.2),
								   new Vector(2.0, 3.1),
								   new Vector(-5.3, 1.1) }, 1);
	KFuzzyInput expected = new KFuzzyInput(3, 2, new Vector[] { new Vector(-5.3, 1.1),
								    new Vector(2.0, 3.1),
								    new Vector(1.0, -1.2) }, 1);
       	checkKFuzzyInput(expected, current);
    }
}
