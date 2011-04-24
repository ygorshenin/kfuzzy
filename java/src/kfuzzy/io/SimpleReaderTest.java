package kfuzzy.io;

import java.io.*;
import java.util.*;

import kfuzzy.math.Vector;
import kfuzzy.utils.TestingUtils;


/**
 * Class contains several tests for SimpleReader class.
 *
 * @author Yuri Gorshenin
 * @version 2011.0418
 * @since 1.6
 */
public class SimpleReaderTest extends TestingUtils {
    /**
     * An implementation of SimpleReader class
     */
    private ReaderInterface reader;
    /**
     * Wraps reading input from a string
     *
     * @param buffer a string, from which input will be readed
     * @return an input of KFuzzy algorithm
     */
    private KFuzzyInput readFromString(String buffer) throws IOException {
	return reader.read(new InputStreamReader(new ByteArrayInputStream(buffer.getBytes())));
    }

    public void setUp() {
	reader = new SimpleReader();
    }
    /**
     * Tests reading with empty set of vectors
     */
    public void testEmpty() {
	final String buffer = "0 10 4";

	try {
	    KFuzzyInput input = readFromString(buffer);
	    assertEquals(0, input.getNumObjects());
	    assertEquals(4, input.getNumClusters());
	    assertEquals(0, input.getVectors().length);
	} catch (IOException e) {
	    fail(e.getMessage());
	}
    }
    /**
     * Tests reading from very small set of vectors
     */
    public void testSimple() {
	final String buffer =
	    "3 2 3\n" +
	    "1.0 2.0\n" +
	    "2.0 3.0\n" +
	    "-1.5 3.5\n";

	try {
	    KFuzzyInput input = readFromString(buffer);

	    assertEquals(3, input.getNumObjects());
	    assertEquals(3, input.getNumClusters());

	    Vector[] vectors = input.getVectors();
	    Arrays.sort(vectors, new LexicalVectorComparator());
	    checkVector(new double[] { -1.5, +3.5 }, vectors[0]);
	    checkVector(new double[] { +1.0, +2.0 }, vectors[1]);
	    checkVector(new double[] { +2.0, +3.0 }, vectors[2]);
	} catch (IOException e) {
	    fail(e.getMessage());
	}
    }
}
